CREATE TABLE users (
    id VARCHAR(36),
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
) ENGINE=InnoDB charset=UTF8;