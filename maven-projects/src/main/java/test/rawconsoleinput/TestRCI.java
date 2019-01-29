package test.rawconsoleinput;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import test.Main;

public class TestRCI
{
	private static final Logger logger = Logger.getLogger( TestRCI.class );
	private static final String PREFIX = "PREFIX>";

	public static void main( String[] args ) throws Exception
	{
		DOMConfigurator.configure( Main.FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		try
		{
			System.out.print( PREFIX );
			StringBuilder currentLine = new StringBuilder();
			while ( true )
			{
				int rc = RawConsoleInput.read( true );
				if ( rc<0 || rc==0x03 )
				{
					logger.debug( String.format( "Ctrl-C(%04X,%d,%c)",rc,rc,rc ) );
					break;
				}
				if ( rc==0x09 )
				{
					System.out.print( String.format( "\n  list: 123,6456,327246\n\n%s%s",PREFIX,currentLine ) );
				}
				else if ( rc==0x0D )
				{
					System.out.print( String.format( "\n  call command:%s:\n\n%s",currentLine,PREFIX ) );
					currentLine.setLength( 0 );
				}
				else if ( rc==0x08 )
				{
					currentLine.setLength( currentLine.length()-1 );
					System.out.print( String.format( "\r%s%s%c",PREFIX,currentLine,0x0F ) );
				}
				else
				{
					logger.debug( String.format( "char(%04X,%d,%c)",rc,rc,rc ) );
					System.out.append( (char)rc );
					currentLine.append( (char)rc );
				}
			}
		}
		finally
		{
			RawConsoleInput.resetConsoleMode();
		}

		logger.debug( "End" );
	}
}
