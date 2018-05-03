<?php

include_once ("config/functions.php");

    $pdo = conn();

    $data = fetch_calendar_records($pdo, $_POST);

    echo json_encode($data);