CREATE DATABASE Giftit;
USE Giftit;
CREATE TABLE Users(userId BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50), password VARCHAR(50),
                   first_name VARCHAR(50), last_name VARCHAR(50), email VARCHAR(50), phone VARCHAR(20), address VARCHAR(80),
                   account DECIMAL, init_date DATE, blocked_until DATE, role VARCHAR(10));
CREATE TABLE Items(userId BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50), type VARCHAR(20),
                   description VARCHAR(255), cost DECIMAL);
CREATE TABLE Questions(userId BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT, request VARCHAR(255),
                       response VARCHAR(255), request_date DATE, response_date DATE,
  FOREIGN KEY (user_id) REFERENCES Users(userId) ON DELETE  CASCADE  ON UPDATE CASCADE);
CREATE TABLE Comments(userId BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT, message VARCHAR(255), date DATE,
  FOREIGN KEY (user_id) REFERENCES Users(userId) ON DELETE  CASCADE  ON UPDATE CASCADE);
CREATE TABLE Carts(userId BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT, item_id BIGINT, count DECIMAL,
  FOREIGN KEY (user_id) REFERENCES Users(userId) ON DELETE  CASCADE  ON UPDATE CASCADE,
  FOREIGN KEY (item_id) REFERENCES Items(userId) ON DELETE  CASCADE  ON UPDATE CASCADE);
CREATE TABLE Orders(userId BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT, cart_id BIGINT,
                    details VARCHAR(255), status VARCHAR(20), init_date DATE, issue_date DATE,
  FOREIGN KEY (user_id) REFERENCES Users(userId) ON DELETE  CASCADE  ON UPDATE CASCADE,
  FOREIGN KEY (cart_id) REFERENCES Carts(userId) ON DELETE  CASCADE  ON UPDATE CASCADE);
INSERT INTO Users(username, password, first_name, last_name, email, phone, address, account, init_date,
                  blocked_until, role)
VALUES ('chmut','1234','Sergei', 'Chmut', 'chmuts@tut.by', '+375296242715','Minsk, Mihanovichi, Magistralnaya 21-8',
  0,'2019-01-10','2019-01-10','ADMIN');