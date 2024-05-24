CREATE TABLE IF NOT EXISTS accounts (
  id INTEGER AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  role VARCHAR(256) NOT NULL,
  first_name VARCHAR(256) NOT NULL,
  last_name VARCHAR(256) NOT NULL,
  balance_in_dkk INTEGER NOT NULL
) engine=InnoDB;

-- password = admin
INSERT IGNORE INTO accounts(username, password, role, first_name, last_name, balance_in_dkk) VALUES("admin", "$2a$10$0.sx37/fBVMAxmasa3M5.uvNUPPXj6HSvjdyOsPONvG2WCVjq1KVW", "ADMIN", "Kasra", "Mp", 100);
-- password = 1234
INSERT IGNORE INTO accounts(username, password, role, first_name, last_name, balance_in_dkk) VALUES("leo", "$2a$10$3GigtQXq.9U9xSSQ8Vylee7P/QdIK40gknKjmgFeCF7zaOxi5/eZq", "USER", "Leonardo", "DiCaprio", 20);
INSERT IGNORE INTO accounts(username, password, role, first_name, last_name, balance_in_dkk) VALUES("will", "$2a$10$3GigtQXq.9U9xSSQ8Vylee7P/QdIK40gknKjmgFeCF7zaOxi5/eZq", "USER", "Will", "Smith", 0);
INSERT IGNORE INTO accounts(username, password, role, first_name, last_name, balance_in_dkk) VALUES("den", "$2a$10$3GigtQXq.9U9xSSQ8Vylee7P/QdIK40gknKjmgFeCF7zaOxi5/eZq", "USER", "Denzel", "Washington", 65);