package   com.pangugle.im;

import com.pangugle.framework.bean.ErrorResult;

/**
 * 
 * @author Administrator
 * dfad
 */
public enum TokenErrorResult implements ErrorResult{
	ERR_ACCESSTOKEN_INVALID(20001, "invalid access token"), 
	ERR_LOGINTOKEN_INVALID(20002, "invalid login token"),
	;

	private int code;
	private String msg;
	
	private TokenErrorResult(int code, String msg) 
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
