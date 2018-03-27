<?php

    /**
     * Include required functions
     */
    include_once ("config/functions.php");


    // open PDO connection
    $pdo = conn();

    // authenticate user
    auth($pdo);

    $data = index_page($pdo);

    /**
     * Include page head
     */
    include_once ("config/head.php");

?>

<!-- Page main content -->
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Dashboard</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-3 col-md-6">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-xs-3">
                            <i class="fa fa-user fa-5x"></i>
                        </div>
                        <div class="col-xs-9 text-right">
                            <div class="huge"><?= $data['users']['count'][0]['count'];?></div>
                            <div>Users</div>
                        </div>
                    </div>
                </div>
                <a href="details-dashboard.php?el=users">
                    <div class="panel-footer">
                        <span class="pull-left">View Details</span>
                        <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                        <div class="clearfix"></div>
                    </div>
                </a>
            </div>
        </div>
        <div class="col-lg-3 col-md-6">
            <div class="panel panel-green">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-xs-3">
                            <i class="fa fa-mobile fa-5x"></i>
                        </div>
                        <div class="col-xs-9 text-right">
                            <div class="huge"><?= $data['devices']['count'][0]['count'];?></div>
                            <div>Devices</div>
                        </div>
                    </div>
                </div>
                <a href="details-dashboard.php?el=devices">
                    <div class="panel-footer">
                        <span class="pull-left">View Details</span>
                        <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                        <div class="clearfix"></div>
                    </div>
                </a>
            </div>
        </div>
        <div class="col-lg-3 col-md-6">
            <div class="panel panel-yellow">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-xs-3">
                            <i class="fa fa-comment fa-5x"></i>
                        </div>
                        <div class="col-xs-9 text-right">
                            <div class="huge" id="notification_count"><span><?= $data['notifications']['count'][0]['count'];?></span></div>
                            <div>Notifications</div>
                        </div>
                    </div>
                </div>
                <a href="details-dashboard.php?el=notifications">
                    <div class="panel-footer">
                        <span class="pull-left">View Details</span>
                        <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                        <div class="clearfix"></div>
                    </div>
                </a>
            </div>
        </div>
        <div class="col-lg-3 col-md-6">
            <div class="panel panel-red">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-xs-3">
                            <i class="fa fa-exclamation-circle fa-5x"></i>
                        </div>
                        <div class="col-xs-9 text-right">
                            <div class="huge"><?= $data['errors']['count'][0]['count'];?></div>
                            <div>Errors</div>
                        </div>
                    </div>
                </div>
                <a href="details-dashboard.php?el=errors">
                    <div class="panel-footer">
                        <span class="pull-left">View Details</span>
                        <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                        <div class="clearfix"></div>
                    </div>
                </a>
            </div>
        </div>
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-8">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i> Management Login Activity
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body" style="height: 400px;">
                    <div id="bar-example" ></div>
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->

        </div>
        <!-- /.col-lg-8 -->
        <div class="col-lg-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bell fa-fw"></i> Notifications Panel
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body" style="height: 400px;">
                    <div class="list-group" id="notifications" style="height: 320px; overflow: auto;">
                        <p id="notifications_loading" style="text-align: center; padding-top: 140px;">Loading...</p>
                    </div>
                    <!-- /.list-group -->
                    <a href="#" class="btn btn-default btn-block">View All Notifications</a>
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div>
        <!-- /.col-lg-4 -->
    </div>
    <!-- /.row -->
</div>
<!-- /#page-wrapper -->


<?php
/**
 * Include page footer
 */
include_once ("config/footer.php");

?>

<script>

    // interval method in order to refresh notifications div;
    // every 3 seconds call is made to the server to fetch latest notifications
    var interval = 0;
    var date = 0;
    setInterval(function(){
        $.ajax({
            url: 'fetch-notifications.php',
            data: {
                interval: interval,
                date: date
            },
            error: function(e, jqXHR, textStatus) {
                // console.log('error: ', e);
                // console.log('jqXHR: ', jqXHR);
                // console.log('textStatus: ', textStatus);
                $('#notifications').html('<p>An error has occurred</p>');
            },
            dataType: 'json',
            success: function(data) {

                if(data['notifications'].length>0)
                {
                    $('#notifications_loading').hide();
                    date = data['notifications'][0]['date'];
                    var el = '';
                    for(var i=0; i<data['notifications'].length; i++){
                        el += '<a href="#" class="list-group-item">\n' +
                            '<i class="fa fa-comment fa-fw"></i> '+data['notifications'][i]['device']+'<br/>'+data['notifications'][i]['message']+'\n' +
                            '<span class="pull-right text-muted small"><em>'+data['notifications'][i]['date']+'</em>\n' +
                            '</span>\n' +
                            '</a>'
                    }
                    $('#notifications').prepend( el );
                    var str = String(data['count'][0]['count']);
                    $('#notification_count span').html( str );
                }
            },
            type: 'POST'
        });
        interval = 1;
    }, 3000);


    // echo php string generated by functions file;
    // string contains definition for bar chart inserted into div id: bar-example
    <?= $data['chart']; ?>

</script>