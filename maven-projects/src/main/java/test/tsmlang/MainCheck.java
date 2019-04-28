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
import org.w3c.dom.NodeList;

import test.tsmlang.CmdTreeNode.NODE_TYPE;

public class MainCheck
{
	private static final String COMMAND_TREE_XML = "command-tree1.xml";

	public static void main( String[] args ) throws Exception
	{
		CmdTreeRootNode nodeRoot = loadXml();

//		checkInput( cmdTree,"  q   n no" );
		checkInput( nodeRoot,"  q   n do=domain1" );

//		checkInput( args[0] );
//		handleTab( args[0] );
	}

	public static int indexNode = 0;
	private static CmdTreeRootNode loadXml() throws Exception
	{
//		egy olyan fát készítek, ahol 1 node-ból megmondható 
//		- a parent
//		- a child
//		- a szinten következő node
//		ez azért kell, hogy folytathassam a parse-olást, bármelyik node-ról, tudjak bármerre mozogni

		CmdTreeRootNode rootNode = new CmdTreeRootNode( null,NODE_TYPE.root,null );
		List<CmdTreeNode> cmdTree = new ArrayList<CmdTreeNode>();
		try ( FileInputStream fileIS = new FileInputStream( COMMAND_TREE_XML ) )
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse( fileIS );
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "/cmdtree/choice";
			NodeList nodeList1 = (NodeList)xPath.compile( expression ).evaluate( xmlDocument,XPathConstants.NODESET );
			for ( int ic=0; ic<nodeList1.getLength(); ic++ )
			{
				System.out.println( String.format( "main nodelist(%d)",ic ) );
				Node node = nodeList1.item( ic );
				short nodeType = node.getNodeType();
				if ( nodeType==Node.ELEMENT_NODE )
				{
					cmdTree.add( CmdTreeNode.createNode( node ) );
				}
				else
				{
					System.out.println( String.format( "node(%s) tpye(%d)",node.getNodeName(),nodeType ) );
				}
			}
		}
		return new CmdTreeRootNode( cmdTree );
	}

	private static LinkedList<CmdTreeParsePosition> listLevels = null;
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

		listLevels = new LinkedList<CmdTreeParsePosition>();
		String cmd1 = skipFirstSpaces( cmd );
		CmdTreeParsePosition level = new CmdTreeParsePosition( cmd1,nodeRoot );
		listLevels.add( level );

		iterateOnLevel();

		System.out.println( "---------result" );
		for ( CmdTreeParsePosition level1 : listLevels )
		{
			if ( level1.getParentNode()==null )
				System.out.println( String.format( "root(%s)",level1.getCmd() ) );
			else
				System.out.println( String.format( "nodetype(%s)(%s)",level1.getParentNode().type,level1.getCmd() ) );
		}

