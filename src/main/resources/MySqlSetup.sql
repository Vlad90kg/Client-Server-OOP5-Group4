DROP DATABASE IF EXISTS mobile_store;
CREATE DATABASE mobile_store;
USE mobile_store;

-- Create Brand table to store mobile phone manufacturers
CREATE TABLE Brand
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255)
);

-- Insert sample brands
INSERT INTO Brand (name, description)
VALUES ('Apple', 'Apple Inc.'),
       ('Samsung', 'Samsung Electronics'),
       ('Huawei', 'Huawei Technologies'),
       ('OnePlus', 'OnePlus Mobile');

-- Create MobilePhone table as the main entity
CREATE TABLE MobilePhone
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    brand_id       INT            NOT NULL,
    model          VARCHAR(50)    NOT NULL,
    quantity       INT            NOT NULL,
    price          DECIMAL(10, 2) NOT NULL,
    specifications VARCHAR(255),
    FOREIGN KEY (brand_id) REFERENCES Brand (id)
);

-- Insert at least 10 sample mobile phone records
INSERT INTO MobilePhone (brand_id, model, quantity, price, specifications)
VALUES (1, 'iPhone 14 Pro', 15, 999.99, '128GB, A16 Bionic'),
       (1, 'iPhone 14', 20, 799.99, '128GB, A15 Bionic'),
       (2, 'Galaxy S23 Ultra', 10, 1199.99, '256GB, Snapdragon 8 Gen 2'),
       (2, 'Galaxy S23', 25, 899.99, '128GB, Snapdragon 8 Gen 2'),
       (3, 'P50 Pro', 8, 699.99, '128GB, Kirin 9000'),
       (3, 'Nova 9', 12, 499.99, '128GB, Snapdragon 750G'),
       (4, 'OnePlus 11', 18, 749.99, '256GB, Snapdragon 8 Gen 2'),
       (4, 'OnePlus Nord 2', 22, 399.99, '128GB, MediaTek Dimensity 1200'),
       (2, 'Galaxy A53', 30, 499.99, '128GB, Exynos 1280'),
       (1, 'iPhone SE', 25, 429.99, '64GB, A13 Bionic');
