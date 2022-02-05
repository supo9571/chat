-- ----------------------------
-- 消息历史表, 废弃
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_im_history (
  history_id    				varchar(32) NOT NULL comment '消息唯一id',
  history_from_userid 	varchar(32) NOT NULL  comment '发送者',
  history_to_userid       	varchar(32) DEFAULT NULL comment '接收者',
  history_event      	int(11) DEFAULT NULL comment '消息事件类型',
  history_type_msg      	int(11) DEFAULT NULL comment '消息类型',
  history_content   		varchar(500) DEFAULT NULL comment '消息体内容' ,
  history_createtime 	datetime NOT NULL ,
  PRIMARY KEY (history_id),
  INDEX pangugle_im_history_from_userid(history_from_userid, history_to_userid),
  INDEX pangugle_im_history_to_userid(history_to_userid, history_from_userid),
  INDEX pangugle_im_history_type_msg_event(history_type_msg, history_event),
  INDEX pangugle_im_history_event(history_event),
  INDEX pangugle_im_history_createtime(history_createtime)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ;

-- ----------------------------
-- 用户群组类型配置
-- ----------------------------
CREATE TABLE IF NOT EXISTS pangugle_im_user_group_type (
  type_id         				int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  type_name      			varchar(50) NOT NULL ,
  type_remark     			varchar(200) DEFAULT '',
  type_max_capacity 	int(11) NOT NULL,
  type_createtime 		datetime NOT NULL ,
  PRIMARY KEY (type_id),
  UNIQUE INDEX pangugle_im_user_group_type_name(type_name)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=1;