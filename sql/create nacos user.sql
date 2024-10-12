CREATE USER 'nacos'@'localhost' IDENTIFIED BY 'nacos';
GRANT ALL PRIVILEGES ON letao_config.* TO 'nacos'@'localhost';
FLUSH PRIVILEGES;