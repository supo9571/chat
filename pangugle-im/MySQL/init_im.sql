-- ----------------------------
-- 用户朋友关系表
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_im_user_friend_relation (
  relation_from_userid      	varchar(50) NOT NULL ,
  relation_to_userid     			varchar(50) NOT NULL ,
  relation_request_userid    	varchar(50) NOT NULL  comment '请求添加好友的人',
  relation_from_status    		varchar(20) NOT NULL  comment 'unconfirm = 待确认 | enable=通过',
  relation_to_status    			varchar(20) NOT NULL  comment 'unconfirm = 待确认 | enable=通过',
  relation_createtime 			datetime NOT NULL,
  relation_updatetime 			datetime NOT NULL,
  relation_remark					varchar(255) comment '添加好友时的备注',
  PRIMARY KEY (relation_from_userid, relation_to_userid),
  INDEX pangugle_im_user_friend_relation_to_userid_from_userid(relation_to_userid, relation_from_userid),
  INDEX pangugle_im_user_friend_relation_createtime(relation_createtime),
  INDEX pangugle_im_user_friend_relation_updatetime(relation_updatetime)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 用户对朋友信息的备注
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_im_user_friend_info (
  info_userid      		varchar(50) NOT NULL ,
  info_friendid     		varchar(50) NOT NULL ,
  info_createtime 		datetime NOT NULL ,
  info_black				tinyint(1) comment '是否在黑名单里',
  info_alias				varchar(100) comment '别名',
  info_mobile			varchar(20) comment '手机号',
  info_remark			varchar(200) comment '更多详情',
  PRIMARY KEY (info_userid, info_friendid),
  INDEX pangugle_im_user_friend_info_friendid_black(info_friendid, info_black),
  INDEX pangugle_im_user_friend_info_createtime(info_createtime)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ----------------------------
-- 用户群组表
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_im_user_group (
  group_id         				varchar(32) NOT NULL ,
  group_holder					varchar(50) NOT NULL COMMENT '群主-用户名',
  group_name      			varchar(255) NOT NULL DEFAULT '' COMMENT '群名称',
  group_alias	      			varchar(255) NOT NULL DEFAULT '' COMMENT '群名称-群前三个成员的昵称',
  group_icons	      			varchar(2048) NOT NULL DEFAULT '' COMMENT '图标',
  group_notice     			varchar(200) NOT NULL DEFAULT '' COMMENT '公告',
  group_current_capacity int(11)  NOT NULL  DEFAULT 1 COMMENT '当前群总人数',
  group_max_capacity 		int(11)  NOT NULL  DEFAULT 1 COMMENT '当前群上限总人数',
  group_enable_invite 			tinyint(1)  NOT NULL  DEFAULT 1 COMMENT '默认允许群成员拉人',
  group_enable_add_friend	tinyint(1)  NOT NULL  DEFAULT 1 COMMENT '默认允许群成员添加好友',
  group_enable_chat				tinyint(1)  NOT NULL  DEFAULT 1 COMMENT '默认允许全员聊天',
  group_createtime 			datetime NOT NULL ,
  group_enable_status     	tinyint(1) NOT NULL DEFAULT 1 COMMENT 'true = 启用 | false= 禁用',
  PRIMARY KEY (group_id),
  INDEX pangugle_im_user_group_holder_status(group_holder, group_enable_status),
  INDEX pangugle_im_user_group_createtime(group_createtime)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=1;

-- ----------------------------
-- 群组表与用户的关系
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_im_user_group_relation (
  relation_groupid			varchar(32) NOT NULL COMMENT '群组id => pangugle_im_user_group',
  relation_username      	varchar(50) NOT NULL ,
  relation_nickname			varchar(20) DEFAULT '' COMMENT '群内用户昵称', 
  relation_free			     	tinyint(1) NOT NULL DEFAULT 0 COMMENT 'true = 免打扰  | false= 打扰',
  relation_createtime 		datetime NOT NULL ,
  PRIMARY KEY (relation_groupid, relation_username),
  INDEX pangugle_im_user_group_relation_username(relation_username)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=1;

-- ----------------------------
-- 群管理员表
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_im_user_group_admin (
  admin_groupid	      varchar(32) NOT NULL,
  admin_username    varchar(50) NOT NULL,
  PRIMARY KEY (admin_groupid, admin_username)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=1;




