package test.parsetxtcmdtree;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;

public class ParseTxtCmdTree
{
	private static final Logger logger = Logger.getLogger( ParseTxtCmdTree.class );

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

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document mainCmdTreeXml = docBuilder.newDocument();

			while ( true )
			{
				List<String> listLinesOneCommand = mcr.readNextCommand();
				if ( listLinesOneCommand==null )
					break;

				CommandParser mcp = new CommandParser( listLinesOneCommand );
				Document commandTreeXml = mcp.generateCommandTreeXml();

				mergeCmdTreeXmls( mainCmdTreeXml,commandTreeXml );
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource( mainCmdTreeXml );
			StreamResult result = new StreamResult( new File( FILENAME_OUT ) );
			transformer.transform( source,result );

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

//			try( PrintWriter pw = new PrintWriter( new FileWriter( FILENAME_OUT ) ) )
//			{
//				logger.warn( String.format( "no cmd in file (%s)",pathTxt ) );
//			}
		}
		catch ( Exception exc )
		{
			logger.error( "",exc );
		}

		logger.debug( "End." );
		System.out.println( "End." );
	}

	private static void mergeCmdTreeXmls( Document mainCmdTreeXml,Document commandTreeXml )
	{
	}
}
