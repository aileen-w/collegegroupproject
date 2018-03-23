<?php

require_once ("const.php");

// library reponsible for sending email (use SMTP protocol)
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
require_once ("PHPMailer/src/Exception.php");
require_once ("PHPMailer/src/PHPMailer.php");
require_once ("PHPMailer/src/SMTP.php");


/**
 * Connect with database
 *
 * @return PDO
 */
function conn()
{

    $dsn = "mysql:host=" . HOST . ";dbname=" . DB . ";charset=" . CHARSET;
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    try
    {
        $pdo = new PDO($dsn, USER, PASS, $opt);
    }
    catch(Exception $e)
    {
        die('<div class="alert alert-danger" role="alert">Oh snap! ' . $e->getMessage() . '</div>');
    }

    return $pdo;

};

/**
 * Close connection with database
 *
 * @param null $pdo
 * @return null
 */
function closeDB($pdo = null)
{
    return $pdo = null;
}

/**
 * Record errors
 *
 * @param $pdo
 * @param $msg
 * @return string
 */
function errorLog($pdo, $msg)
{

    try
    {
        $sql = 'INSERT INTO errorlog(date, message) VALUES(NOW(), :message)';
        $stmt = $pdo->prepare($sql);
        $stmt->execute(array(':message' => $msg));
    }
    catch(Exception $ex)
    {
        return '<div class="alert alert-danger" role="alert">Oh snap! ' . $ex->getMessage() . '</div>';
    }

}

/**
 * Authenticate user
 *
 * @param $pdo
 */
function auth($pdo)
{

    if(!empty($_COOKIE["cit_project_user"]))
    {
        try
        {
            $sql = 'select token, date from tokens where token = :token limit 1';
            $stmt = $pdo->prepare($sql);
            $params = array("token" => $_COOKIE["cit_project_user"]);
            $stmt->execute($params);

            if($stmt->rowCount())
            {
                $results = $stmt->fetchAll();
                //validate time
                $today = date("Y-m-d H:i:s");
                $date1=date_create($today);
                $date2=date_create($results[0]['date']);
                $diff=date_diff($date1,$date2);

                list($token, $hmac) = explode(':', $_COOKIE["cit_project_user"], 2);

                // redirect user to login page if cookie was tempered
                if ($hmac != hash_hmac('sha256', $token, SALT) ||
                    $results[0]['token'] != $_COOKIE["cit_project_user"] ||
                    $diff->days > 3) {
                    header("Location: login.php");
                }
            }
            else
            {
                header("Location: login.php");
            }
        }
        catch(Exception $ex)
        {
            errorLog($pdo, __LINE__ . ':' . $ex->getMessage());
        }

    }
    else
    {
        header("Location: login.php");
        die();
    }

}

/**
 * Login
 *
 * @param $pdo
 * @param $email
 * @param $pwd
 * @return string
 *
 */