//		LinkedList<CmdTreeNode> cmdTreeNodes = new LinkedList<CmdTreeNode>();
//		String cmd2 = getFirstCmdTreeNode( cmd1,cmdTree,cmdTreeNodes );
//		if ( cmd2==null )
//		{
//			System.out.println( "hiba" );
//		}
//		else
//		{
//			while ( true )
//			{
//				String cmd3 = skipFirstSpaces( cmd2 );
//				String cmd4 = getNextCmdTreeNode( cmd3,cmdTreeNodes );
//				if ( cmd4==null )
//				{
//					System.out.println( "hiba" );
//					break;
//				}
//				else
//				{
//					cmd2 = cmd4;
//				}
//			}
//		}
	}

	private static void iterateOnLevel()
	{
		CmdTreeParsePosition level = listLevels.getLast();
		System.out.println( String.format( "iterateOnLevel (%s)",level.getCmd() ) );

		List<CmdTreeNode> list = level.getCmdTree();
		CmdTreeNode node0 = list.get( 0 );
		boolean bPartialMatch;
		if ( node0.type==NODE_TYPE.choice )
			bPartialMatch = iterateOnChoices( list );
		else
			bPartialMatch = iterateOnSeqs( list );
		level.setbPartialMatch( bPartialMatch );
	}

	private static boolean iterateOnSeqs( List<CmdTreeNode> list )
	{
		boolean bResult = false;
		for ( CmdTreeNode ctnode : list )
		{
//			- FullText-ig egyezik, ekkor a fulltext utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem 
//			- különben
//				- ha FixPart-ig egyezik, ekkor a FixPart utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem
//				- különben ez node nem jó
			String cmd = listLevels.getLast().getCmd();
			CmdTreeSeq ctsnode = (CmdTreeSeq)ctnode;
			if ( ctsnode.type==NODE_TYPE.seqText )
			{
				CmdTreeSeqText ctstnode = (CmdTreeSeqText)ctnode;
				if ( cmd.startsWith( ctstnode.getFullText() )==true )
				{
					String cmd2 = cmd.substring( ctstnode.getFullText().length() );
					addNewLevelWithPartialMatch( cmd2,ctstnode );
					bResult = true;
				}
				else if ( cmd.startsWith( ctstnode.getFixPart() )==true )
				{
					String cmd2 = cmd.substring( ctstnode.getFixPart().length() );
					addNewLevelWithPartialMatch( cmd2,ctstnode );
					cmd = cmd2;
					bResult = true;
				}
			}
			else if ( ctsnode.type==NODE_TYPE.seqSub )
			{
				CmdTreeSeqSub ctssnode = (CmdTreeSeqSub)ctnode;
				CmdTreeParsePosition levelChild = addNewLevel( cmd,ctssnode );
				if ( levelChild==null )
					throw new RuntimeException( "invalid xml:seqSub with no children" );
				cmd = listLevels.getLast().getCmd();
				bResult = levelChild.isbPartialMatch();
			}
			else if ( ctsnode.type==NODE_TYPE.seqList )
			{
				CmdTreeSeqList ctslnode = (CmdTreeSeqList)ctnode;
				String strFound = null;
				for ( String value : ctslnode.getListValues() )
				{
					if ( cmd.equalsIgnoreCase( value )==true )
					{
						strFound = value;
						break;
					}
				}
				if ( strFound!=null )
				{
					String cmd2 = cmd.substring( strFound.length() );
					addNewLevelWithPartialMatch( cmd2,ctslnode );
					bResult = true;
				}
			}

			if ( ctsnode.getbCanBeEmpty()==false )
				break;
		}
		return bResult;
	}

	private static boolean iterateOnChoices( List<CmdTreeNode> list )
	{
		boolean bResult = false;
		CmdTreeParsePosition level = listLevels.getLast();
		String cmd = level.getCmd();
		for ( CmdTreeNode ctnode : list )
		{
//			- FullText-ig egyezik, ekkor a fulltext utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem 
//			- különben
//				- ha FixPart-ig egyezik, ekkor a FixPart utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem
//				- különben ez node nem jó
			CmdTreeChoice ctcnode = (CmdTreeChoice)ctnode;
			if ( cmd.startsWith( ctcnode.getFullText() )==true )
			{
				String cmd2 = cmd.substring( ctcnode.getFullText().length() );
				addNewLevelWithPartialMatch( cmd2,ctcnode );
				bResult = true;
				break;
			}
			else if ( cmd.startsWith( ctcnode.getFixPart() )==true )
			{
				String cmd2 = cmd.substring( ctcnode.getFixPart().length() );
				addNewLevelWithPartialMatch( cmd2,ctcnode );
				bResult = true;
				break;
			}
		}
		return bResult;
	}

	private static CmdTreeParsePosition addNewLevel( String cmd2,CmdTreeNode ctnode )
	{
		return addNewLevel( cmd2,ctnode,false );
	}

	private static CmdTreeParsePosition addNewLevelWithPartialMatch( String cmd2,CmdTreeNode ctnode )
	{
		return addNewLevel( cmd2,ctnode,true );
	}

	private static CmdTreeParsePosition addNewLevel( String cmd2,CmdTreeNode ctnode,boolean bPartialMatch )
	{
		String cmd3 = skipFirstSpaces( cmd2 );
		CmdTreeParsePosition levelChild = new CmdTreeParsePosition( cmd3,ctnode,ctnode.listSubNodes );
		levelChild.setbPartialMatch( bPartialMatch );
		listLevels.add( levelChild );
		if ( cmd3.isEmpty()==false && ctnode.listSubNodes.isEmpty()==false )
			iterateOnLevel();
		return levelChild;
	}

	private static String skipFirstSpaces( String cmd )
	{
		int ic;
		for ( ic=0; Character.isWhitespace( cmd.charAt( ic ) )==true && ic<cmd.length(); ic++ );
		return cmd.substring( ic );
	}

