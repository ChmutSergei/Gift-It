CREATE DATABASE Giftit;
USE Giftit;
CREATE TABLE Bitmaps(criteria_key VARCHAR(20) PRIMARY KEY, data BLOB);
CREATE TABLE Users(id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50), password VARCHAR(60),
                   first_name VARCHAR(50), last_name VARCHAR(50), email VARCHAR(50), phone VARCHAR(20), address VARCHAR(80),
                   account DECIMAL(10,2), init_date DATE, blocked_until DATE, role VARCHAR(10));
CREATE TABLE Items(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50), type VARCHAR(20),
                   description VARCHAR(255), active BOOLEAN, cost DECIMAL(10,2), image MEDIUMBLOB);
CREATE TABLE Questions(id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT, request VARCHAR(255),
                       response VARCHAR(255), request_date DATE, response_date DATE,
  FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE  CASCADE  ON UPDATE CASCADE);
CREATE TABLE Comments(id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT, item_id BIGINT,
                      message VARCHAR(255), date DATE, status VARCHAR(8), FOREIGN KEY (user_id) REFERENCES Users(id),
  FOREIGN KEY (item_id) REFERENCES Items(id) ON DELETE  CASCADE  ON UPDATE CASCADE);
CREATE TABLE Orders(id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT,
                    details VARCHAR(255), status VARCHAR(20), init_date DATE, issue_date DATE,
  FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE  CASCADE  ON UPDATE CASCADE);
CREATE TABLE Carts(id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT, item_id BIGINT, order_id BIGINT, count DECIMAL,
  FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE  CASCADE  ON UPDATE CASCADE,
  FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE  CASCADE  ON UPDATE CASCADE,
  FOREIGN KEY (item_id) REFERENCES Items(id) ON DELETE  CASCADE  ON UPDATE CASCADE);
INSERT INTO Users(username, password, first_name, last_name, email, phone, address, account, init_date,
                  blocked_until, role)
VALUES ('admin','$2a$10$L90aFEyIXHLFEQv0.QWo9.rzePZWi6g8TW4MhhVLCFhEkQHuXl.Hi','Sergei', 'Chmut', 'chmut@tut.by',
 '+375296242715','Minsk, Mihanovichi, Magistralnaya 21-8', 0,'2019-01-10','2019-01-10','ADMIN');
 INSERT INTO Users(username, password, first_name, last_name, email, phone, address, account, init_date,
                  blocked_until, role)
VALUES ('des','$2a$10$L90aFEyIXHLFEQv0.QWo9.rzePZWi6g8TW4MhhVLCFhEkQHuXl.Hi','Jane', 'Ivanova', 'ivanova@tut.by',
 '+375296663322','Минск, Плеханова 31-2', 0,'2019-01-10','2019-01-10','DESIGNER');
 INSERT INTO Users(username, password, first_name, last_name, email, phone, address, account, init_date,
                  blocked_until, role)
VALUES ('moder','$2a$10$L90aFEyIXHLFEQv0.QWo9.rzePZWi6g8TW4MhhVLCFhEkQHuXl.Hi','Ivan', 'Ivanov', 'ivanov@tut.by',
 '+375293224455','Минск, Плеханова 3-15', 0,'2019-01-10','2019-01-10','MODERATOR');
 INSERT INTO Users(username, password, first_name, last_name, email, phone, address, account, init_date,
                  blocked_until, role)
VALUES ('user1','$2a$10$L90aFEyIXHLFEQv0.QWo9.rzePZWi6g8TW4MhhVLCFhEkQHuXl.Hi','Alex', 'Kotov', 'kotov@tut.by',
 '+375293224455','Минск, Плеханова 6-32', 0,'2019-01-10','2019-01-10','USER');
 INSERT INTO Users(username, password, first_name, last_name, email, phone, address, account, init_date,
                  blocked_until, role)
