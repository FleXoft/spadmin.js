package test.parsetxtcmdtree2;

import java.util.List;

import org.apache.log4j.Logger;

import test.parsetxtcmdtree2.CmdTreeToken.CmtTreeTokenMode;
import test.parsetxtcmdtree2.CmdTreeToken.CmtTreeTokenType;

public class CommandParser
{
	private static final Logger logger = Logger.getLogger( CommandParser.class );

	private static final String COMMAND_START_PREFIX = ">>";

	private final List<String> listLinesOneCommand;
	private CmdTreeToken startToken = null;

/*
 * statusok:
 * 		- kezdés: start vertex keresés
 * 		- ezután edge kell jöjjön: minimum 1 db '-'
 * 		- a következő '-' karakterig vertex
 * 		- ezután edge jön, minimum 1 db '-'
 * 		- '+' esetén elágazás: spec vertex "elágazás", ezalatti és efeletti karakterek megnézése
 * 		
 */


	public CommandParser( List<String> listLinesOneCommand )
	{
		this.listLinesOneCommand = listLinesOneCommand;
	}

	public void parse() throws Exception
	{
		CmdTreeToken.indexAllTokens = 0;
		logger.debug( String.format( "------------cmd line start" ) );
		for ( String line:listLinesOneCommand )
		{
			logger.debug( String.format( "cmd line:(%s)",line ) );
		}

		try
		{
			this.startToken = getStartItem();
			fillNextTokens( this.startToken );
			calcAllNextTokens( this.startToken );

			logger.debug( String.format( "startItem ttpos(%s)",this.startToken.pos.toString() ) );
		}
		finally
		{
			printTree( this.startToken );
		}
	}

	private void printTree( CmdTreeToken token )
	{
		for ( int ic=0; ic<CmdTreeToken.indexAllTokens; ic++ )
		{
			logger.debug( String.format( "token %02d:(%s)",ic,CmdTreeToken.arrayAllTokens[ic].toString( listLinesOneCommand ) ) );
		}
	}

	private void calcAllNextTokens( CmdTreeToken paramToken )
	{
		if ( paramToken.listNextItems.size()==1 )
		{
			CmdTreeToken token = paramToken.listNextItems.get( 0 );
			if ( token.type==CmtTreeTokenType.commandEnd && token.mode==CmtTreeTokenMode.MainLine )
				return;
		}

		for ( CmdTreeToken token : paramToken.listNextItems )
		{
			fillNextTokens( token );
			calcAllNextTokens( token );
		}
	}

