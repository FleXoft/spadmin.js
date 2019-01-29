package test.jline3;

import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jline.builtins.Completers;
import org.jline.builtins.Completers.DirectoriesCompleter;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import test.Main;

public class TestJLine3
{
	private static final Logger logger = Logger.getLogger( TestJLine3.class );

	public static void main( String[] args ) throws Exception
	{
		DOMConfigurator.configure( Main.FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		Terminal terminal = TerminalBuilder.builder()
			.system(true)
			.build();
		DirectoriesCompleter fscompleter = new DirectoriesCompleter(Paths.get( "." ));
		LineReader reader = LineReaderBuilder.builder()
			.terminal(terminal)
//			.completer(new MyCompleter())
			.completer(fscompleter)
//			.parser(new MyParser())
			.build();

		String prompt = "PREFIX>";
		while ( true )
		{
			String line = null;
			try
			{
				line = reader.readLine( prompt );
				logger.debug( String.format( "line(%s)",line ) );
			}
			catch ( UserInterruptException exc )
			{
				logger.debug( String.format( "UserInterruptException(%s)",exc.getMessage() ) );
			}
			catch ( EndOfFileException exc )
			{
				logger.debug( String.format( "EndOfFileException(%s)",exc.getMessage() ) );
				break;
			}
		}

		logger.debug( "End" );
	}
}
