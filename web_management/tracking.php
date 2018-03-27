<?php

    /**
     * Include required functions
     */
    include_once ("config/functions.php");


    // open PDO connection
    $pdo = conn();

    // authenticate user
    auth($pdo);

    $data = tracking_page($pdo);
//    var_dump($data);die();

    /**
     * Include page head
     */
    include_once ("config/head.php");

?>

<!-- Page main content -->
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Live Tracking</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">

        <div class="alert alert-danger" id="error" style="display: none" role="alert">Error occurred while fetching coordinates.</div>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Device</h3>
            </div>
            <div class="panel-body">
                <select id="device-select">
                    <option value="" selected>--select--</option>
                    <?php

                        foreach($data as $device){
                            echo '<option value="'.$device['device'].'">'.$device['device'].'</option>';
                        }

                    ?>
                </select>
            </div>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title" style="display: inline-block;">Live Position</h3>
                <span id="date-holder" style="display: none; float: right;">Updated: <span id="date"></span></span>
            </div>
            <div class="panel-body">

                <p id="loading-map" style="display: none;">Loading...</p>
                <p id="invalid-coords" style="display: none;">Invalid coordinates</p>
                <div id="map" style="width:400px;height:400px">

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
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDL34dnop8334R82xg7j7o5QpdEph65Drg&callback=myMap"></script>

<script>

    var myLatLng = {lat: 53.350140, lng: -6.266155};

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 5,
        center: myLatLng
    });

    var marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        title: 'Live Tracking Map!'
    });

    // var device = $('#device-select').find(":selected").text();

    var tid = setInterval(function () {

    }, 1000);

    $('#device-select').on('change', function() {

        clearInterval(tid);
        $('#map').hide();
        $('#loading-map').show();
        $('#date-holder').hide();

        var device = $('#device-select').find(":selected").text();

        // interval method in order to refresh notifications div;
        // every 3 seconds call is made to the server to fetch latest notifications
            tid = setInterval(function(){
            $.ajax({
                url: 'fetch-position.php',
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

                    if(data.length>0)
                    {
                        $('#error').hide();
                        $('#map').show();
                        $('#loading-map').hide();
                        $('#invalid-coords').hide();
                        $('#date-holder').show();
                        $('#date').html(data[0]['date']);

                        var latitude = parseFloat(data[0]['latitude']);
                        var longitude = parseFloat(data[0]['longitude']);

                        if((!isNaN(latitude) && latitude!="" && latitude!=null && latitude!=undefined && latitude!='undefined') &&
                            (!isNaN(longitude) && longitude!="" && longitude!=null && longitude!=undefined && longitude!='undefined'))
                        {
                            var myLatLng = {lat: latitude, lng: longitude};

                            var map = new google.maps.Map(document.getElementById('map'), {
                                zoom: 15,
                                center: myLatLng
                            });

                            var marker = new google.maps.Marker({
                                position: myLatLng,
                                map: map,
                                title: 'Live Tracking Map!'
                            });
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