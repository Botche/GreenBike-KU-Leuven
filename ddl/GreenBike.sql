CREATE DATABASE a21pt104;
USE a21pt104;

CREATE TABLE user_role (
	id VARCHAR(64) NOT NULL DEFAULT (UUID()),
	name VARCHAR(255) NOT NULL,
	
	PRIMARY KEY(id)
);

CREATE TABLE user (
	id VARCHAR(64) NOT NULL DEFAULT (UUID()),
	email VARCHAR(256) NOT NULL, 
	password VARCHAR(256) NOT NULL,
   role_id VARCHAR(64) NOT NULL,
   
   PRIMARY KEY(id),
   
   CONSTRAINT uc_email UNIQUE (email),
	CONSTRAINT fk_roles FOREIGN KEY (role_id)  
	REFERENCES user_role(id)
	ON UPDATE CASCADE
);

CREATE TABLE bike_brand (
	id VARCHAR(64) NOT NULL DEFAULT (UUID()),
	name VARCHAR(256),
	
	PRIMARY KEY(id)
);

CREATE TABLE bike_material (
	id VARCHAR(64) NOT NULL DEFAULT (UUID()),
	name VARCHAR(256),
	
	PRIMARY KEY(id)
);

CREATE TABLE bike_category (
	id VARCHAR(64) NOT NULL DEFAULT (UUID()),
	name VARCHAR(256),
	
	PRIMARY KEY(id)
);

CREATE TABLE bike (
	id VARCHAR(64) NOT NULL DEFAULT (UUID()),
	model VARCHAR(256) NOT NULL,
	brand_id VARCHAR(64) NOT NULL,
	material_id VARCHAR(64) NOT NULL,
	category_id VARCHAR(64) NOT NULL,
	image_url VARCHAR(256),
   is_for_rent BOOLEAN NOT NULL,
   price DECIMAL(7, 2) NOT NULL,
   
   PRIMARY KEY(id),
   
	CONSTRAINT fk_brand FOREIGN KEY (brand_id)  
	REFERENCES bike_brand(id)
	ON UPDATE CASCADE,
	
	CONSTRAINT fk_material FOREIGN KEY (material_id)  
	REFERENCES bike_material(id)
	ON UPDATE CASCADE,
	
	CONSTRAINT fk_category FOREIGN KEY (category_id)  
	REFERENCES bike_category(id)
	ON UPDATE CASCADE
);

CREATE TABLE user_bike_mapping (
	id VARCHAR(64) NOT NULL DEFAULT (UUID()),
	user_id VARCHAR(64) NOT NULL,
	bike_id VARCHAR(64) NOT NULL,
	rent_start_date DATETIME,
	rent_end_date DATETIME,
	buy_date DATETIME,
	
	PRIMARY KEY(id),
	
	CONSTRAINT fk_user FOREIGN KEY (user_id)  
	REFERENCES user(id)
	ON UPDATE CASCADE,
	
	CONSTRAINT fk_bike FOREIGN KEY (bike_id)  
	REFERENCES bike(id)
	ON UPDATE CASCADE,
	
	UNIQUE KEY(user_id, bike_id)
);


INSERT INTO user_role (name) VALUES
("normal"),
("admin");