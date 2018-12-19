package test;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class TestTSM
{
	private static final Logger logger = Logger.getLogger( TestTSM.class );


//	public TestTSM() throws Exception
//	{
//		List<String> tokens = new ArrayList<>();
//		try (BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) ))
//		{
//			StringTokenizer st = new StringTokenizer( br.readLine() );
//
//			while ( st != null && st.hasMoreElements() )
//			{
//				tokens.add( st.nextToken() );
//			}
//
//			logger.debug( tokens );
//		}
//	}

	

	public TestTSM()
	{
		try ( Scanner keyboard = new Scanner( System.in ) )
		{
			boolean exit = false;
			while ( !exit )
			{
				logger.debug( "Enter command (quit to exit):" );
				String input = keyboard.nextLine();
				if ( input != null )
				{
					logger.debug( "Your input is : " + input );
					if ( "quit".equals( input ) )
					{
						logger.debug( "Exit programm" );
						exit = true;
					}
					else
					{
						logger.debug( String.format( "input:",input ) );
					}
				}
			}
		}
	}

	public static void main( String[] args )
	{
		DOMConfigurator.configure( Main.FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		try
		{
			new TestTSM();
		}
		catch ( Exception exc )
		{
			logger.error( "",exc );
		}

		logger.debug( "End" );
	}
}
