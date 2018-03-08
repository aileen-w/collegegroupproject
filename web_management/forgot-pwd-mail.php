<?php

    include_once ("config/functions.php");

    $pdo = conn();

    $forgot_pwd_mail = null;

    if(!empty($_POST['submit']))
    {
        if(!empty($_POST['email']))
        {
            $forgot_pwd_mail = forgot_pwd_mail($pdo, $_POST['email']);
        }
        else
        {
            $forgot_pwd_mail = '<div class="alert alert-warning" role="alert">Some inputs has been left empty. Please correct and try again.</div>';
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

            <?php echo $forgot_pwd_mail; ?>

            <div class="col-sm-6 col-md-4 col-md-offset-4">
                <h1 class="text-center login-title">Password Reset</h1>
                <div class="account-wall">
                    <img class="profile-img" src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=120" alt="">
                    <p class="text-center">Please provide email address associated with the account. <br/>An email with further instructions will be sent to that emial address.</p>
                    <form class="form-signin" method="post" action="">
                        <input style="margin-bottom: 10px; border-radius: 5px;" id="email" name="email" type="text" class="form-control" placeholder="Email" required autofocus>
                        <input id="reg" class="btn btn-lg btn-primary btn-block" type="submit" name="submit" value="Send"/>

                    </form>
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