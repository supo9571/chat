-- ----------------------------
-- 客服成员
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_kefu_memeber (
  member_name       			varchar(50) NOT NULL ,
  member_groupid              int(11) UNSIGNED NOT NULL ,
  member_createtime 			datetime NOT NULL ,
  PRIMARY KEY (member_name),
  INDEX pangugle_user_createtime(member_createtime)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;

-- ----------------------------
-- 客服分组
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_kefu_group (
  group_id       			  		int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  group_name     		 		varchar(100) NOT NULL ,
  group_remark     	  		varchar(255) DEFAULT '' ,
  group_max_capacity    	int(11) UNSIGNED NOT NULL,
  group_enable 				tinyint(1) DEFAULT 1 COMMENT '是否禁用 0:禁用 1:启用',
  group_createtime 	 		datetime NOT NULL ,
  PRIMARY KEY (group_id),
  UNIQUE INDEX pangu_user_group_name(group_name),
  INDEX pangu_user_createtime(group_createtime)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci AUTO_INCREMENT=1;