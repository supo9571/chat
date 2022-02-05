package com.pangugle.modules.web.fix;

import org.springframework.stereotype.Service;

import com.pangugle.framework.spring.DaoSupport;
import com.pangugle.framework.utils.CollectionUtils;

@Service
public class InitDataService extends DaoSupport {
	
	public void clearAll()
	{
		clearAllTableData("pangugle_user");
		clearAllTableData("pangugle_im_user_friend_relation");
		clearAllTableData("pangugle_im_user_friend_info");
		
		clearAllTableData("pangugle_im_user_group");
		clearAllTableData("pangugle_im_user_group_admin");
		clearAllTableData("pangugle_im_user_group_relation");
	}

	
	private void clearAllTableData(String table)
	{
		Object[] values = CollectionUtils.emptyObjectArray();
		mWriterJdbcService.executeUpdate("delete from " + table, values);
	}
	
}
