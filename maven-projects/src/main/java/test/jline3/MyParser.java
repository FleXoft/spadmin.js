package test.jline3;

import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.SyntaxError;

public class MyParser implements Parser
{
	@Override
	public ParsedLine parse( String line,int cursor,ParseContext context ) throws SyntaxError
	{
		return null;
	}
}
