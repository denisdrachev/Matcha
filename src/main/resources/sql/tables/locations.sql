CREATE TABLE IF NOT EXISTS locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    profile INT,
    location VARCHAR(255) NOT NULL,
    FOREIGN KEY (profile)  REFERENCES profiles (id)
) ;