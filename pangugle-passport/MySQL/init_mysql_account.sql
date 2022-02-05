### master execute
CREATE USER 'writeuser'@'%' IDENTIFIED BY 'kjlsadfjlkasfdj@987mn';
GRANT INSERT, DELETE, UPDATE, SELECT ON pangugle_dev_db.* TO 'writeuser'@'%';


### slave execute
CREATE USER 'readuser'@'%' IDENTIFIED BY 'dfsafa@%dgfgu97';
GRANT SELECT ON pangugle_dev_db.* TO 'readuser'@'%';