package   com.pangugle.im;

import com.pangugle.framework.bean.ErrorResult;

public enum GroupErrorResult implements ErrorResult{
	
	ERR_GROUP_ADD_LIMIT(30301, "创建群组已上限"),
	ERR_GROUP_DELETE(30302, "群组已解散"),
	ERR_GROUP_BLACK_OR_NOT_INVITE(30303, "操作禁止!"),
	ERR_GROUP_HAS_MEMBER(30304, "群组还有成员未删除"),
	ERR_GROUP_CAPACITY_LIMIT(30304, "群组成员已超过最大限制"),
	;

	private int code;
	private String msg;
	
	private GroupErrorResult(int code, String msg) 
	{
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String getError() {
		return msg;
	}

	@Override
	public int getCode() {
		return code;
	}

}
