package test.parsetsmhelptxts;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ObjectHelpTXTMultiLine
{
	private static final Logger logger = Logger.getLogger( ObjectHelpTXTMultiLine.class );

	public enum PARSE_STATUS { Start,SyntaxLine,LinesAfterSyntaxLine,LinesCmdPart,EmptyLinesAfterCmdPart,LinesLastCmdPart,End };
	private final Map<Integer,String> hmFinalLines;
	private final List<String> listLinesCmdPart;
	private PARSE_STATUS parseStatus = PARSE_STATUS.Start;

	public ObjectHelpTXTMultiLine()
	{
		listLinesCmdPart = new ArrayList<String>();
		hmFinalLines = new HashMap<Integer,String>();
	}

	public PARSE_STATUS addLine( String line )
	{
		if ( parseStatus!=PARSE_STATUS.Start )
			logger.debug( String.format( "before addLine(%s,%s)",parseStatus.name(),line ) );
		if ( parseStatus==PARSE_STATUS.Start )
		{
			if ( line.equals( "Syntax " )==true )
				parseStatus = PARSE_STATUS.SyntaxLine;
		}
		else if ( parseStatus==PARSE_STATUS.SyntaxLine )
		{
			if ( line.startsWith( ">>-" )==true )
				throw new RuntimeException( "PARSE_STATUS.SyntaxLine" );
			parseStatus = PARSE_STATUS.LinesAfterSyntaxLine;
		}
		else if ( parseStatus==PARSE_STATUS.LinesAfterSyntaxLine )
		{
			if ( line.startsWith( ">>-" )==true )
			{
				listLinesCmdPart.clear();
				listLinesCmdPart.add( line );
				if ( line.endsWith( "-><" )==true )
					parseStatus = PARSE_STATUS.LinesLastCmdPart;
				else
					parseStatus = PARSE_STATUS.LinesCmdPart;
			}
		}
		else if ( parseStatus==PARSE_STATUS.LinesCmdPart )
		{
			if ( line.isEmpty()==true )
			{
				parseStatus = PARSE_STATUS.EmptyLinesAfterCmdPart;
				processCmdPart();
			}
			else
			{
				if ( line.endsWith( "-><" )==true )
					parseStatus = PARSE_STATUS.LinesLastCmdPart;
				else
					parseStatus = PARSE_STATUS.LinesCmdPart;
				listLinesCmdPart.add( line );
			}
		}
		else if ( parseStatus==PARSE_STATUS.LinesLastCmdPart )
		{
			if ( line.isEmpty()==true )
			{
				parseStatus = PARSE_STATUS.End;
				processCmdPart();
//				printFinalLines();
			}
			else
			{
				listLinesCmdPart.add( line );
			}
		}
		else if ( parseStatus==PARSE_STATUS.EmptyLinesAfterCmdPart )
		{
			if ( line.isEmpty()==true )
				throw new RuntimeException( "PARSE_STATUS.EmptyLinesAfterCmdPart" );

			if ( line.endsWith( "-><" )==true )
				parseStatus = PARSE_STATUS.LinesLastCmdPart;
			else
				parseStatus = PARSE_STATUS.LinesCmdPart;
			listLinesCmdPart.clear();
			listLinesCmdPart.add( line );
		}

		if ( parseStatus!=PARSE_STATUS.Start )
			logger.debug( String.format( "after addLine(%s)",parseStatus.name(),line ) );
		return parseStatus;
	}

//	private void printFinalLines()
//	{
//		ArrayList<Integer> sortedList = new ArrayList<Integer>( hmFinalLines.keySet() );
//		Collections.sort( sortedList );
//		for ( Integer index : sortedList )
//		{
//			String line = hmFinalLines.get( index );
//			logger.debug( String.format( "(%02d)(%s)",index,line ) );
//		}
//	}
//
	private void processCmdPart()
	{
		int index = searchForCenterLine();
		fillFinalLines( index );
	}

	private void fillFinalLines( int indexCenterLine )
	{
		String strFinalCenterLine = hmFinalLines.get( 0 );
		int lenFinalCenterLine = ( strFinalCenterLine==null ) ? 0 : strFinalCenterLine.length();
//		int lenCenterLine = listLinesCmdPart.get( indexCenterLine ).length();
		for ( int ic=0; ic<listLinesCmdPart.size(); ic++ )
		{
			Integer indexFinalLines = ic-indexCenterLine;
//			String lineFinalTmp = String.format( strFormatLine,listLinesCmdPart.get( ic ) );
			String lineFinalTmp = listLinesCmdPart.get( ic );

			if ( lenFinalCenterLine==0 )
			{
				hmFinalLines.put( indexFinalLines,lineFinalTmp );
			}
			else
			{
				String prevFinalLine = hmFinalLines.get( indexFinalLines );
				if ( prevFinalLine==null )
					prevFinalLine = "";
				String strFormatLine = String.format( "%%-%ds|",lenFinalCenterLine );
				lineFinalTmp = String.format( strFormatLine,prevFinalLine ) + lineFinalTmp;
				hmFinalLines.put( indexFinalLines,lineFinalTmp );
			}
		}
	}

	private int searchForCenterLine()
	{
		Integer result = null;
		String strStartLine = ( hmFinalLines.isEmpty()==true ) ? ">>-" : ">-";
		for ( int ic=0; ic<listLinesCmdPart.size(); ic++ )
		{
			String line = listLinesCmdPart.get( ic );
			if ( line.startsWith( strStartLine )==true )
			{
				result = ic;
				break;
			}
		}
		if ( result==null )
			throw new RuntimeException( "searchForCenterLine" );
		return result;
	}

	public PARSE_STATUS getParseStatus()
	{
		return parseStatus;
	}

	public void printFinalLines( PrintWriter pw )
	{
		ArrayList<Integer> sortedList = new ArrayList<Integer>( hmFinalLines.keySet() );
		Collections.sort( sortedList );
		for ( Integer index : sortedList )
		{
			String line = hmFinalLines.get( index );
			pw.println( line );
		}
	}
}
