package test.jline3;

import java.util.List;

import org.apache.log4j.Logger;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

public class MyCompleter implements Completer
{
	private static final Logger logger = Logger.getLogger( MyCompleter.class );

	@Override
	public void complete( LineReader reader,ParsedLine line,List<Candidate> candidates )
	{
		logger.debug( "MyCompleter..." );
	}
}