//	private static String getNextCmdTreeNode( String cmd2,LinkedList<CmdTreeNode> cmdTreeNodes )
//	{
//		System.out.println( String.format( "cmd2(%s)",cmd2 ) );
//
//		CmdTreeNode node = cmdTreeNodes.getLast();
//		String cmd3 = getFirstCmdTreeNode( cmd2,node.listSubNodes,cmdTreeNodes );
//
//		System.out.println( String.format( "cmd3(%s)",cmd3 ) );
//		return cmd3;
//	}
//
//	private static String getFirstCmdTreeNode( String cmd1,List<CmdTreeNode> cmdTree,LinkedList<CmdTreeNode> cmdTreeNodes )
//	{
//		String cmd2 = null;
//		System.out.println( String.format( "cmd1(%s)",cmd1 ) );
//
//		if ( cmdTree.isEmpty()==false )
//		{
//			CmdTreeNode firstNode = cmdTree.get( 0 );
//			if ( firstNode.type==NODE_TYPE.choice )
//			{
//				for ( CmdTreeNode ctnode : cmdTree )
//				{
////					- FullText-ig egyezik, ekkor a fulltext utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem 
////					- különben
////						- ha FixPart-ig egyezik, ekkor a FixPart utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem
////						- különben ez node nem jó
//					CmdTreeChoice ctcnode = (CmdTreeChoice)ctnode;
//					if ( cmd1.startsWith( ctcnode.getFullText() )==true )
//					{
//						cmdTreeNodes.add( ctcnode );
//						cmd2 = cmd1.substring( ctcnode.getFullText().length() );
//						break;
//					}
//					else if ( cmd1.startsWith( ctcnode.getFixPart() )==true )
//					{
//						cmdTreeNodes.add( ctcnode );
//						cmd2 = cmd1.substring( ctcnode.getFixPart().length() );
//						break;
//					}
//				}
//			}
//			else
//			{
//				for ( CmdTreeNode ctnode : cmdTree )
//				{
////					- FullText-ig egyezik, ekkor a fulltext utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem 
////					- különben
////						- ha FixPart-ig egyezik, ekkor a FixPart utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem
////						- különben ez node nem jó
//					CmdTreeSeq ctsnode = (CmdTreeSeq)ctnode;
//					if ( ctsnode.type==NODE_TYPE.seqText )
//					{
//						CmdTreeSeqText ctstnode = (CmdTreeSeqText)ctnode;
//						if ( cmd1.startsWith( ctstnode.getFullText() )==true )
//						{
//							cmdTreeNodes.add( ctstnode );
//							cmd2 = cmd1.substring( ctstnode.getFullText().length() );
//							break;
//						}
//						else if ( cmd1.startsWith( ctstnode.getFixPart() )==true )
//						{
//							cmdTreeNodes.add( ctstnode );
//							cmd2 = cmd1.substring( ctstnode.getFixPart().length() );
//							break;
//						}
//					}
//					else if ( ctsnode.type==NODE_TYPE.seqSub )
//					{
//						CmdTreeSeqSub ctssnode = (CmdTreeSeqSub)ctnode;
//						ctssnode.getListSubNodes();
//					}
//
//					if ( ctsnode.getbCanBeEmpty()==false )
//						break;
//				}
//			}
//		}
//		
//
//		System.out.println( String.format( "cmd2(%s)",cmd2 ) );
//		return cmd2;
//	}

	private static void handleTab( String cmd )
	{
		// TODO Auto-generated method stub
	}
}
