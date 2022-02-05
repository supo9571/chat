package com.pangugle.framework.utils.markdown;

import com.pangugle.framework.utils.markdown.impl.FlexmarkUtils;

public class MarkdownUtils {
	
	
//	public static void main(String[] args)
//	{
//		Parser parser = Parser.builder().build();
//		Node document = parser.parse("This is *Sparta*");
//		HtmlRenderer renderer = HtmlRenderer.builder().build();
//		String rs = renderer.render(document); 
//		
//		System.out.println(rs);
//	}
	
	public static String toHtml(String content)
	{
		return FlexmarkUtils.getIntance().toHtml(content);
	}
	

}
