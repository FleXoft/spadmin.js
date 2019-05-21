package test.tsmlang;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;
import test.tsmlang.cmdtreenodes.CmdTreeNode;
import test.tsmlang.cmdtreenodes.CmdTreeRootNode;

public class MainCheck
{
	private static final Logger logger = Logger.getLogger( MainCheck.class );

	public static final String FILENAME_LOG4j_CONFIG = "log4j-config.xml";

	public static final String COMMAND_TREE_XML = "command-tree1.xml";

	public static LinkedList<CmdTreeNode> listCmdTreeNodes = new LinkedList<CmdTreeNode>();
	public static void addNextCmdTreeNode( CmdTreeNode cmdTreeNode )
	{
		listCmdTreeNodes.addLast( cmdTreeNode );
	}

	public MainCheck() throws Exception
	{
		CmdTreeRootNode nodeRoot = LoadCmdTreeXml.loadXml();

		PrintCmdTree.recursivePrintCmdTreeNode( nodeRoot,"" );

		logger.debug( "---------listCmdTreeNodes" );
		for ( CmdTreeNode ctNode : listCmdTreeNodes )
		{
			logger.debug( String.format( "node(%02d)(%s)",ctNode.getIndexNode(),ctNode.getCmdSample() ) );
		}

//		checkInput( nodeRoot,"  query" );
//		checkInput( nodeRoot,"  quer   node" );
		checkInput( nodeRoot,"  q   no no" );
//		checkInput( nodeRoot,"  q   n do=domain1" );
//		checkInput( nodeRoot,"  q   n node1 auth=" );
//		checkInput( nodeRoot,"  q   n node1 auth=ld t=" );
//		checkInput( nodeRoot,"  q   n node1 auth=ld t=nas" );
//		checkInput( nodeRoot,"  dir" );
//		checkInput( nodeRoot,"  q  ac" );
//		checkInput( nodeRoot,"  q  actlog" );

//		checkInput( args[0] );
//		handleTab( args[0] );
	}

	public static void main( String[] args ) throws Exception
	{
		DOMConfigurator.configure( FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		try
		{
			new MainCheck();
		}
		catch ( Exception exc )
		{
			logger.error( "",exc );
		}

		logger.debug( "End." );
	}

	public static List<CmdTreeParseTabChoices> checkInput( CmdTreeRootNode nodeRoot,String cmd )
	{
		ObjectTabChoices.listTabChoices.clear();

//		be kell járni a fát úgy, hogy nézni kell, mikor hova mehetek
//			ha nincs továbbmenési lehetőség, akkor kész a parse-olás
//		choice ágnál akkor és csak akkor megyek a következő szintre, ha a fix vagy a full part egyezik
//		a fában egy adott szinten vagy csak choice vagy csak seq elemek lehetnek
//		ha megakad az egyeztetés, akkor az adott szinten levő elemek kerülnek a TAB listába

//		a cmdTree paraméterben az adott szint összes node-ja benne van

//		egy szint vizsgálatakor:
//			- leveszem a kezdő space-eket
//			- ellenőrzöm, hogy a szinten egyforma tag-ek vannak-e (seq,choice)
//			- sorban vizsgálom a tag-eket
//				- ha megfelelő, akkor a következő szintre lépek
//			- ha nincs találat, akkor visszamegyek az utolsó olyan szintre, ami még egyezett

		SearchCmdTreeForMatch.init( nodeRoot,cmd );
		SearchCmdTreeForMatch.recursiveSearchCmdTreeForMatch( "" );

		CmdTreeParsePosition lastMatchingPos = SearchCmdTreeForMatch.getLastMathcingPosition();
		if ( lastMatchingPos!=null )
		{
			if ( lastMatchingPos.getMatch()==TYPE_MATCH.partial )
			{
				ObjectTabChoices.addTabChoices( lastMatchingPos.getCTNode(),lastMatchingPos.getCmd() );
			}
			else
			{
				SearchCmdTreeForNextWord.search( lastMatchingPos );
			}
		}

		SearchCmdTreeForMatch.printResults( cmd );
		SearchCmdTreeForNextWord.printResults( cmd );

		return ObjectTabChoices.listTabChoices;
	}

	public static String skipFirstSpaces( String cmd )
	{
		int ic;
		for ( ic=0; ic<cmd.length() && Character.isWhitespace( cmd.charAt( ic ) )==true; ic++ );
		return cmd.substring( ic );
	}

//	private static void handleTab( String cmd )
//	{
//		// TODO Auto-generated method stub
//	}
}