function login($pdo, $email, $pwd)
{

    $loginError = false;

    if (!empty($email) && !empty($pwd)) {

        $email = filter_var((trim($email)), FILTER_SANITIZE_EMAIL);
        $pwd = filter_var((trim($pwd)), FILTER_SANITIZE_STRING);

        if ($email && $pwd) {

            $options = [
                'salt' => SALT, //write your own code to generate a suitable salt
                'cost' => 12 // the default cost is 10
            ];
            $pwdHash = password_hash($pwd, PASSWORD_DEFAULT, $options);

            $sql = 'select * from user where email = :email and pwd = :pwd limit 1;';
            $params = array(
                ":email" => $email,
                ":pwd" => $pwdHash
            );
            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);

            if ($stmt->rowCount()) {
                // Success!
                $results = $stmt->fetchAll();
                if(password_verify($pwd, $results[0]['pwd']))
                {
                    if ($results[0]['logAttempts'] > 10) {
                        return '<div class="alert alert-warning" role="alert">You have reached maximum number of login attempts. Please contact administrator to unlock your account.</div>';
                    } else {
                        // update users login attempts to zero as he logged in successfully
                        $sql = 'update user set logAttempts = 0 where email = :email and id = :id;';
                        $params = array(
                            ":email" => $email,
                            ":id" => $results[0]['id']
                        );
                        $stmt = $pdo->prepare($sql);
                        $stmt->execute($params);
                    }

                    // generate unique token for this session
                    $randomToken = hash('sha256', uniqid(mt_rand(), true) . uniqid(mt_rand(), true));
                    $randomToken .= ':' . hash_hmac('sha256', $randomToken, SALT);

                    // store unique token in db against users id
                    $sql = 'insert into tokens(token, userid, date) values(:token, :userid, NOW())';
                    $params = array(
                        ":token" => $randomToken,
                        ":userid" => $results[0]['id']
                    );
                    $stmt = $pdo->prepare($sql);
                    $stmt->execute($params);

                    // store this token in cookie
                    if (!isset($_COOKIE["cit_project_user"])) {
                        setcookie("cit_project_user", "", time() - 3600);
                    }
                    setcookie("cit_project_user", $randomToken, time() + 1 * 24 * 60 * 60);

                }
                else
                {

                    $loginError = true;

                }

            }
            else
            {

                $loginError = true;

            }

        }
        else
        {

            $loginError = true;

        }
    }
    else
    {

        $loginError = true;

    }

    if($loginError)
    {
        // Invalid credentials
        // log how many times user was trying to log in, 3 is max
        $sql = 'update user set logAttempts = logAttempts + 1 where email = :email;';
        $params = array(
            ":email" => $email
        );
        $stmt = $pdo->prepare($sql);
        $stmt->execute($params);
        return '<div class="alert alert-warning" role="alert">Invalid email and password combination.</div>';
    }
    else
    {

        header("Location: index.php");

    }

}

/**
 * Insert new registered user data into db
 *
 * @param $pdo
 * @param $email
 * @param $pwd
 * @param $rpwd
 * @return string
 */
function register($pdo, $email, $pwd, $rpwd){

//    $registerError = false;

    if(!empty($email) && !empty($pwd) && !empty($rpwd))
    {

        if($pwd != $rpwd)
        {
            errorLog($pdo, __LINE__ . ':' . "Password mismatch");
            return '<div class="alert alert-warning" role="alert">Password mismatch.</div>';
        }

        $email = filter_var ( (trim($email)), FILTER_SANITIZE_EMAIL );
        $pwd = filter_var ( (trim($pwd)), FILTER_SANITIZE_STRING );
        $hash = generateRandomString(128);
        $options = [
            'salt' => SALT, //write your own code to generate a suitable salt
            'cost' => 12 // the default cost is 10
        ];

        if($email && $pwd){
            $pwd = password_hash($pwd, PASSWORD_DEFAULT, $options);

            $sql = 'select * from user where email = :email limit 1;';
            $params = array(
                ":email" => $email,
            );
            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);

            if ($stmt->rowCount()) {
                errorLog($pdo, __LINE__ . ':' . "Email already exist");
                return '<div class="alert alert-warning" role="alert">This email address already exist in our databse.</div>';
            }
        } else {
            errorLog($pdo, __LINE__ . ':' . "Invalid email and password combination");
            return '<div class="alert alert-warning" role="alert">PDO error ('.$e->getMessage().').</div>';
        }

        try {

            $sql = "insert into user(email, pwd, hash) values(:email, :pwd, :hash)";
            $params = array(
                ":email" => $email,
                ":pwd" => $pwd,
                ":hash" => $hash,
            );
            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);
//            $id = $pdo->lastInsertId();
            return '<div class="alert alert-success" role="alert">Account created. Go to <a href="login.php">login</a> page to start.</div>';

        } catch (Exception $e){
            errorLog($pdo, __LINE__ . ':' . $e->getMessage().':'.$e);
            return '<div class="alert alert-warning" role="alert">PDO error ('.$e->getMessage().').</div>';
        }

    } else {
        errorLog($pdo,__LINE__ . ':' . "Empty inputs");
        return '<div class="alert alert-warning" role="alert">Empty inputs.</div>';
    }

}

/**
 * Reset password
 *
 * @param $pdo
 * @param $email
 * @return string
 */
