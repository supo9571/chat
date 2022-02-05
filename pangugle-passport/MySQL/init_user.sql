-- ----------------------------
-- 用户表 
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_user (
  user_id       			  		int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  user_name       			varchar(50) NOT NULL ,
  user_phone     			varchar(30) DEFAULT NULL DEFAULT '',
  user_email       			varchar(50) DEFAULT NULL DEFAULT '',
  user_password   			char(32) COLLATE utf8_bin NOT NULL ,
  user_nickname   			varchar(20) DEFAULT '' comment '用户昵称' ,
  user_sex         				varchar(10) DEFAULT '' comment 'sex' ,
  user_salt       				char(32)  COLLATE utf8_bin NULL DEFAULT NULL ,
  user_createtime 			datetime NOT NULL ,
  user_avatar     			varchar(255) DEFAULT '',
  user_registerpath 		varchar(15) DEFAULT '' comment 'wx|ios|android|pc',
  user_registerip 			varchar(15) NOT NULL ,
  user_lastloginip 			varchar(45) DEFAULT '' comment '最后登录IP',
  user_lastlogintime  		datetime DEFAULT NULL comment '最后登录时间',
  user_enable_status     tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (user_id),
  UNIQUE INDEX pangugle_user_name(user_name),
  UNIQUE INDEX pangugle_user_phone(user_phone),
  UNIQUE INDEX pangugle_user_email(user_email),
  INDEX pangugle_user_createtime(user_createtime)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci;

