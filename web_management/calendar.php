<?php

    /**
     * Include required functions
     */
    include_once ("config/functions.php");


    // open PDO connection
    $pdo = conn();

    // authenticate user
    auth($pdo);

    $data = calendar_page($pdo);

    /**
     * Include page head
     */
    include_once ("config/head.php");

?>

<!-- Page main content -->
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Calendar Records</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">

        <div class="alert alert-danger" id="error" style="display: none" role="alert">Error occurred while fetching data.</div>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Device</h3>
            </div>
            <div class="panel-body">
                <select id="device-select">
                    <option value="" selected>--select--</option>
                    <?php

                        foreach($data as $device){
                            echo '<option value="'.$device['user_device'].'">'.$device['user_device'].'</option>';
                        }

                    ?>
                </select>
            </div>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title" style="display: inline-block;">Records</h3>
<!--                <span id="date-holder" style="display: none; float: right;">Updated: <span id="date"></span></span>-->
            </div>
            <div class="panel-body">

                <p id="loading-map" style="display: none;">Loading...</p>
                <p id="invalid-coords" style="display: none;">Error occurred.</p>
                <div id="map" style="width:100%;height:100%">

            </div>
        </div>

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

<!--<script src="https://maps.googleapis.com/maps/api/js?callback=myMap"></script>-->
<!--<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDL34dnop8334R82xg7j7o5QpdEph65Drg&callback=myMap"></script>-->

<script>

    var tid = setInterval(function () {

    }, 1000);



    var table_head = '<table class="table table-hover table-bordered table-condensed">\n' +
        '        <thead>\n' +
        '        <tr>\n' +
        '        <th>Date</th>\n' +
        '    <th>From</th>\n' +
        '    <th>To</th>\n' +
        '    <th>Title</th>\n' +
        '    <th>Description</th>\n' +
        '    </tr>\n' +
        '    </thead>\n' +
        '    <tbody>';
    var table_footer = '</tbody>\n' +
        '    </table>';

    $('#device-select').on('change', function() {

        clearInterval(tid);
        $('#map').hide();
        $('#loading-map').show();
        // $('#date-holder').hide();

        var device = $('#device-select').find(":selected").text();

        // interval method in order to refresh notifications div;
        // every 3 seconds call is made to the server to fetch latest notifications
            tid = setInterval(function(){

                var table = '';
                var table_body = '';

                $.ajax({
                    url: 'fetch-calendar-records.php',
                    data: {
                        device: device,
                    },
                    error: function(e, jqXHR, textStatus) {
                        // console.log('error: ', e);
                        // console.log('jqXHR: ', jqXHR);
                        // console.log('textStatus: ', textStatus);
                        $('#error').show();
                    },
                    dataType: 'json',
                    success: function(data) {

                        var temp = '';

                        if(data.length>0)
                        {
                            $('#error').hide();
                            $('#map').show();
                            $('#loading-map').hide();
                            $('#invalid-coords').hide();
                            // $('#date-holder').show();
                            // $('#date').html(data[0]['date']);

                            if(data.length>0)
                            {
                                // console.log(data);
                                for(var i=0; i<data.length; i++){
                                    temp = '<tr>\n' +
                                        '    <td>'+data[i]["cal_date"]+'</td>\n' +
                                        '    <td>'+data[i]["time_from"]+'</td>\n' +
                                        '    <td>'+data[i]["time_to"]+'</td>\n' +
                                        '    <td>'+data[i]["title"]+'</td>\n' +
                                        '    <td>'+data[i]["cal_desc"]+'</td>\n' +
                                        '    </tr>'
                                    table_body = table_body + temp;
                                }
                                table = table_head + table_body + table_footer;
                                $('#map').html(table);
                            }
                            else
                            {
                                $('#invalid-coords').show();
                                $('#map').hide();
                                $('#date-holder').hide();
                            }


                        }

                    },
                    type: 'POST'
                });
        }, 3000);
    });

</script>