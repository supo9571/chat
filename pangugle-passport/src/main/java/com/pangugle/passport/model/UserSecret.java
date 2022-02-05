package   com.pangugle.passport.model;

import com.pangugle.framework.utils.MD5;

public class UserSecret {
	
	private String name;
	private String password;
	private String salt;
	
	
	
	public static String getColumnPrefix(){
        return "user";
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public boolean checkPassword(String pwd)
	{
		String encryPwd = encryPassword(name, pwd, salt);
		return encryPwd.equalsIgnoreCase(this.password);
	}

	public static String encryPassword(String account, String pwd, String salt)
	{
		return MD5.encode(account + pwd + salt + "fadsf674674(&%$*^%I");
	}
	
}
