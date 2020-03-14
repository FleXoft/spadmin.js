package test.parsetxtcmdtree2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import test.parsetxtcmdtree.CommandReader;


public class ParseTxtCmdTree2
{
	private static final Logger logger = Logger.getLogger( ParseTxtCmdTree2.class );

	public static final String FILENAME_LOG4j_CONFIG = "log4j-config.xml";
	private static final String FILENAME_IN = "R:/all-cmds-txt-tree.txt";
	private static final String FILENAME_OUT = "r:/out.txt";

	public static void main( String[] args )
	{
		DOMConfigurator.configure( FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		try
		{
			List<String> linesTxtCmdTree = Files.readAllLines( Paths.get( FILENAME_IN ) );
			CommandReader mcr = new CommandReader( linesTxtCmdTree );

			while ( true )
			{
				List<String> listLinesOneCommand = mcr.readNextCommand();
				if ( listLinesOneCommand==null || listLinesOneCommand.size()==0 )
					break;

				CommandParser mcp = new CommandParser( listLinesOneCommand );
				mcp.parse();
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
