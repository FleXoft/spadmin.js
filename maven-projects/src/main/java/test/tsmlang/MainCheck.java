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
	private static final String COMMAND_TREE_XML = "command-tree1.xml";

	public static void main( String[] args ) throws Exception
	{
		CmdTreeRootNode nodeRoot = loadXml();

		recursivePrintCmdTreeNode( nodeRoot,"" );

		checkInput( nodeRoot,"  quer   node" );
//		checkInput( nodeRoot,"  q   n do=domain1" );
//		checkInput( nodeRoot,"  q   n node1 auth=ld t=" );
//		checkInput( nodeRoot,"  dir" );
//		checkInput( nodeRoot,"  q  ac" );

//		checkInput( args[0] );
//		handleTab( args[0] );
	}

	public static int indexNode = 0;
	private static CmdTreeRootNode loadXml() throws Exception
	{
//		egy olyan fát készítek, ahol 1 node-ból megmondható 
//		- a parent
//		- a child
//		- a szinten következő node (nextSibling)
//		ha a parent node-nak több child node-ja van, akkor a prent node az első child-ot tartalmazza, ahonnan végig lehet járni az összes sibling-et
//		ez azért kell, hogy folytathassam a parse-olást, bármelyik node-ról, tudjak bármerre mozogni

		CmdTreeRootNode rootNode = null;
		try ( FileInputStream fileIS = new FileInputStream( COMMAND_TREE_XML ) )
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse( fileIS );
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "/root";
			Node xmlNodeRoot = (Node)xPath.compile( expression ).evaluate( xmlDocument,XPathConstants.NODE );
			rootNode = (CmdTreeRootNode)CmdTreeNode.recursiveCreateNode( xmlNodeRoot,null,null );
		}
		return rootNode;
	}

	private static void recursivePrintCmdTreeNode( CmdTreeNode ctnode,String prefix )
	{
//		mélységi bejárás szinteket jelző prefix kezeléssel
		System.out.println( String.format( "%snode(%s)",prefix,ctnode ) );

		if ( ctnode.getChildCTNode()!=null )
			recursivePrintCmdTreeNode( ctnode.getChildCTNode(),prefix + "\t" );

		if ( ctnode.getNextSiblingCTNode()!=null )
			recursivePrintCmdTreeNode( ctnode.getNextSiblingCTNode(),prefix );
	}

	private static LinkedList<CmdTreeParsePosition> listPositions = null;
	private static LinkedList<CmdTreeParsePosition> listTabPositions = null;
	private static List<CmdTreeParseTabChoices> listTabChoices = null;
	private static List<CmdTreeNode> listPossibleNextWords = null;
	private static CmdTreeParsePosition lastMatchingPos = null;
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

		listTabChoices = new ArrayList<CmdTreeParseTabChoices>();
		listTabPositions = new LinkedList<CmdTreeParsePosition>();
		listPossibleNextWords = new ArrayList<CmdTreeNode>();

		{
			listPositions = new LinkedList<CmdTreeParsePosition>();
			CmdTreeParsePosition level = new CmdTreeParsePosition( cmd.toLowerCase(),nodeRoot,TYPE_MATCH.full );
			listPositions.add( level );
			recursiveSearchCmdTreeForMatch( "" );
		}

		lastMatchingPos = getLastMathcingPosition();
		if ( lastMatchingPos!=null )
		{
			if ( lastMatchingPos.getMatch()==TYPE_MATCH.partial )
			{
				addTabChoices( lastMatchingPos.getCTNode(),lastMatchingPos.getCmd() );
			}
			else
			{
				recursiveSearchCmdTreeForNextWord( nodeRoot,"" );
				for ( CmdTreeNode ctnode : listPossibleNextWords )
					addTabChoices( ctnode,lastMatchingPos.getCmd() );
			}
		}

		System.out.println( "---------result" );
		for ( CmdTreeParsePosition pos : listPositions )
		{
			String processedCmd = cmd.substring( 0,cmd.length()-pos.getCmd().length() );
			System.out.println( String.format( "node(%02d)type(%s)(processedCmd=%s)(remainingCmd=%s)(%s)",pos.getCTNode().indexNode,pos.getCTNode().type,processedCmd,pos.getCmd(),pos.getMatch().name() ) );
		}
		System.out.println( "---------tabPositions" );
		for ( CmdTreeParsePosition pos : listTabPositions )
		{
			System.out.println( String.format( "node(%02d)",pos.getCTNode().indexNode ) );
		}
		System.out.println( "---------tab choices" );
		for ( CmdTreeParseTabChoices item : listTabChoices )
		{
			System.out.println( String.format( "%s (%d)",item.getStrChoice(),item.getCtnode().indexNode ) );
		}
	}

	private static CmdTreeParsePosition getLastMathcingPosition()
	{
		CmdTreeParsePosition lastMatchingPos = null;
		for ( CmdTreeParsePosition pos : listPositions )
		{
			if ( pos.getMatch()==TYPE_MATCH.full || pos.getMatch()==TYPE_MATCH.partial )
				lastMatchingPos = pos;
		}
		return lastMatchingPos;
	}

	private static boolean bEndSignSearchCmdTreeForNextWord = false;
	private static void recursiveSearchCmdTreeForNextWord( CmdTreeNode ctNode,String prefix )
	{
		if ( bEndSignSearchCmdTreeForNextWord==true )
			return;

//		mélységi bejárás szinteket jelző prefix kezeléssel
		System.out.println( String.format( "%snode(%s)",prefix,ctNode ) );
		boolean bSearchForNextWord = ( ctNode.indexNode>lastMatchingPos.getCTNode().indexNode );

		if ( bSearchForNextWord==true )
		{
			if ( checkNextWord( ctNode )==true )
				listPossibleNextWords.add( ctNode );
		}

		if ( ctNode.getChildCTNode()!=null )
			recursiveSearchCmdTreeForNextWord( ctNode.getChildCTNode(),prefix + " " );

		if ( ctNode.getNextSiblingCTNode()!=null )
			recursiveSearchCmdTreeForNextWord( ctNode.getNextSiblingCTNode(),prefix );
	}

	private static boolean checkNextWord( CmdTreeNode ctNode )
	{
		String cmdSampleBase = lastMatchingPos.getCTNode().getCmdSample();
		String cmdSample = ctNode.getCmdSample();
		if ( cmdSample.startsWith( cmdSampleBase )==true )
		{
			int wordCountDiff = ( cmdSample.split( " " ).length - cmdSampleBase.split( " " ).length );
			if ( wordCountDiff==1 )
				return true;
		}
		return false;
	}

	private static void recursiveSetCmdSample( CmdTreeNode ctNode,String prefix )
	{
		System.out.println( String.format( "%srecursiveSetCmdSample (%02d)",prefix,ctNode.indexNode ) );

//		if ( posLast.getCTNode().indexNode>=2 )
//			System.out.println( "X" );
		CmdTreeNode childCTNode = ctNode.getChildCTNode();
		if ( childCTNode!=null )
		{
			ctNode = childCTNode;
			boolean bPrevNodeMatch = false;
			while ( true )
			{
				CmdTreeNode nextSiblingCTNode = ctNode.getNextSiblingCTNode();
				if ( nextSiblingCTNode==null )
					break;

				CmdTreeParsePosition nextPos = new CmdTreeParsePosition( matchResult.nextCmd,nextSiblingCTNode,matchResult.match );
				listPositions.add( nextPos );
				recursiveSetCmdSample( prefix + " " );

				if ( nextSiblingCTNode instanceof CmdTreeSeq )
				{
					CmdTreeSeq ctsNode = (CmdTreeSeq)nextSiblingCTNode;
					if ( ctsNode.getbCanBeEmpty()==false )
						break;
				}
				else if ( nextSiblingCTNode instanceof CmdTreeChoice )
				{
					if ( bPrevNodeMatch==true )
						break;
				}
				ctNode = nextSiblingCTNode;
				bPrevNodeMatch = ( matchResult.match!=TYPE_MATCH.noMatch );
			}
		}
	}

	private static void addNextTabChoices( CmdTreeNode ctNode,String cmd )
	{
//		azokat a szavakat kell összeszedni, amelyekkel folytatható lenne a cmd
//		indítok egy standard bejárást, amíg el nem érek a megadott node-ig.
//		ezután már keresem azokat a szavakat, amelyekkel folytatható lenne a cmd

//		a következő szó a cmdTree-ben a következő indexű node-on lehet
//		egészen addig vizsgálom a "következő indexű node"-okat, amíg menni tudok a következő szabályok szerint
//		egy ciklusban megyek a standard bejárás szerint (mélységi), ugyanis ez alapján vannak a node indexek is
//		ha nem tudok tovább menni, akkor megkeresem 
//		ez tuti nehéz: több eset van
//		- ha choice, akkor csak a child-ot járom be, amíg noMatch-ot kapok
//		ha seqText, akkor csak a nextSibling-et járom be, amíg noMatch-ot kapok
	}

	private static void recursiveSearchCmdTreeForMatch( String prefix )
	{
//		a listPositions utolsó eleméig megfelelő a parse tesz
//		a printCmdTreeNode mélységi bejárás szerint vizsgálom tovább a fát
//			először a child-okat vizsgálom, majd a testvéreket
		CmdTreeParsePosition posLast = listPositions.getLast();
		CmdTreeNode ctNode = posLast.getCTNode();
		System.out.println( String.format( "%srecursiveSearchCmdTreeForMatch (%02d,%s)",prefix,ctNode.indexNode,posLast.getCmd() ) );

//		if ( posLast.getCTNode().indexNode>=2 )
//			System.out.println( "X" );

		if ( posLast.getCmd().isEmpty()==false )
		{
			CmdTreeNode childCTNode = ctNode.getChildCTNode();
			if ( childCTNode!=null )
			{
				ctNode = childCTNode;
				boolean bPrevNodeMatch = false;
				while ( true )
				{
					CmdTreeNode nextSiblingCTNode = ctNode.getNextSiblingCTNode();
					if ( nextSiblingCTNode==null )
						break;

					posLast = listPositions.getLast();
					String cmd = posLast.getCmd();
					ObjectCTNodeMatch matchResult = nextSiblingCTNode.checkCTNode( cmd );
					if ( matchResult.match==TYPE_MATCH.noMatch )
					{
						if ( nextSiblingCTNode instanceof CmdTreeSeq )
						{
							CmdTreeSeq ctsNode = (CmdTreeSeq)nextSiblingCTNode;
							if ( ctsNode.getbCanBeEmpty()==false )
								break;
						}
						else if ( nextSiblingCTNode instanceof CmdTreeChoice )
						{
							if ( bPrevNodeMatch==true )
								break;
						}
					}
					else
					{
						CmdTreeParsePosition nextPos = new CmdTreeParsePosition( matchResult.nextCmd,nextSiblingCTNode,matchResult.match );
						listPositions.add( nextPos );
						recursiveSearchCmdTreeForMatch( prefix + " " );
					}
					ctNode = nextSiblingCTNode;
					bPrevNodeMatch = ( matchResult.match!=TYPE_MATCH.noMatch );
				}
			}
		}
	}

	private static void addTabChoices( CmdTreeNode ctnode,String cmd )
	{
		List<String> list = ctnode.addTabChoices( cmd );
		if ( list!=null && list.isEmpty()==false )
		{
			for ( String val : list )
			{
				listTabChoices.add( new CmdTreeParseTabChoices( val,ctnode ) );
			}
		}
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
