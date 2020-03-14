package test.parsetxtcmdtree2;

import java.util.ArrayList;
import java.util.List;


public class CmdTreeToken
{
/*
- a token-ek:
	- ">>" : cmd start
	- "><" : cmd end
	- ">|>": line separator
	- "+" : elágazás (több jelentése lehet)
	- "'" : elágazásból új ág kezdete vagy vége csak "+" vagy "|" jel alatt lehet
	- "|" : elágazás kötőjel, csak "+" jel alatt lehet
	- egy vagy több "-": kötöjel
	- nem "-": "-" után levő nem kötőjel vagy előbb felsorolt karakterek, a következő '-'-ig tart", pl ezek lehetnek
		- "nem kötőjel nagybetűvel kezdődik (tsm utasítás), a következő '-'-ig tart" : tsm utasítás (vagy több, space-el elválasztva)
		- "nem kötőjel kisbetűvel kezdődik (valamilyen tsm utasítás paramétere), a következő '-'-ig tart" : tsm utasítás (vagy több, space-el elválasztva)
 */
	public enum CmtTreeTokenType
	{
		commandStart( "commandStart",">>" ),
		commandEnd( "commandEnd","><" ),
		lineSeparator( "lineSeparator",">|>" ),
		junction1( "junction1","+" ),
		junction2( "junction2","'" ),
		junction3( "junction3","|" ),
		junction4( "junction4","." ),
		separator( "separator","-" ),
		notSeparator( "notSeparator","" );

		private final String name;
		private final String chars;

		private CmtTreeTokenType( String name,String chars )
		{
			this.name = name;
			this.chars = chars;
		}

		public String getName()
		{
			return name;
		}
		public String getChars()
		{
			return chars;
		}
	};
	public enum CmtTreeTokenMode
	{
		MainLine,
		RepeatLine,
		SubLine,
	}

	public static int indexAllTokens = 0;
	public static final CmdTreeToken[] arrayAllTokens = new CmdTreeToken[1000];

	public final CmtTreeTokenType type;
	public final CmtTreeTokenMode mode;
	public final TxtTreePos pos;
	public final List<CmdTreeToken> listNextItems = new ArrayList<CmdTreeToken>();
	public final int indexToken;


	public CmdTreeToken( CmtTreeTokenType type,CmtTreeTokenMode mode,TxtTreePos pos )
	{
		this.type = type;
		this.mode = mode;
		this.pos = pos;
		this.indexToken = indexAllTokens++; 
		arrayAllTokens[this.indexToken] = this;
	}

	public TxtTreePos getNextPosLineBelowOrAbove( CmtTreeTokenType tokenTpye,int direction )
	{
		if ( tokenTpye.chars.length()!=1 )
			throw new RuntimeException( String.format( "invalid getNextPosLineBelowOrAbove" ) );

		TxtTreePos ttpos = new TxtTreePos();
		ttpos.indexOfLines = pos.indexOfLines + direction;
		ttpos.posStartInLine = pos.posStartInLine;
		ttpos.posLength = tokenTpye.chars.length();
		return ttpos;
	}

	public TxtTreePos getNextPosInLine( CmtTreeTokenType tokenTpye,String lineCont )
	{
		TxtTreePos ttpos = new TxtTreePos();
		ttpos.indexOfLines = pos.indexOfLines;
		ttpos.posStartInLine = pos.posStartInLine + pos.posLength;
		if ( tokenTpye==CmtTreeTokenType.separator )
		{
			int countSepChars = 0;
			for ( int ic=0; ic<lineCont.length(); ic++ )
			{
				if ( lineCont.charAt( ic )=='-' )
					countSepChars++;
				else
					break;
			}
			ttpos.posLength = countSepChars;
		}
		else if ( tokenTpye==CmtTreeTokenType.notSeparator )
		{
			int index = lineCont.indexOf( '-' );
			if ( index<0 )
				throw new RuntimeException( String.format( "invalid notSeparator(%s)",lineCont ) );

			ttpos.posLength = index;
		}
		else
		{
			ttpos.posLength = tokenTpye.chars.length();
		}
		return ttpos;
	}

	public String toString( List<String> listLinesOneCommand )
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeToken [type=" );
		builder.append( type );
		builder.append( ", mode=" );
		builder.append( mode );
		builder.append( ", text=" );
		builder.append( listLinesOneCommand.get( pos.indexOfLines ).substring( pos.posStartInLine,pos.posStartInLine+pos.posLength ) );
		builder.append( ", listNextItems=[" );
		for ( CmdTreeToken token : listNextItems )
			builder.append( token.indexToken ).append( ',' );
		builder.append( "] ]" );
		return builder.toString();
	}
}
