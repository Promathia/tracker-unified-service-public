CREATE SCHEMA IF NOT EXISTS subscription_tracker;

CREATE TABLE IF NOT EXISTS registrationtype (
  id SMALLINT AUTO_INCREMENT,
  registration_type_name VARCHAR(20) NOT NULL,
  PRIMARY KEY (id));

INSERT INTO registrationtype (registration_type_name) VALUES ('GOOGLE');
INSERT INTO registrationtype (registration_type_name) VALUES ('PHONE');

CREATE TABLE IF NOT EXISTS roles (
  id SMALLINT AUTO_INCREMENT,
  role_name VARCHAR(20) NOT NULL,
  PRIMARY KEY (id));

INSERT INTO roles (role_name) VALUES ('ADMIN');
INSERT INTO roles (role_name) VALUES ('OWNER');
INSERT INTO roles (role_name) VALUES ('INSTRUCTOR');
INSERT INTO roles (role_name) VALUES ('VISITOR');

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT,
  external_id VARCHAR(50) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(100),
  birth_date DATE NOT NULL,
  registration_type_id SMALLINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (registration_type_id) REFERENCES registrationtype(id));

INSERT INTO
users (external_id, first_name, last_name, email, birth_date, registration_type_id)
VALUES ('tje0mNWLspgmgzg2Np5mAYrqO2K2', 'Ivan', 'Akulenka', 'ivakulenka@gmail.com', '1987-07-24', 1);

CREATE TABLE IF NOT EXISTS city (
  id INT,
  city_name_en VARCHAR(80) NOT NULL,
  city_name_ru VARCHAR(80) NOT NULL,
  PRIMARY KEY (id));

INSERT INTO city (id, city_name_en, city_name_ru) VALUES (1, 'Brest', 'Брест');
INSERT INTO city (id, city_name_en, city_name_ru) VALUES (2, 'Vitebsk', 'Витебск');
INSERT INTO city (id, city_name_en, city_name_ru) VALUES (3, 'Gomel', 'Гомель');
INSERT INTO city (id, city_name_en, city_name_ru) VALUES (4, 'Grodno', 'Гродно');
INSERT INTO city (id, city_name_en, city_name_ru) VALUES (5, 'Minsk', 'Минск');
INSERT INTO city (id, city_name_en, city_name_ru) VALUES (6, 'Mogilev', 'Могилев');

CREATE TABLE IF NOT EXISTS club (
  id INT AUTO_INCREMENT,
  club_name VARCHAR(80) NOT NULL,
  club_name_alt VARCHAR(80) NOT NULL,
  image_name VARCHAR(50),
  city_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (city_id) REFERENCES city(id));

INSERT INTO club (club_name, club_name_alt, image_name, city_id) VALUES ('UNBROKEN', 'Анброкен', 'ubroken_club_logo', 3);
INSERT INTO club (club_name, club_name_alt, image_name, city_id) VALUES ('Tonus Plus', 'Тонус Плюс', 'tonus_plus_club_logo', 3);
INSERT INTO club (club_name, club_name_alt, image_name, city_id) VALUES ('Atlet', 'Атлет', 'atlet_club_logo', 3);

