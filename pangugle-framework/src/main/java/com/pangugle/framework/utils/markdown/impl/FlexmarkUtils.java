package com.pangugle.framework.utils.markdown.impl;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class FlexmarkUtils {
	
	private Parser mParser;
	private HtmlRenderer mHtmlReander;
	
	private interface MyInternal {
		public FlexmarkUtils mgr= new FlexmarkUtils();
	}
	
	private FlexmarkUtils()
	{
		MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        this.mParser = Parser.builder(options).build();
        this.mHtmlReander = HtmlRenderer.builder(options).build();
	}
	
	public static FlexmarkUtils getIntance()
	{
		return MyInternal.mgr;
	}
	
	public String toHtml(String content)
	{
        // You can re-use parser and renderer instances
        Node document = mParser.parse(content);
        String html = mHtmlReander.render(document);  // "<p>This is <em>Sparta</em></p>\n"
        return html;
	}

}