	private void fillNextTokens( CmdTreeToken paramToken )
	{
		if ( paramToken.mode!=CmtTreeTokenMode.MainLine )
			logger.debug( "paramToken.mode!=CmtTreeTokenMode.MainLine" );
		String lineCont = getLineAfterToken( paramToken );
		String charAboveToken = getCharAboveToken( paramToken );
		String charBelowToken = getCharBelowToken( paramToken );
		if ( paramToken.type==CmtTreeTokenType.commandStart )
		{
			CmtTreeTokenType tokenTpye = getNextTokenTypeInLine( lineCont );
			if ( tokenTpye!=CmtTreeTokenType.separator )
				throw new RuntimeException( "commandStart after" );

			TxtTreePos ttpos = paramToken.getNextPosInLine( tokenTpye,lineCont );
			CmdTreeToken nextToken = new CmdTreeToken( tokenTpye,paramToken.mode,ttpos );
			paramToken.listNextItems.add( nextToken );
		}
		else if ( paramToken.type==CmtTreeTokenType.separator )
		{
			CmtTreeTokenType tokenTpye = getNextTokenTypeInLine( lineCont );
			if ( tokenTpye==CmtTreeTokenType.separator )
				throw new RuntimeException( "separator after" );

			TxtTreePos ttpos = paramToken.getNextPosInLine( tokenTpye,lineCont );
			CmdTreeToken nextToken = new CmdTreeToken( tokenTpye,paramToken.mode,ttpos );
			paramToken.listNextItems.add( nextToken );
		}
		else if ( paramToken.type==CmtTreeTokenType.notSeparator )
		{
			CmtTreeTokenType tokenTpye = getNextTokenTypeInLine( lineCont );
			if ( tokenTpye!=CmtTreeTokenType.separator )
				throw new RuntimeException( "notSeparator after" );

			TxtTreePos ttpos = paramToken.getNextPosInLine( tokenTpye,lineCont );
			CmdTreeToken nextToken = new CmdTreeToken( tokenTpye,paramToken.mode,ttpos );
			paramToken.listNextItems.add( nextToken );
		}
		else if ( paramToken.type==CmtTreeTokenType.lineSeparator )
		{
			CmtTreeTokenType tokenTpye = getNextTokenTypeInLine( lineCont );
			if ( tokenTpye!=CmtTreeTokenType.separator )
				throw new RuntimeException( "lineSeparator after" );

			TxtTreePos ttpos = paramToken.getNextPosInLine( tokenTpye,lineCont );
			CmdTreeToken nextToken = new CmdTreeToken( tokenTpye,paramToken.mode,ttpos );
			paramToken.listNextItems.add( nextToken );
		}
		else if ( paramToken.type==CmtTreeTokenType.junction1 )
		{
			{
				CmtTreeTokenType tokenTpye = getNextTokenTypeInLine( lineCont );
				if ( tokenTpye!=CmtTreeTokenType.separator )
					throw new RuntimeException( "lineSeparator after" );

				TxtTreePos ttpos = paramToken.getNextPosInLine( tokenTpye,lineCont );
				CmdTreeToken nextToken = new CmdTreeToken( tokenTpye,paramToken.mode,ttpos );
				paramToken.listNextItems.add( nextToken );
			}

			if ( charAboveToken==null && charBelowToken==null )
			{
				// van ilyen is, ez hiba: DEfine ALERTTrigger
			}

			if ( charAboveToken!=null && charBelowToken!=null )
			{
				// ilyenkor van default ág a fenti, ezzel nem foglalkozunk!
				CmtTreeTokenType tokenTpye = getNextTokenTypeInLine( charBelowToken );
				if ( tokenTpye!=CmtTreeTokenType.junction1 && tokenTpye!=CmtTreeTokenType.junction2 && tokenTpye!=CmtTreeTokenType.junction3 )
					throw new RuntimeException( "junction1 below" );

				TxtTreePos ttpos = paramToken.getNextPosLineBelowOrAbove( tokenTpye,1 );
				CmdTreeToken nextToken = new CmdTreeToken( tokenTpye,CmtTreeTokenMode.SubLine,ttpos );
				paramToken.listNextItems.add( nextToken );
			}
		}
		else if ( paramToken.type==CmtTreeTokenType.junction2 )
		{
			if ( paramToken.mode!=CmtTreeTokenMode.SubLine )
				throw new RuntimeException( "junction2 mode" );

			CmtTreeTokenType tokenTpye = getNextTokenTypeInLine( lineCont );
			if ( tokenTpye!=CmtTreeTokenType.separator )
				throw new RuntimeException( "lineSeparator after" );

			TxtTreePos ttpos = paramToken.getNextPosInLine( tokenTpye,lineCont );
			CmdTreeToken nextToken = new CmdTreeToken( tokenTpye,paramToken.mode,ttpos );
			paramToken.listNextItems.add( nextToken );
		}
		else
			throw new RuntimeException( "unknown tokentype:" + paramToken.type.getName() );
	}

	private String getCharBelowToken( CmdTreeToken paramToken )
	{
		String result = null;
		if ( listLinesOneCommand.size()>paramToken.pos.indexOfLines+1 )
		{
			String line = listLinesOneCommand.get( paramToken.pos.indexOfLines+1 );
			if ( paramToken.pos.posStartInLine<line.length() )
			{
				char ch = line.charAt( paramToken.pos.posStartInLine );
				if ( ch!=' ' )
					result = new String( new char[]{ ch } );
			}
		}
		return result;
	}

	private String getCharAboveToken( CmdTreeToken paramToken )
	{
		String result = null;
		if ( paramToken.pos.indexOfLines>0 )
		{
			String line = listLinesOneCommand.get( paramToken.pos.indexOfLines-1 );
			if ( paramToken.pos.posStartInLine<line.length() )
			{
				char ch = line.charAt( paramToken.pos.posStartInLine );
				if ( ch!=' ' )
					result = new String( new char[]{ ch } );
			}
		}
		return result;
	}

	private String getLineAfterToken( CmdTreeToken paramToken )
	{
		String line = listLinesOneCommand.get( paramToken.pos.indexOfLines );
		return line.substring( paramToken.pos.posStartInLine+paramToken.pos.posLength );
	}

	private CmtTreeTokenType getNextTokenTypeInLine( String lineCont )
	{
		CmtTreeTokenType result = CmtTreeTokenType.notSeparator;
		for ( CmtTreeTokenType type : CmtTreeTokenType.values() )
		{
			if ( type.getChars().length()>0 )
			{
				if ( lineCont.startsWith( type.getChars() )==true )
					return type;
			}
		}
		return result;
	}

	private CmdTreeToken getStartItem()
	{
		TxtTreePos ttpos = new TxtTreePos();
		ttpos.indexOfLines = 0;
		for ( String line : listLinesOneCommand )
		{
			if ( line.startsWith( COMMAND_START_PREFIX )==true )
			{
				ttpos.posStartInLine = 0;
				ttpos.posLength = COMMAND_START_PREFIX.length();
				return new CmdTreeToken( CmtTreeTokenType.commandStart,CmtTreeTokenMode.MainLine,ttpos );
			}
			ttpos.indexOfLines++;
		}
		throw new RuntimeException( "" );
	}
}
