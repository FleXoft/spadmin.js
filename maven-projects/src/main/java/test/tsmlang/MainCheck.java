package test.tsmlang;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;

public class MainCheck
{
	public static final String COMMAND_TREE_XML = "command-tree1.xml";

	public static LinkedList<CmdTreeNode> listCmdTreeNodes = new LinkedList<CmdTreeNode>();
	public static void addNextCmdTreeNode( CmdTreeNode cmdTreeNode )
	{
		listCmdTreeNodes.addLast( cmdTreeNode );
	}

	public static void main( String[] args ) throws Exception
	{
		CmdTreeRootNode nodeRoot = LoadCmdTreeXml.loadXml();

		PrintCmdTree.recursivePrintCmdTreeNode( nodeRoot,"" );

//		checkInput( nodeRoot,"  quer   node" );
		checkInput( nodeRoot,"  q   n do=domain1" );
//		checkInput( nodeRoot,"  q   n node1 auth=ld t=" );
//		checkInput( nodeRoot,"  dir" );
//		checkInput( nodeRoot,"  q  ac" );

//		checkInput( args[0] );
//		handleTab( args[0] );
	}

	private static void checkInput( CmdTreeRootNode nodeRoot,String cmd )
	{
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
				SearchCmdTreeForNextWord.addTabChoices( lastMatchingPos.getCTNode(),lastMatchingPos.getCmd() );
			}
			else
			{
				SearchCmdTreeForNextWord.search( lastMatchingPos );
			}
		}

		SearchCmdTreeForMatch.printResults( cmd );
		SearchCmdTreeForNextWord.printResults( cmd );
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
