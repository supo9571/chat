package com.pangugle.passport;

public class TestString {
	
	public static void main(String[] args)
	{
		String str1 = "3";
		String str2 = "2";
		
		int lenStr1 = str1.length();
		int lenStr2 = str2.length();
		
		int len = Math.min(lenStr1, lenStr2);
		String tmp = null;
		for(int i = 0; i < len; i ++)
		{
			int rs = str1.charAt(i) - str2.charAt(i);
			if(rs > 0)
			{
				tmp = str1;
				str1 = str2;
				str2 = tmp;
				break;
			} 
			if(rs < 0) {
				break;
			}
		}
		if(tmp == null) {
			if(lenStr1 > lenStr2)
			{
				tmp = str2;
				str2  = str1;
				str1 = tmp;
			}
		} 
		
		System.out.println(str1 + " < " + str2);
	}

}
