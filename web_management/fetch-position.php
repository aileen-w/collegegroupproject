<?php

include_once ("config/functions.php");

    $pdo = conn();

    $data = fetch_position($pdo, $_POST);

    echo json_encode($data);