<?php

/**
 * API
 *
 * Below switch statement is responsible for recognizing what service user is calling with API.
 * Depends on the selection, correct action is undertaken.
 * This can be easily altered and extended if it comes to new functionality and additional services.
 *
 * Request method: API accepts only POST requests.
 * Data: API accepts only JSON format.
 * Variables: 3 variables must be passed: svc (service), msg (message), dev (device)
 * Respond: API return JSON array with "status" and "msg" index
 * URL: http://cit-project.hopto.org:15000/api.php
 *
 */

include_once ("config/functions.php");

    // mysql connection
    $pdo = conn();

    // default return array
    $result = array("status" => "Ok", "msg" => "");

    if($_SERVER['REQUEST_METHOD'] === 'POST')
    {
        // variables
        $post = json_decode(file_get_contents('php://input'), true);
        $svc = !empty($post['svc']) ? $post['svc'] : null;
        $msg = !empty($post['msg']) ? $post['msg'] : null;
        $dev = !empty($post['dev']) ? $post['dev'] : null;

        if(!empty($svc) && !empty($msg) && !empty($dev))
        {
            switch ($svc) {
                // insert notification received from android app into DB
                case "notification":
                    $sql = "INSERT INTO notifications(device, date, message) VALUES(:device, NOW(), :message);";
                    $params = array("device" => $dev, "message" => $msg);
                    $result = insertDB($pdo, $sql, $params);
                    break;
                // insert geolocation information to DB
                case "geolocation":
                    $geolocation = json_decode($msg, true);
                    $latitude = $geolocation['latitude'];
                    $longitude = $geolocation['longitude'];
                    $sql = "INSERT INTO geolocation(device, date, latitude, longitude) VALUES(:device, NOW(), :latitude, :longitude);";
                    $params = array("device" => $dev, "latitude" => $latitude, "longitude" => $longitude);
                    $result = insertDB($pdo, $sql, $params);
                    break;
                // insert calendar information to DB
                case "calendar":
                    $cal = json_decode($msg, true);
                    $user = $cal['user'];
                    $title = $cal['title'];
                    $desc = $cal['desc'];
                    $date = $cal['date'];
                    $t_from = $cal['t_from'];
                    $t_to = $cal['t_to'];
                    $active = !empty($cal['active']) ? $cal['active'] : null;
                    $id = !empty($cal['id']) ? $cal['id'] : null;
                    $action = $cal['action'];
                    if(!empty($action)){
                        if($action === 'new'){

                            if( empty($title) ||
                                empty($desc) ||
                                empty($date) ||
                                empty($t_from) ||
                                empty($t_from) ||
                                empty($user)
                            ){
                                errorLog($pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "INSERT  INTO calendar(user_device, title, cal_desc, cal_date, time_from, time_to) 
                                            VALUES(:user, :title, :desc, :date, :t_from, :t_to);";
                            $params = array(
                                "user" => $user,
                                "title" => $title,
                                "desc" => $desc,
                                "date" => $date,
                                "t_from" => $t_from,
                                "t_to" => $t_to
                            );
                            $result = insertDB($pdo, $sql, $params);
                        }
                        if($action === 'delete'){

                            if(empty($id)){
                                errorLog($pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "UPDATE calendar SET active = 'N' WHERE id = :id;";
                            $params = array(
                                "id" => $id
                            );
                            $result = insertDB($pdo, $sql, $params);
                        }
                        if($action === 'update'){

                            if( empty($title) ||
                                empty($desc) ||
                                empty($date) ||
                                empty($t_from) ||
                                empty($t_from) ||
                                empty($id)
                            ){
                                errorLog($pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "UPDATE calendar 
                                    SET title = :title,
                                        cal_desc = :desc,
                                        cal_date = :date,
                                        time_from = :t_from,
                                        time_to = :t_to
                                    WHERE id = :id;";
                            $params = array(
                                "title" => $title,
                                "desc" => $desc,
                                "date" => $date,
                                "t_from" => $t_from,
                                "t_to" => $t_to,
                                "id" => $id
                            );
                            $result = insertDB($pdo, $sql, $params);
                        }
                        if($action === 'view'){

                            if( empty($user) ||
                                empty($date)
                            ){errorLog(
                                $pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "SELECT * FROM calendar
                                    WHERE user_device = :user 
                                    AND cal_date = :date 
                                    AND active = 'Y'
                                    ORDER BY cal_date, time_from;";
                            $params = array(
                                "user" => $user,
                                "date" => $date
                            );
                            $result = selectDB($pdo, $sql, $params);
                        }
                        if($action === 'viewOne'){

                            if(empty($id)){
                                errorLog($pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "SELECT * FROM calendar
                                    WHERE id = :id;";
                            $params = array(
                                "id" => $id
                            );
                            $result = selectDB($pdo, $sql, $params);
                        }
                        if($action === 'next'){

                            if( empty($user) ||
                                empty($id)
                            ){errorLog(
                                $pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "SELECT * FROM calendar
                                    WHERE user_device = :user 
                                    AND id = (select min(id) from calendar where user_device = :user and id > :id) 
                                    AND active = 'Y'
                                    LIMIT 1;";
                            $params = array(
                                "user" => $user,
                                "id" => $id
                            );
                            $result = selectDB($pdo, $sql, $params);
                        }
                        if($action === 'prev'){

                            if( empty($user) ||
                                empty($id)
                            ){errorLog(
                                $pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "SELECT * FROM calendar
                                    WHERE user_device = :user 
                                    AND id = (select max(id) from calendar where user_device = :user and id < :id) 
                                    AND active = 'Y'
                                    LIMIT 1;";
                            $params = array(
                                "user" => $user,
                                "id" => $id
                            );
                            $result = selectDB($pdo, $sql, $params);
                        }

                        if($result){
                            $result = array("status" => "Ok", "msg" => $result);
                        }
                    }
                    break;
                // insert error received from android app into DB
                case "error":
                    $sql = "INSERT INTO errorlog(date, message) VALUES(NOW(), :message);";
                    $params = array("message" => $msg);
                    $result = insertDB($pdo, $sql, $params);
                    break;
                // default action taken when non of the above services will be requested
                default:
                    errorLog($pdo, "API:Unknown service requested.");
                    $result = array("status" => "Error", "msg" => "Unknown service requested.");
            }
        }
        else
        {
            errorLog($pdo, "API:Values like: svc, msg, dev cannot be empty.");
            $result = array("status" => "Error", "msg" => "Values like: svc, msg, dev cannot be empty.");
        }

    }
    else if($_SERVER['REQUEST_METHOD'] === 'GET')
    {
        // variables
        if(!empty($_GET)){
            $counter = 0;
            foreach ($_GET as $key=>$value) {

                if ($counter === 1) {
                    $post = json_decode($key);
                }

                $counter++;
            }
        }
        $svc = !empty($post->svc) ? $post->svc : null;
        $msg = !empty($post->msg) ? $post->msg : null;
        $dev = !empty($post->dev) ? $post->dev : null;

        if(!empty($svc) && !empty($msg) && !empty($dev))
        {
            switch ($svc) {
                // insert calendar information to DB
                case "calendar":
                    $cal = json_decode($msg, true);
                    $user = $cal['user'];
                    $title = $cal['title'];
                    $desc = $cal['desc'];
                    $date = $cal['date'];
                    $t_from = $cal['t_from'];
                    $t_to = $cal['t_to'];
                    $active = !empty($cal['active']) ? $cal['active'] : null;
                    $id = !empty($cal['id']) ? $cal['id'] : null;
                    $action = $cal['action'];
                    if(!empty($action)){
                        if($action === 'new'){

                            if( empty($title) ||
                                empty($desc) ||
                                empty($date) ||
                                empty($t_from) ||
                                empty($t_from) ||
                                empty($user)
                            ){
                                errorLog($pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "INSERT  INTO calendar(user_device, title, cal_desc, cal_date, time_from, time_to) 
                                            VALUES(:user, :title, :desc, :date, :t_from, :t_to);";
                            $params = array(
                                "user" => $user,
                                "title" => $title,
                                "desc" => $desc,
                                "date" => $date,
                                "t_from" => $t_from,
                                "t_to" => $t_to
                            );
                            $result = insertDB($pdo, $sql, $params);
                        }
                        if($action === 'delete'){

                            if(empty($id)){
                                errorLog($pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "UPDATE calendar SET active = 'N' WHERE id = :id;";
                            $params = array(
                                "id" => $id
                            );
                            $result = insertDB($pdo, $sql, $params);
                            if($result){
                                echo $_GET['callback'] . '(' . "{'status' : 'Ok', 'msg':'$result'}" . ')';
                                die();
                            }
                        }
                        if($action === 'update'){

                            if( empty($title) ||
                                empty($desc) ||
                                empty($date) ||
                                empty($t_from) ||
                                empty($t_from) ||
                                empty($id)
                            ){
                                errorLog($pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "UPDATE calendar 
                                    SET title = :title,
                                        cal_desc = :desc,
                                        cal_date = :date,
                                        time_from = :t_from,
                                        time_to = :t_to
                                    WHERE id = :id;";
                            $params = array(
                                "title" => $title,
                                "desc" => $desc,
                                "date" => $date,
                                "t_from" => $t_from,
                                "t_to" => $t_to,
                                "id" => $id
                            );
                            $result = insertDB($pdo, $sql, $params);
                        }
                        if($action === 'view'){

                            if( empty($user) ||
                                empty($date)
                            ){errorLog(
                                $pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "SELECT * FROM calendar
                                    WHERE user_device = :user 
                                    AND cal_date = :date 
                                    AND active = 'Y'
                                    ORDER BY cal_date, time_from;";
                            $params = array(
                                "user" => $user,
                                "date" => $date
                            );
                            $result = selectDB($pdo, $sql, $params);
                        }
                        if($action === 'viewOne'){

                            if(empty($id)){
                                errorLog($pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "SELECT * FROM calendar
                                    WHERE id = :id;";
                            $params = array(
                                "id" => $id
                            );
                            $result = selectDB($pdo, $sql, $params);
                        }
                        if($action === 'next'){

                            if( empty($user) ||
                                empty($id)
                            ){errorLog(
                                $pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "SELECT * FROM calendar
                                    WHERE user_device = :user 
                                    AND id = (select min(id) from calendar where user_device = :user and id > :id) 
                                    AND active = 'Y'
                                    LIMIT 1;";
                            $params = array(
                                "user" => $user,
                                "id" => $id
                            );
                            $result = selectDB($pdo, $sql, $params);
                        }
                        if($action === 'prev'){

                            if( empty($user) ||
                                empty($id)
                            ){errorLog(
                                $pdo, "API:Calendar, empty value passed (line: " . __LINE__ . ")");
                                $result = array("status" => "Error", "msg" => "Calendar, found empties.");
                            };

                            $sql = "SELECT * FROM calendar
                                    WHERE user_device = :user 
                                    AND id = (select max(id) from calendar where user_device = :user and id < :id) 
                                    AND active = 'Y'
                                    LIMIT 1;";
                            $params = array(
                                "user" => $user,
                                "id" => $id
                            );
                            $result = selectDB($pdo, $sql, $params);
                        }

//                        if($result){
//                            $result = array("status" => "Ok", "msg" => $result);
//                            var_dump($result);die();
                            $result = json_encode($result);
                            echo $_GET['callback'] . '(' . "{'status' : 'Ok', 'msg':'$result'}" . ')';
                            die();
//                        }
                    }
                    break;
                // default action taken when non of the above services will be requested
                default:
                    errorLog($pdo, "API:Unknown service requested.");
//                    $result = array("status" => "Error", "msg" => "Unknown service requested.");
                    echo $_GET['callback'] . '(' . "{'status' : 'Error', 'msg':'Unknown service requested.'}" . ')';
                    die();
            }
        }
        else
        {
            errorLog($pdo, "API:Values like: svc, msg, dev cannot be empty.");
//            $result = array("status" => "Error", "msg" => "Values like: svc, msg, dev cannot be empty.");
            echo $_GET['callback'] . '(' . "{'status' : 'Error', 'msg':'Values like: svc, msg, dev cannot be empty.'}" . ')';
            die();
        }

    }
    else
    {
        errorLog($pdo, "API:Not supported HTTP request.");
        $result = array("status" => "Error", "msg" => "Not supported HTTP request.");
    }

    // return json array
    echo json_encode($result);

    // close connection
    closeDB($pdo);

?>