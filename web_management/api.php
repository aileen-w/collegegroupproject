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