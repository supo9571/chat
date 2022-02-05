package   com.pangugle.passport;

import com.pangugle.framework.bean.ErrorResult;

/**
 * 
 * @author Administrator
 * dfad
 */
public enum UserErrorResult implements ErrorResult{
	ERR_ACCESSTOKEN_INVALID(20001, "invalid access token"), 
	ERR_LOGINTOKEN_INVALID(20002, "invalid login token"),
	ERR_PHONE(20003, "手机号为空或格式不正确"),
	ERR_PHON_EXIST (20004, "手机号已存在"),
	ERR_EMAIL(20005, "邮箱格式不正确"),
	ERR_EMAIL_EXIST(20006, "邮箱已存在"),
	ERR_PWD(20007, "密码异常"),
	ERR_REG_FAIR(20008, "注册失败"),
	
	ERR_ACCOUNT_NOT_EXIST(20021, "账户不存在"),
	ERR_ACCOUNT_DISABLED(20022, "账户被冻结"),
	
	;

	private int code;
	private String msg;
	
	private UserErrorResult(int code, String msg) 
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