function forgot_pwd_mail($pdo, $email){

    if(!empty($email))
    {
        $email = filter_var ( (trim($email)), FILTER_SANITIZE_EMAIL );
        if($email){

            $sql = 'select * from user where email = :email limit 1;';
            $params = array(
                ":email" => $email,
            );
            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);

            if ($stmt->rowCount())
            {
                $results = $stmt->fetchAll();

                //recipient
                $to = $email;

                // subject
                $subject = "VCE - Password Reset";
                $link = 'http://cit-project.hopto.org:15000/forgot-pwd.php?token=' . $results[0]['hash'];

                // compose message
                $message = "
                            <html>
                              <head>
                                <title>VCE - Password Reset</title>
                              </head>
                              <body>
                                <h3>VCE - Password Reset</h3>
                                <p>You receiving this email because you requested to reset your curret password.<br/>
                                    Click on the link below in order to continue with the Password Reset process:<br/>
                                   <a href=\"$link\">password reset link</a>
                                   <br/>
                                   <br/>
                                   If the above link doesn't work, copy and paste this link into you browser: <br/>
                                   $link
                                    <br/>
                                    <br/>
                                    Have a nice day,<br/>
                                    VCE Team   
                                </p>
                              </body>
                            </html>
                            ";
                $messageNonHtml = "VCE - Password Reset |\r\n 
                                    You receiving this email because you requested to reset your curret password. |\r\n 
                                    Copy and paste this link into you browser in order to continue with the Password Reset process: |\r\n
                                    $link";

                $sql = 'select * from settings where service = :service;';
                $params = array(
                    ":service" => 'mail',
                );
                $stmt = $pdo->prepare($sql);
                $stmt->execute($params);

                if ($stmt->rowCount())
                {
                    $results = $stmt->fetchAll();
                    $username = null;
                    $password = null;

                    foreach ($results as $key => $value)
                    {
                        if($value['field'] === 'username'){$username = $value['value'];};
                        if($value['field'] === 'password'){$password = $value['value'];};
                    }

                    if(empty($username) || empty($password))
                    {
                        errorLog($pdo, __LINE__ . ':' . 'Error retrieving settings details for mailer from db.');
                        return '<div class="alert alert-warning" role="alert">Sorry, we encountered some errors while performing this operations. Process can\'t be completed at this time.</div>';
                    }
                }
                else
                {
                    errorLog($pdo, __LINE__ . ':' . 'Error retrieving settings details for mailer from db.');
                    return '<div class="alert alert-warning" role="alert">Sorry, we encountered some errors while performing this operations. Process can\'t be completed at this time.</div>';
                }

                // PHPMailer
                $mail = new PHPMailer(true);                              // Passing `true` enables exceptions
                try {
                    //Server settings
                    $mail->SMTPDebug = 0;                                           // Enable verbose debug output
                                                                                    // 0 = off (for production use)
                                                                                    // 1 = client messages
                                                                                    // 2 = client and server messages
                    $mail->isSMTP();                                                // Set mailer to use SMTP
                    $mail->Host = 'smtp.gmail.com';                                 // Specify main and backup SMTP servers
                    $mail->SMTPAuth = true;                                         // Enable SMTP authentication
                    $mail->Username = $username;                                    // SMTP username
                    $mail->Password = $password;                                    // SMTP password
                    $mail->SMTPSecure = 'tls';                                      // Enable TLS encryption, `ssl` also accepted
                    $mail->Port = 587;                                              // TCP port to connect to
                    //Recipients
                    $mail->setFrom('noreply@vce.ie', 'VCE (CIT Group Project)');
                    $mail->addAddress($to);                                         // Add a recipient
                    $mail->addReplyTo('noreply@vce.ie', 'VCE (CIT Group Project)');

                    //Content
                    $mail->isHTML(true);                                    // Set email format to HTML
                    $mail->Subject = $subject;
                    $mail->Body    = $message;
                    $mail->AltBody = $messageNonHtml;

                    $mail->send();
                    return '<div class="alert alert-success" role="alert">An email with further instructions has been sent to provided email address. <br/>Please follow instructions provided in the email.</div>';

                } catch (Exception $e) {
                    errorLog($pdo, __LINE__ . ':' . 'Message could not be sent. Mailer Error: ' . $mail->ErrorInfo . ' |\r\n ' . $e->getMessage().':'.$e);
                    return '<div class="alert alert-warning" role="alert">Sorry, we encountered some errors while performing this operations. Process can\'t be completed at this time.</div>';
                }
            }
            else
            {
                errorLog($pdo,__LINE__ . ':' . "Provided email address doesn't exist");
                return '<div class="alert alert-warning" role="alert">Provided email address doesn\'t exist in our database.</div>';
            }
        }
        else
        {
            errorLog($pdo,__LINE__ . ':' . "Provided information are incorrect");
            return '<div class="alert alert-warning" role="alert">Sorry. Process can\'t be completed at this time.</div>';
        }
    }
    else
    {
        errorLog($pdo,__LINE__ . ':' . "Empty inputs: Some information are missing. Process can't be completed.");
        return '<div class="alert alert-warning" role="alert">Some information are missing. Process can\'t be completed.</div>';
    }
}