VALUES ('user2','$2a$10$L90aFEyIXHLFEQv0.QWo9.rzePZWi6g8TW4MhhVLCFhEkQHuXl.Hi','Sasha', 'Voinov', 'voinov@tut.by',
 '+375293224455','Минск, Жуковского 1-32', 0,'2019-01-10','2019-01-10','USER');
 INSERT INTO Users(username, password, first_name, last_name, email, phone, address, account, init_date,
                  blocked_until, role)
VALUES ('user3','$2a$10$L90aFEyIXHLFEQv0.QWo9.rzePZWi6g8TW4MhhVLCFhEkQHuXl.Hi','Николай', 'Пряников', 'pryanikov@tut.by',
 '+375293224455','Минск, Независимости 8-34', 0,'2019-01-10','2019-01-10','USER');
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Подушка Любимой маме','pillow','Удобная, яркая подушка - в подарок Любимой маме.',true, 40);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Кружка Любовь','cup','Симпатичная кружка на тематику любви',true, 35);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Тарелка с фото','plate','У нас печать на тарелки Можно мыть Дизайнер поможет с дизайном',true, 35);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Кружка с Вашим фото','cup','Белая кружка (сорт премиум) с фото на заказ',true, 7.9);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Кружка цветная','cup','Кружка цветная внутри с цветной ручкой',true, 8.9);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Кружка хамелеон с фото','cup','Волшебные кружки, которые меняют цвет от горячей воды.',true, 14.4);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Кружка золотая','cup','Печать фото на золотую кружку, картинки и надписи.',true, 13.9);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Черная кружка с фото','cup','Оригинальные кружки чёрного цвета для ваших фото.',true, 15);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Кружка латте конусная','cup','Оригинальная кружка. Печать вашей картинки на заказ.',true, 13.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Кружка с фото-серебро','cup','Печать фото на кружку серебро, картинки и надписи.',true, 12.2);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Детская кружка с фото','cup','Печать фото, надписей, лого, картинок на кружку термос',true, 10.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Белая кружка - фарфор','cup','Печать фото на кружку фарфоровую - премиум.',true, 13.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Подушка белая с фото','pillow','Красивая, сочная печать фотографий на подушки 40-40 см',true, 25.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Подушка чёрная - печать фото','pillow','Нанесение изображения на подушку, оборотная сторона чёрная',true, 26.8);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Подушка сердце-печать','pillow','Романтичная подушка с красной каймой в виде сердца для печати.',true, 32.8);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Подушки на 14 февраля','pillow','Подушки на 14 февраля №2 (кекс)',true, 31.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Печать на терелку полноцвет','plate','Напечатаем на тарелочку ваше изображение',true, 28);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Майки женские-печать','shirt','Напечатаем ваше любое изображение на нашу женскую майку нужного Вам размера',true, 32.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Майки мужские печать','shirt','Напечатаем ваше любое изображение на нашу мужскую майку нужного Вам размера. ',true, 35);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Майки 14 февраля','shirt','Майки 14 февраля оригинальный дизайн№1',true, 29);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Майки 14 февраля','shirt','Майки 14 февраля оригинальный дизайн№9',true, 33.3);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Подушки на 14 февраля','pillow','Подушки на 14 февраля оригинальный дизайн №11',true, 34.4);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Тарелка с полями на заказ','plate','Красивые тарелочки с фотографиями на заказ',true, 24.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Пазл с вашим фото - глянцевый картон','puzzle','Печать фотографий и картинок на пазл разного размера и формы на заказ.',true, 65.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Пазл с вашим фото','puzzle','Качественная печать вашего фото на пазл  ',true, 50.8);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Полотенце с Вашим фото','towel','Нанесение полноцвеного изображения на полотенца',true, 102);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Коврик для мыши дизайн','mousePad','Нанесение изображения на коврики для мыши',true, 42);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Коврик для мыши','mousePad','Коврик для мыши с Вашим Фото',true, 61);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Фото на полотенцах','towel','Великолепный подарок фото на полотенце',true, 115);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Пазл с  фото-сердце','puzzle','Качественная печать вашего фото на пазл  ',true, 36.5);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Тарелка на День Рождения','plate','Праздичный подарок с фото на выбор',true, 25);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Тарелка - Любовь','plate','Популярные тарелки на тему любви',true, 32);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Майка - Кофе и пончик','shirt','Веселая майка с фото. Возможен Ваш вариант',true, 31);
INSERT INTO Items(name, type, description, active, cost)
VALUES ('Кружка - Лучший тренер','cup','Веселая кружка в подарок другу',true, 10.8);
INSERT INTO Orders(user_id, details, status, init_date, issue_date) VALUES (4,'Заберу сам', 'DONE','2019-03-10','2019-03-11');
INSERT INTO Orders(user_id, details, status, init_date) VALUES (4,'Гурского 4', 'PAID','2019-03-12');
INSERT INTO Orders(user_id, details, status, init_date, issue_date) VALUES (5,'-375-29-1131879', 'DONE','2019-03-07','2019-03-08');
INSERT INTO Orders(user_id, details, status, init_date) VALUES (5,'Плеханова 8', 'PAID','2019-03-13');
INSERT INTO Orders(user_id, details, status, init_date, issue_date) VALUES (6,'+375297894512', 'DONE','2019-03-8','2019-03-10');
INSERT INTO Orders(user_id, details, status, init_date) VALUES (6,'Волгоградская 7', 'PAID','2019-03-13');
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (4,1,1,2);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (4,2,1,2);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (4,3,2,2);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (5,4,3,1);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (5,32,3,1);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (5,5,4,5);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (6,6,5,1);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (6,13,5,1);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (6,14,6,2);
INSERT INTO Carts(user_id, item_id, order_id, count) VALUES (6,15,6,3);
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (4,1,'Отличное качество. Все понравилось', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (4,2,'Полная хре...', '2019-03-01', 'BLOCKED');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (5,2,'Негативный комментарий', '2019-03-01', 'BLOCKED');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (6,5,'Негативный комментарий', '2019-03-01', 'BLOCKED');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (4,34,'Отличное качество. Все понравилось', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (4,33,'Отличное качество. Все понравилось', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (4,32,'Отличное качество. Все понравилось', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (4,31,'Отличное качество. Все понравилось', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (4,30,'Отличное качество. Все понравилось', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (4,15,'Отличное качество. Все понравилось', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (5,34,'Рекомендую!', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (5,33,'Рекомендую!', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (5,32,'Рекомендую!', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (5,30,'Рекомендую!', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (5,15,'Рекомендую!', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (5,1,'Рекомендую!', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (5,5,'Рекомендую!', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (6,34,'Буду брать еще', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (6,2,'Буду брать еще', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (6,3,'Буду брать еще', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (6,15,'Буду брать еще', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (6,31,'Буду брать еще', '2019-03-01', 'ACTIVE');
INSERT INTO Comments(user_id, item_id, message, date, status) VALUES (6,33,'Буду брать еще', '2019-03-01', 'ACTIVE');
INSERT INTO Questions(user_id, request, response, request_date, response_date) VALUES (4, 'Хотел уточнить фото можно любое?',
 'Да, дизайнер поможет его обработать, если понадобиться', '2019-03-01', '2019-03-02');
INSERT INTO Questions(user_id, request, response, request_date, response_date) VALUES (5, 'Как правильно указать другой адрес доставки?',
 'Вы можете указать его в примечании к заказу', '2019-03-01', '2019-03-02');
INSERT INTO Questions(user_id, request, response, request_date, response_date) VALUES (6, 'Наверное это глупый вопрос?',
 'Ну, если вам так кажется, то да', '2019-03-01', '2019-03-02');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('cup', '[0,1,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('high', '[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('low', '[0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,1,0,0,1]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('medium', '[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,0,1,0,0,1,1,1,1,1]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('mousePad', '[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('pillow', '[1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('plate', '[0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('puzzle', '[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('shirt', '[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,0]');
INSERT INTO Bitmaps (criteria_key, data) VALUES ('towel', '[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0]');


