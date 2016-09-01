package com.sangupta.outline.help;

/**
 * Unit tests for {@link IndentedStringWriter}.
 * 
 * @author sangupta
 *
 */
public class IndentedStringWriteTest {

	public static void main(String[] args) {
		IndentedStringWriter sw = new IndentedStringWriter(20);
		sw.writeLine("this is a long line that should be breaking at 20 characters and must flow accordingly.");
		sw.incrementIndent();
		sw.writeLine("The first paragraph that we are writing is going to be this one - that is 1 level indented");
		sw.incrementIndent();
		sw.writeLine("The second paragraph that we are writing is going to be this one - that is 1 level indented");
		sw.decrementIndent();
		sw.writeLine("The third paragraph that we are writing is going to be this one - that is 1 level indented");
		
		System.out.println(sw.getString());
	}
}
