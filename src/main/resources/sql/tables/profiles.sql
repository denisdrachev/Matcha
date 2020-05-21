CREATE TABLE IF NOT EXISTS profiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    age TINYINT NOT NULL,
    gender TINYINT NOT NULL,
    preference TINYINT NOT NULL,
    biography VARCHAR(1000) NOT NULL,
    tags VARCHAR(255) NOT NULL,
    images VARCHAR(255) NULL,
    avatar INT,
    FOREIGN KEY (avatar)  REFERENCES images (id)
) ENGINE InnoDB;