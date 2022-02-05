package com.pangugle.im;

import com.pangugle.framework.bean.ErrorResult;

public enum IMErrorResult implements ErrorResult  {
	ERR_INVALID_TOKEN(30001,  "invalid token"),
	ERR_MULTY_CLIENT(30002,  "multy client"),
	;

	private int code;
	private String error;
	private IMErrorResult(int code, String error)
	{
		this.error = error;
		this.code = code;
	}
	
	@Override
	public String getError() {
		return error;
	}

	@Override
	public int getCode() {
		return code;
	}

}
