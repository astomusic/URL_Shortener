jdbc setting in Logic.java
```
String url = "jdbc:mysql://localhost/url_shortener";
Class.forName("com.mysql.jdbc.Driver");
Connection conn = DriverManager.getConnection(url, "urlshortener", "db1004");
```

mysql setting in local
```
create database url_shortener;
use url_shortener;

create user 'urlshortener'@'localhost' identified by 'db1004';
GRANT ALL ON url_shortener.* to 'urlshortener'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

CREATE DATABASE `url_shortener`
 
CREATE TABLE `url_data` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `long_url` text NOT NULL,
  PRIMARY KEY (`id`)
);
```