CREATE TABLE IF NOT EXISTS user_club (
  user_id INT,
  club_id INT,
  user_accepted SMALLINT,
  active_club SMALLINT,
  PRIMARY KEY (user_id, club_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (club_id) REFERENCES club(id));

INSERT INTO user_club (user_id, club_id, user_accepted, active_club) VALUES (1,1,1,1);

CREATE TABLE IF NOT EXISTS user_role (
  user_id INT,
  club_id INT,
  role_id SMALLINT,
  PRIMARY KEY (user_id, club_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (club_id) REFERENCES club(id),
  FOREIGN KEY (role_id) REFERENCES roles(id));

INSERT INTO user_role (user_id, club_id, role_id) VALUES (1,1,1);

USE `subscription_tracker`;
DROP procedure IF EXISTS `InsertUser`;

DELIMITER $$
USE `subscription_tracker`$$
CREATE PROCEDURE InsertUser(
    IN external_id VARCHAR(50),
	IN first_name VARCHAR(50),
	IN last_name VARCHAR(50),
	IN phone VARCHAR(20),
	IN email VARCHAR(100),
	IN birth_date DATE,
	IN registration_type_id SMALLINT,
	IN club_id INT,
    OUT result_user_id INT
)
BEGIN

    DECLARE exit handler FOR SQLEXCEPTION, SQLWARNING
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

	INSERT INTO users
    (external_id, first_name, last_name,phone , email, birth_date, registration_type_id)
    VALUES (external_id, first_name, last_name, phone, email, birth_date, registration_type_id);
    SELECT LAST_INSERT_ID() into result_user_id;
    INSERT INTO user_club (user_id, club_id, user_accepted, active_club) VALUES (result_user_id, club_id, 0, 1);
    INSERT INTO user_role (user_id, club_id, role_id) VALUES (result_user_id, club_id, 4);

    COMMIT;
END$$

DELIMITER ;

CREATE TABLE IF NOT EXISTS subscription (
  id INT AUTO_INCREMENT,
  club_id INT NOT NULL,
  visits_limit INT NOT NULL,
  term_days INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (club_id) REFERENCES club(id));

INSERT INTO subscription (club_id, visits_limit, term_days) VALUES (1,5,20);
INSERT INTO subscription (club_id, visits_limit, term_days) VALUES (1,8,30);
INSERT INTO subscription (club_id, visits_limit, term_days) VALUES (1,12,60);
INSERT INTO subscription (club_id, visits_limit, term_days) VALUES (1,16,70);
INSERT INTO subscription (club_id, visits_limit, term_days) VALUES (1,0,30);

CREATE TABLE IF NOT EXISTS user_subscription (
  id INT AUTO_INCREMENT,
  user_id INT,
  subscription_id INT,
  buy_date DATE,
  deadline DATE,
  visit_counter INT,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (subscription_id) REFERENCES subscription(id));

CREATE TABLE IF NOT EXISTS user_visits (
  id INT AUTO_INCREMENT,
  user_subscription_id INT,
  visit_date DATE,
  PRIMARY KEY (id),
  FOREIGN KEY (user_subscription_id) REFERENCES user_subscription(id));

DELIMITER $$
USE `subscription_tracker`$$
CREATE PROCEDURE UseUserSubscription(
	IN subscriptionId INT,
	IN visitDate Date,
    OUT rowCount INT
)
BEGIN

    DECLARE counter INT;
	DECLARE vlimit INT;
	DECLARE sdeadline DATE;
    DECLARE exit handler FOR SQLEXCEPTION, SQLWARNING
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SELECT user_subscription.visit_counter, subscription.visits_limit, user_subscription.deadline INTO counter, vlimit, sdeadline
    FROM user_subscription
    INNER JOIN subscription
    ON subscription.id = user_subscription.subscription_id
    WHERE user_subscription.id = subscriptionId;

    IF ((vlimit = 0) OR (counter < vlimit)) AND DATE(sdeadline) >= DATE(visitDate) THEN
        START TRANSACTION;
			UPDATE user_subscription SET user_subscription.visit_counter = counter + 1 WHERE user_subscription.id = subscriptionId;
            SELECT ROW_COUNT() into rowCount;
				IF rowCount > 0 THEN
					INSERT INTO user_visits(user_subscription_id, visit_date) VALUES (subscriptionId, visitDate);
                END IF;
        COMMIT;
    END IF;


END$$

DELIMITER ;

CREATE TABLE IF NOT EXISTS club_timetable (
  id INT AUTO_INCREMENT,
  club_id INT NOT NULL,
  timetable VARCHAR(5000),
  PRIMARY KEY (id),
  FOREIGN KEY (club_id) REFERENCES club(id));

USE `subscription_tracker`;
DROP procedure IF EXISTS `InsertTimetable`;

DELIMITER $$
USE `subscription_tracker`$$
CREATE PROCEDURE InsertTimetable(
    IN club_id int,
	IN timetable VARCHAR(5000),
    OUT result_timetable_id INT
)
BEGIN

    DECLARE exit handler FOR SQLEXCEPTION, SQLWARNING
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

	INSERT INTO club_timetable (club_id, timetable) VALUES (club_id, timetable);
    SELECT LAST_INSERT_ID() into result_timetable_id;

    COMMIT;
END$$

DELIMITER ;

CREATE TABLE IF NOT EXISTS club_info (
  id INT AUTO_INCREMENT,
  club_id INT NOT NULL,
  information VARCHAR(5000),
  PRIMARY KEY (id),
  FOREIGN KEY (club_id) REFERENCES club(id));

USE `subscription_tracker`;
DROP procedure IF EXISTS `InsertClubInfo`;

DELIMITER $$
USE `subscription_tracker`$$
CREATE PROCEDURE InsertClubInfo(
    IN club_id int,
	IN information VARCHAR(5000),
    OUT result_information_id INT
)
BEGIN

    DECLARE exit handler FOR SQLEXCEPTION, SQLWARNING
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

	INSERT INTO club_info (club_id, information) VALUES (club_id, information);
    SELECT LAST_INSERT_ID() into result_information_id;

    COMMIT;
END$$

DELIMITER ;
