package   com.pangugle.im;

import com.pangugle.framework.bean.ErrorResult;

public enum FriendErrorResult implements ErrorResult{
	
	
	ERR_FRIEND_ADD_MAX_LIMIT_SELF(30201, "添加好友已上限"),
	ERR_FRIEND_ADD_BY_LIMIT(30202, "对方拒绝你的消息"),
	ERR_FRIEND_ADD_MAX_LIMIT_TARGET(30201, "对方添加好友已上限"),
	
	ERR_ACCOUNT_DISABLED(30022, "账户被冻结"),
	;

	private int code;
	private String msg;
	
	private FriendErrorResult(int code, String msg) 
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
