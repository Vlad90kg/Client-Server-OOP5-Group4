DROP DATABASE IF EXISTS mobile_store;
CREATE DATABASE mobile_store;
USE mobile_store;

-- Create Brand table to store mobile phone manufacturers
CREATE TABLE brand
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255)
);

-- Insert sample brands
INSERT INTO brand (name, description)
VALUES ('Apple', 'Apple Inc.'),
       ('Samsung', 'Samsung Electronics'),
       ('Huawei', 'Huawei Technologies'),
       ('OnePlus', 'OnePlus Mobile');

-- Create MobilePhone table as the main entity
CREATE TABLE mobile_phone
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    brand_id       INT            NOT NULL,
    model          VARCHAR(50)    NOT NULL,
    quantity       INT            NOT NULL,
    price          DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);

-- Insert at least 10 sample mobile phone records
INSERT INTO mobile_phone (brand_id, model, quantity, price)
VALUES (1, 'iPhone 14 Pro', 15, 999.99),
       (1, 'iPhone 14', 20, 799.99),
       (2, 'Galaxy S23 Ultra', 10, 1199.99),
       (2, 'Galaxy S23', 25, 899.99),
       (3, 'P50 Pro', 8, 699.99),
       (3, 'Nova 9', 12, 499.99),
       (4, 'OnePlus 11', 18, 749.99),
       (4, 'OnePlus Nord 2', 22, 399.99),
       (2, 'Galaxy A53', 30, 499.99),
       (1, 'iPhone SE', 25, 429.99);

CREATE TABLE phone_specifications
(
    phone_id INT PRIMARY KEY,
    storage  VARCHAR(20),
    chipset  VARCHAR(50),
    FOREIGN KEY (phone_id) REFERENCES mobile_phone (id)
);

INSERT INTO phone_specifications (phone_id, storage, chipset)

VALUES (1, '128GB', 'A16 Bionic'),
       (2, '128GB', 'A15 Bionic'),
       (3, '256GB', 'Snapdragon 8 Gen 2'),
       (4, '128GB', 'Snapdragon 8 Gen 2'),
       (5, '128GB', 'Kirin 9000'),
       (6, '128GB', 'Snapdragon 750G'),
       (7, '256GB', 'Snapdragon 8 Gen 2'),
       (8, '128GB', 'MediaTek Dimensity 1200'),
       (9, '128GB', 'Exynos 1280'),
       (10, '64GB', 'A13 Bionic');
