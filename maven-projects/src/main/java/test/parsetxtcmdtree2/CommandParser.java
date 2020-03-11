package test.parsetxtcmdtree2;

import java.util.List;

import org.apache.log4j.Logger;

import test.parsetxtcmdtree.CommandParser.BranchType;
import test.parsetxtcmdtree2.GraphVertex.GraphVertexType;

public class CommandParser
{
	private static final Logger logger = Logger.getLogger( CommandParser.class );

	private static final String COMMAND_START_PREFIX = ">>-";
	private static final String COMMAND_MULTILINE_SEPARATOR = ">|>";

	private final List<String> listLinesOneCommand;
	private GraphVertex cmdStart = null;

	public enum Token { plusSign, equalSign, commandEnd, listOrName, option, lineEnd, questioMark };


	public CommandParser( List<String> listLinesOneCommand )
	{
		this.listLinesOneCommand = listLinesOneCommand;
	}

	public void parse() throws Exception
	{
		logger.debug( String.format( "------------cmd line start" ) );
		for ( String line : listLinesOneCommand )
		{
			logger.debug( String.format( "cmd line:(%s)",line ) );
		}

		this.cmdStart = getCmdStartVertex();
		logger.debug( String.format( "cmdStart ttpos(%s)",this.cmdStart.pos.toString() ) );

//		DEfine ALERTTrigger, hibás commaSeparatedList '+--message_number-+'
//		DEFine PROFASSOCiation, több szintű elágazás
//		DEFine SCRipt, egymásba ágyazott '+'
//		DEFine DATAMover, hibás + elágazás, 2 '+' van egymás alatt
//		DEFine DEVclass--device_class_name--DEVType--=--CENTERA, hibás option: "?PEA_file"
//		DELete DEDUPSTats, spec listás '+'
//		DEFine GRPMEMber, hibás '+' jel
//		DEFine SPACETrigger----STG, nem lehet eldönteni, hogy option vagy lista
//		DEFine STGRULE, csak default érték van!

		while ( true )
		{
			Token token = getNextToken();
			logger.debug( String.format( "token(%s) mainCol(%d)",token,mainCol ) );
			if ( token==Token.commandEnd )
				break;
			else if ( token==Token.lineEnd )
			{
				mainCol += COMMAND_MULTILINE_SEPARATOR.length();
			}
			else if ( token==Token.listOrName )
			{
				String tokenStr = readToken();
				logger.debug( String.format( "listOrName tokenStr(%s) mainCol(%d)",tokenStr,mainCol ) );
			}
			else if ( token==Token.option )
			{
				String tokenStr = readToken();
				logger.debug( String.format( "option tokenStr(%s) mainCol(%d)",tokenStr,mainCol ) );

				Token tokenAfterOption = getNextToken();
				if ( tokenAfterOption==Token.equalSign )
				{
					this.mainCol++;

					Token tokenValue = getNextToken();
					if ( tokenValue==Token.listOrName )
					{
						String tokenStr2 = readToken();
						logger.debug( String.format( "value listOrName tokenStr(%s) mainCol(%d)",tokenStr2,mainCol ) );
					}
					else if ( tokenValue==Token.option )
					{
						String tokenStr2 = readToken();
						logger.debug( String.format( "value option tokenStr(%s) mainCol(%d)",tokenStr2,mainCol ) );
					}
					else if ( tokenValue==Token.plusSign )
					{
						BranchType bType = getBranchType();
						calcBranchEndCol( bType );
						logger.debug( String.format( "plusSignEnd,mainCol(%d)",mainCol ) );
					}
					else
						throw new RuntimeException( "" );
				}
				else if ( tokenAfterOption==Token.plusSign )
				{
					// folytatódik a parancs, pl DEFine SPACETrigger----STG
				}
				else
					throw new RuntimeException( "" );
			}
			else if ( token==Token.plusSign )
			{
				BranchType bType = getBranchType();
				calcBranchEndCol( bType );
				logger.debug( String.format( "plusSignEnd,mainCol(%d)",mainCol ) );
			}
			else if ( token==Token.questioMark )
			{
				String tokenStr = readToken();
				logger.debug( String.format( "questioMark tokenStr(%s) mainCol(%d)",tokenStr,mainCol ) );
			}
		}
	}

	private void calcBranchEndCol( BranchType bType )
	{
		// TODO Auto-generated method stub
		
	}

	private BranchType getBranchType()
	{
//		Character chAbove = getCharAtPos( this.mainLineIndex-1,this.mainCol );
//		Character chUnder = getCharAtPos( this.mainLineIndex+1,this.mainCol );
//
//		if ( listLinesOneCommand.size()==this.mainLineIndex )
//			return this.mainLineIndex;
//
//		if ( this.mainLineIndex>0 )
//		{
//			String line = listLinesOneCommand.get( this.mainLineIndex-1 );
//			if ( this.mainCol<line.length() )
//			{
//				char ch = line.charAt( this.mainCol );
//				if ( ch=='|' )
//					return this.mainLineIndex-1;
//				if ( ch!='.' && ch!=' ' )
//					throw new RuntimeException( "" );
//			}
//		}
//
//		for ( int index = this.mainLineIndex+1; true; index++ )
//		{
//			String line = listLinesOneCommand.get( index );
//			char ch = line.charAt( this.mainCol );
//			if ( ch==' ' )
//				return this.mainLineIndex;
//			if ( ch=='|' )
//				continue;
//			if ( ch=='\'' )
//				return index;
//			if ( ch=='+' )	// hibás elégazás pl: DEFine DATAMover
//				continue;
//			throw new RuntimeException( "" );
//		}
		return null;
	}