/**
 * Reset password
 *
 * @param $pdo
 * @param $email
 * @param $pwd
 * @param $rpwd
 * @param $token
 * @return string
 */
function forgot_pwd($pdo, $email, $pwd, $rpwd, $token){

//    $registerError = false;

    if(!empty($email) && !empty($pwd) && !empty($rpwd) && !empty($token))
    {

        if($pwd != $rpwd)
        {
            errorLog($pdo, __LINE__ . ':' . "Password mismatch");
            return '<div class="alert alert-warning" role="alert">Password mismatch.</div>';
        }

        $email = filter_var ( (trim($email)), FILTER_SANITIZE_EMAIL );
        $pwd = filter_var ( (trim($pwd)), FILTER_SANITIZE_STRING );
        $rpwd = filter_var ( (trim($rpwd)), FILTER_SANITIZE_STRING );
        $token = filter_var ( (trim($token)), FILTER_SANITIZE_STRING );
        $hash = generateRandomString(128);
        $options = [
            'salt' => SALT, //write your own code to generate a suitable salt
            'cost' => 12 // the default cost is 10
        ];

        if($email && $pwd && $rpwd && $token){

            $pwd = password_hash($pwd, PASSWORD_DEFAULT, $options);

            $sql = 'select * from user where email = :email limit 1;';
            $params = array(
                ":email" => $email,
            );
            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);

            if ($stmt->rowCount()) {

                $results = $stmt->fetchAll();
                if($results[0]['hash'] === $token)
                {
                    try {

                        $sql = "update user set pwd = :pwd, hash = :hash where email = :email;";
                        $params = array(
                            ":email" => $email,
                            ":pwd" => $pwd,
                            ":hash" => $hash,
                        );
                        $stmt = $pdo->prepare($sql);
                        $stmt->execute($params);

                        return '<div class="alert alert-success" role="alert">Password updated. Go to <a href="login.php">login</a> page to start.</div>';

                    } catch (Exception $e){
                        errorLog($pdo, __LINE__ . ':' . $e->getMessage().':'.$e);
                        return '<div class="alert alert-warning" role="alert">We encountered some errors while performing this operations ('.$e->getMessage().').</div>';
                    }
                }
                else
                {
                    errorLog($pdo, __LINE__ . ':Process can\'t be completed: token mismatch.');
                    return '<div class="alert alert-warning" role="alert">Sorry, process can\'t be completed at this time.</div>';
                }

            }
            else
            {
                errorLog($pdo, __LINE__ . ':Provided email address doesn\'t exists in our database.');
                return '<div class="alert alert-warning" role="alert">Provided email address doesn\'t exists in our database.</div>';
            }
        } else {
            errorLog($pdo, __LINE__ . ':' . "Empty inputs: Some fields has been left blank. Please correct and try again.");
            return '<div class="alert alert-warning" role="alert">Some fields has been left blank. Please correct and try again.</div>';
        }

    } else {
        errorLog($pdo,__LINE__ . ':' . "Empty inputs: Some information are missing. Process can't be completed.");
        return '<div class="alert alert-warning" role="alert">Some information are missing. Process can\'t be completed.</div>';
    }

}

/**
 * Load data for index page
 *
 * @param $pdo
 * @return array
 */
