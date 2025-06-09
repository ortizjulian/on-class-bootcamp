CREATE TABLE IF NOT EXISTS bootcamp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    launch_date DATE NOT NULL,
    duration INT NOT NULL,
    capability_quantity INT NOT NULL
);