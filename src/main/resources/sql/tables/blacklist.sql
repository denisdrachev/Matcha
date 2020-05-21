CREATE TABLE IF NOT EXISTS blacklist (
    who INT,
    whom INT,
    FOREIGN KEY (who)  REFERENCES profiles (id),
    FOREIGN KEY (whom)  REFERENCES profiles (id)
) ENGINE InnoDB;