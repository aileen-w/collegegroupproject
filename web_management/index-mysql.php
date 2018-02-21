<?php


$host = 'mysql';
$db   = 'cit';
$user = 'jmc';
$pass = 'diehard2018';
$charset = 'utf8mb4';

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$opt = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];
$pdo = new PDO($dsn, $user, $pass, $opt);

//phpinfo();

$active = 'Y';
$email = 'mycit@gmail.com';
$params = array('email' => $email, 'active' => $active);

$stmt = $pdo->prepare('SELECT * FROM user WHERE email = :email AND active=:active');
$stmt->execute($params);
$user = $stmt->fetchAll();

foreach ($user as $row)
{
    echo "email : " . $row['email'] . "\n";
    echo "pwd : " . $row['pwd'] . "\n";
    echo "token : " . $row['token'] . "\n";
    echo "active : " . $row['active'] . "\n";
}