function index_page($pdo)
{
    $results = array(
        'users' => array('count' => 0, 'details' => array()),
        'devices' => array('count' => 0, 'details' => array()),
        'notifications' => array('count' => 0, 'details' => array()),
        'errors' => array('count' => 0, 'details' => array()),
        'chart' => array(),
    );

    try {

        // fetch user information
        $sql = "select count(*) as 'count' from user;";
        $results['users']['count'] = selectDB($pdo, $sql);
        $sql = "select email as 'email' from user where active = 'Y' order by email;";
        $results['users']['details'] = selectDB($pdo, $sql);

        // fetch device information
        $sql = "select count(*) as 'count' from device;";
        $results['devices']['count'] = selectDB($pdo, $sql);
        $sql = "select * from device order by name;";
        $results['devices']['details'] = selectDB($pdo, $sql);

        //fetch notification information
        $sql = "select count(*) as 'count' from notifications;";
        $results['notifications']['count'] = selectDB($pdo, $sql);
        $sql = "select * from notifications order by date desc;";
        $results['notifications']['details'] = selectDB($pdo, $sql);

        // fetch error information
        $sql = "select count(*) as 'count' from errorlog;";
        $results['errors']['count'] = selectDB($pdo, $sql);
        $sql = "select * from errorlog order by date desc;";
        $results['errors']['details'] = selectDB($pdo, $sql);

        // build bar chart
        $sql = "SELECT
                LEFT(`user`.`email`,LOCATE('@',`user`.`email`) - 1) as 'user',
                    `user`.`email`, 
                    COUNT(`tokens`.`userid`) AS `count`
                FROM
                    `user`
                LEFT JOIN
                    `tokens`
                    ON (`tokens`.`userid`=`user`.`id`)
                GROUP BY
                    `user`.email
                ORDER BY
                    `user`.`email`";
        $results['chart'] = selectDB($pdo, $sql);

        $plot = '';
        foreach($results['chart'] as $key=>$value)
        {
            $plot .= "{ y: '" . $value['user'] . "',  b: " . $value['count'] . " },";
        }
        $chart = "Morris.Bar({
                                element: 'bar-example',
                                data: [
                                    $plot
                                ],
                                xkey: 'y',
                                ykeys: ['b'],
                                labels: ['No. of logins']
                            });";
        $results['chart'] = $chart;

    } catch (PDOException $e){
        errorLog($pdo, __LINE__ . ':' . $e->getMessage().':'.$e);
    }

    return $results;
};

/**
 * Helper functions to fetch latest notifications
 *
 * @param $pdo
 * @param $post
 * @return array
 */
function fetch_notifications($pdo, $post)
{
    if($post['interval']>0)
    {
        $date = $post['date'];
        $sql = "select * from notifications where date > '{$date}' order by date desc;";
        return selectDB($pdo, $sql);
    }
    else
    {
        $sql = "select * from notifications order by date desc limit 10;";
        return selectDB($pdo, $sql);
    }

};

/**
 * Logout
 */
function logout()
{
    setcookie("cit_project_user", "", time() - 3600);
    header("Location: login.php");
};

/**
 * Random string generator
 * @param int $length
 * @return string
 */
function generateRandomString($length = 10) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}

/**
 * Simple helper method to fetch data from DB
 *
 * @param $pdo
 * @param $sql
 * @param array $params
 * @return array
 */
function selectDB($pdo, $sql, $params = array())
{
    $results = array();

    try {
        $stmt = $pdo->prepare($sql);
        $stmt->execute($params);
        if ($stmt->rowCount()) {
            $results = $stmt->fetchAll();
        }
    } catch (PDOException $e){
        errorLog($pdo, __LINE__ . ':' . $e->getMessage().':'.$e);
    }

    return $results;
};

/**
 * Simple helper method to insert data from DB
 *
 * @param $pdo
 * @param $sql
 * @param array $params
 * @return array
 */
function insertDB($pdo, $sql, $params = array())
{
    $results = array();

    try {
        $stmt = $pdo->prepare($sql);
        $results = $stmt->execute($params);
    } catch (PDOException $e){
        errorLog($pdo, __LINE__ . ':' . $e->getMessage().':'.$e);
    }

    return $results;
};
