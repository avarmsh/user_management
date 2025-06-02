create database user_management;
use user_management;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    age INT,
    email VARCHAR(150) UNIQUE,
    password VARCHAR(255)
);
DESCRIBE users;
