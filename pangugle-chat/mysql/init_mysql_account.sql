### master execute
CREATE USER 'writeuser'@'%' IDENTIFIED BY 'kjlsadfjlkasfdj@987mn';
GRANT INSERT, DELETE, UPDATE, SELECT ON pangugledb.* TO 'writeuser'@'%';


### slave execute
CREATE USER 'readuser'@'%' IDENTIFIED BY 'dfsafa@%dgfgu97';
GRANT SELECT ON pangugledb.* TO 'readuser'@'%';