	private Character getCharAtPos( int indexRow,int indexCol )
	{
		Character result = null;
		if ( 0<=indexRow && indexRow<listLinesOneCommand.size() )
		{
			String line = listLinesOneCommand.get( indexRow );
			if ( 0<=indexCol && indexCol<line.length() )
				return new Character( line.charAt( indexCol ) );
		}
		return result;
	}

	private void findBranchMainLineEnd( int indexBranchMainLine )
	{
		// továbblépünk a '+'-ról
		this.mainCol++;

		if ( indexBranchMainLine<this.mainLineIndex )
		{
			// csak egy felsorolás
		}
		else if ( indexBranchMainLine==this.mainLineIndex )
		{
			String line = listLinesOneCommand.get( indexBranchMainLine );
			boolean bContentFound = false;
			while ( true )
			{
				Token token = getNextTokenForLine( line );
				if ( token==Token.listOrName || 
					token==Token.option )
				{
					bContentFound = true;
					String tokenStr = readToken();
					logger.debug( String.format( "tokenStr(%s) mainCol(%d)",tokenStr,mainCol ) );
					continue;
				}
				if ( token==Token.plusSign )
				{
					if ( bContentFound==false )
						throw new RuntimeException( "" );
					this.mainCol++;
					return;
				}
				throw new RuntimeException( "" );
			}
		}
		else
		{
			String line = listLinesOneCommand.get( indexBranchMainLine );
			int index = line.indexOf( '\'',this.mainCol+1 );
			if ( index<0 )
				throw new RuntimeException( "" );

			String lineParent = listLinesOneCommand.get( this.mainLineIndex );
			if ( lineParent.charAt( index )!='+' )
				throw new RuntimeException( "" );

			String strBranch = line.substring( this.mainCol+1,index );
			logger.debug( String.format( "strBranch(%s) mainCol(%d)",strBranch,mainCol ) );
			this.mainCol = index+1;
		}
	}

	private String readToken()
	{
		String line = listLinesOneCommand.get( mainLineIndex );
		StringBuilder result = new StringBuilder();
		for ( int index = this.mainCol; true; index++ )
		{
			char ch = line.charAt( index );
			if ( ch=='-' )
			{
				this.mainCol = index;
				return result.toString();
			}
			result.append( ch );
		}
	}

	private Token getNextToken()
	{
		String line = listLinesOneCommand.get( mainLineIndex );
		return getNextTokenForLine( line );
	}

	private Token getNextTokenForLine( String line )
	{
		int index = this.mainCol;
		while ( true )
		{
			char ch = line.charAt( index );
			if ( ch=='-' )
			{
				index++;
				continue;
			}
			else if ( ch=='+' )
			{
				this.mainCol = index;
				return Token.plusSign;
			}
			else if ( ch=='=' )
			{
				this.mainCol = index;
				return Token.equalSign;
			}
			else if ( ch=='>' )
			{
				// ><
				// >|>
				if ( line.charAt( index+1 )=='<' )
				{
					this.mainCol = index+1;
					return Token.commandEnd;
				}
				if ( line.charAt( index+1 )=='|' && line.charAt( index+2 )=='>' )
				{
					this.mainCol = index+2;
					return Token.lineEnd;
				}
				throw new RuntimeException( "" );
			}
			else if ( 'a'<=ch && ch<='z' )
			{
				this.mainCol = index;
				return Token.listOrName;
			}
			else if ( ('A'<=ch && ch<='Z') || ('0'<=ch && ch<='9') )
			{
				this.mainCol = index;
				return Token.option;
			}
			else if ( '?'==ch )
			{
				this.mainCol = index;
				return Token.questioMark;
			}
			logger.error( String.format( "getNextTokenForLine ch(%X,%c) index(%d)",(int)ch,ch,index ) );
			throw new RuntimeException( "" );
		}
	}

	private GraphVertex getCmdStartVertex()
	{
		TxtTreePos ttpos = new TxtTreePos();
		ttpos.indexOfLines = 0;
		for ( String line : listLinesOneCommand )
		{
			if ( line.startsWith( COMMAND_START_PREFIX )==true )
			{
				ttpos.posStartInLine = 0;
				ttpos.posLength = COMMAND_START_PREFIX.length();
				return new GraphVertex( GraphVertexType.commandStart,ttpos );
			}
			ttpos.indexOfLines++;
		}
		throw new RuntimeException( "" );
	}
}
