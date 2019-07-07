package test.parsetsmhelptxts;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import test.parsetsmhelptxts.ObjectHelpTXTMultiLine.PARSE_STATUS;

public class ParseTsmHelpTxts
{
	private static final Logger logger = Logger.getLogger( ParseTsmHelpTxts.class );

	public static final String FILENAME_LOG4j_CONFIG = "log4j-config.xml";
	private static final String FILENAME_OUT = "r:/out.txt";

	public static void main( String[] args )
	{
		DOMConfigurator.configure( FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		try
		{
			Path dir = Paths.get( "R:\\tsm-help-txts" );
			List<Path> listHelpFiles = getListHelpFiles( dir );
//			List<Path> listHelpFiles = new ArrayList<Path>();
//			listHelpFiles.add( Paths.get( "R:\\tsm-help-txts\\help3.13.1" ) );
			try( PrintWriter pw = new PrintWriter( new FileWriter( FILENAME_OUT ) ) )
			{
				for ( Path pathTxt : listHelpFiles )
				{
//					pw.println( pathTxt.toString() );
					logger.warn( String.format( "file(%s)",pathTxt ) );

					List<String> allLines = Files.readAllLines( pathTxt,StandardCharsets.ISO_8859_1 );

					ObjectHelpTXTMultiLine objht = new ObjectHelpTXTMultiLine();
					for ( String line : allLines )
					{
//						pw.println( String.format( "(%s,%s)",logStr( indexSyntax,indexLastSyntaxLine,indexEmptyLineAfterLastSyntaxLine ),line ) );

						PARSE_STATUS status = objht.addLine( line );
						if ( status==PARSE_STATUS.End )
						{
							objht.printFinalLines( pw );
							break;
						}
					}
					if ( objht.getParseStatus()!=PARSE_STATUS.End )
						logger.warn( String.format( "no cmd in file (%s)",pathTxt ) );
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

//	private static String logStr( Integer indexSyntax,Integer indexLastSyntaxLine,Integer indexEmptyLineAfterLastSyntaxLine )
//	{
//		StringBuilder sb = new StringBuilder();
//		if ( indexSyntax==null ) sb.append( "null" ); else sb.append( String.format( "%03d",indexSyntax ) );
//		if ( indexLastSyntaxLine==null ) sb.append( ",null" ); else sb.append( String.format( ",%03d",indexLastSyntaxLine ) );
//		if ( indexEmptyLineAfterLastSyntaxLine==null ) sb.append( ",null" ); else sb.append( String.format( ",%03d",indexEmptyLineAfterLastSyntaxLine ) );
//		return sb.toString();
//	}
//
	private static List<Path> getListHelpFiles( Path dir ) throws Exception
	{
		List<Path> result = null;
		try ( Stream<Path> list = Files.list( dir ) )
		{
			result = list.collect( Collectors.toList() );
		}
		return result;
	}
}
