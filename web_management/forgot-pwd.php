<?php

    include_once ("config/functions.php");

    $pdo = conn();

    $forgot_pwd = null;

    if(!empty($_POST['submit']))
    {
        if(!empty($_POST['email']) && !empty($_POST['pwd']) && !empty($_POST['rpwd']) && !empty($_POST['token']))
        {
            $forgot_pwd = forgot_pwd($pdo, $_POST['email'], $_POST['pwd'], $_POST['rpwd'], $_POST['token']);
        }
        else
        {
            $forgot_pwd = '<div class="alert alert-warning" role="alert">Some information are missing. Sorry, process can\'t be continued.</div>';
        }
    }

?>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>VCE - CIT Group Project</title>

    <!-- Bootstrap Core CSS -->
    <link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="../vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="../dist/css/sb-admin-2.css" rel="stylesheet">
    <link href="../css/cit-group-project.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="../vendor/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="../vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

<div class="login-wrapper">

    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.php">VCE</a>
        </div>
        <!-- /.navbar-header -->
    </nav>

    <!--<div id="page-wrapper">-->

    <div class="container">
        <div class="row">

            <div id="register_feedback">
                <?php echo $forgot_pwd; ?>
            </div>
            <div style="display: none" id="wrong_email" class="alert alert-warning" role="alert">Provided email address is invalid.</div>
            <div style="display: none" id="wrong_rpwd" class="alert alert-warning" role="alert">Password provided and password re-typed dont match.</div>
            <div style="display: none" id="wrong_pwd" class="alert alert-warning" role="alert">Password must meet requirements provided.</div>


            <div class="col-sm-6 col-md-4 col-md-offset-4">
                <h1 class="text-center login-title">Password Reset</h1>
                <div class="account-wall">
                    <img class="profile-img" src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=120"
                         alt="">
                    <form class="form-signin" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>">
                        <input id="email" name="email" type="text" class="form-control" placeholder="Email" required autofocus>
                        <input id="register-pwd" name="pwd" type="password" class="form-control middle-input" placeholder="Password" required>
                        <input id="register-rpwd" name="rpwd" type="password" class="form-control" placeholder="Repeat Password" required>
                        <input style="display: none" id="token" name="token" type="text" value="<?=$_GET['token'];?>">
<!--                        <button class="btn btn-lg btn-primary btn-block" type="submit">Register</button>-->
                        <input id="reg" class="btn btn-lg btn-primary btn-block" type="submit" name="submit" value="Submit"/>

                    </form>
                </div>
            </div>

            <div class="col-md-4">
                <div class="aro-pswd_info">
                    <div id="pswd_info">
                        <h4>Password must be requirements</h4>
                        <ul>
                            <li id="letter" class="invalid">At least <strong>one letter</strong></li>
                            <li id="capital" class="invalid">At least <strong>one capital letter</strong></li>
                            <li id="number" class="invalid">At least <strong>one number</strong></li>
                            <li id="length" class="invalid">Be at least <strong>8 characters</strong></li>
                            <li id="space" class="invalid">be<strong> use [~,!,@,#,$,%,^,&,*,-,=,.,;,']</strong></li>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="aro-pswd_info_2">
                    <div id="pswd_info_2">
                        <h4>Password must be requirements</h4>
                        <ul>
                            <li id="letter_2" class="invalid">At least <strong>one letter</strong></li>
                            <li id="capital_2" class="invalid">At least <strong>one capital letter</strong></li>
                            <li id="number_2" class="invalid">At least <strong>one number</strong></li>
                            <li id="length_2" class="invalid">Be at least <strong>8 characters</strong></li>
                            <li id="space_2" class="invalid">be<strong> use [~,!,@,#,$,%,^,&,*,-,=,.,;,']</strong></li>
                        </ul>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <!--</div>-->
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<!-- jQuery -->
<script src="../vendor/jquery/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="../vendor/bootstrap/js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="../vendor/metisMenu/metisMenu.min.js"></script>

<!-- Morris Charts JavaScript -->
<script src="../vendor/raphael/raphael.min.js"></script>
<script src="../vendor/morrisjs/morris.min.js"></script>
<script src="../data/morris-data.js"></script>

<!-- Custom Theme JavaScript -->
<script src="../dist/js/sb-admin-2.js"></script>
<script src="../js/cit-group-project.js"></script>

</body>

</html>

<?php

closeDB($pdo);

?>