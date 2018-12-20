package test.rawconsoleinput;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import test.Main;

public class TestRCI
{
	private static final Logger logger = Logger.getLogger( TestRCI.class );

	public static void main( String[] args ) throws Exception
	{
		DOMConfigurator.configure( Main.FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		try
		{
			while ( true )
			{
				int rc = RawConsoleInput.read( true );
				if ( rc<0 || rc==0x03 )
					break;
				logger.debug( String.format( "char(%04X,%d,%c)",rc,rc,rc ) );
			}
		}
		finally
		{
			RawConsoleInput.resetConsoleMode();
		}

		logger.debug( "End" );
	}

}
