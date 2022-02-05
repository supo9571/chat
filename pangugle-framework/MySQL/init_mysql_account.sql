### master execute
CREATE USER 'writeuser'@'%' IDENTIFIED BY 'kjlsadfjlkasfdj@987mn';
GRANT INSERT, DELETE, UPDATE, SELECT ON pangudb.* TO 'writeuser'@'%';


### slave execute
CREATE USER 'readuser'@'%' IDENTIFIED BY 'dfsafa@%dgfgu97';
GRANT SELECT ON pangudb.* TO 'readuser'@'%';