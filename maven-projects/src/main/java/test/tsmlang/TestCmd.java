package test.tsmlang;

import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class TestCmd
{
	private static final Logger logger = Logger.getLogger( MainCheck.class );

	public static final String FILENAME_LOG4j_CONFIG = "log4j-config.xml";

	public static void main( String[] args ) throws Exception
	{
		DOMConfigurator.configure( FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		try
		{
			CmdTreeRootNode nodeRoot = LoadCmdTreeXml.loadXml();

			try ( Scanner scanner = new Scanner( System.in ) )
			{
				while ( true )
				{
					System.out.println( "Enter command:" );
					String cmd = scanner.nextLine();
					if ( "quit".equalsIgnoreCase( cmd )==true )
						break;
					List<CmdTreeParseTabChoices> listTabChoices = MainCheck.checkInput( nodeRoot,cmd );
					if ( listTabChoices.isEmpty()==false )
					{
						System.out.println( "--------- tab choices:" );
						for ( CmdTreeParseTabChoices item : listTabChoices )
							System.out.println( String.format( "%s (%d)",item.getStrChoice(),item.getCtnode().indexNode ) );
					}
					else
					{
						System.out.println( "--------- no tab choices" );
					}
//					System.out.println( "Your username is " + cmd );
				}
			}
		}
		catch ( Exception exc )
		{
			logger.error( "",exc );
		}

		logger.debug( "End." );
		System.out.println( "End." );
	}
}
