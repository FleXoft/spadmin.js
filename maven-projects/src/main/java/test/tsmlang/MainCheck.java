package test.tsmlang;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
		List<CmdTreeNode> cmdTree = loadXml();

		checkInput( cmdTree,"  q   n no" );

//		createTsmCommandQueryNode();
//
//		checkInput( args[0] );
//		handleTab( args[0] );
	}

	private static List<CmdTreeNode> loadXml() throws Exception
	{
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
		return cmdTree;
	}

	private static void checkInput( List<CmdTreeNode> cmdTree,String cmd )
	{
		String cmd1 = skipFirstSpaces( cmd );
		LinkedList<CmdTreeNode> cmdTreeNodes = new LinkedList<CmdTreeNode>();
		String cmd2 = getFirstCmdTreeNode( cmd1,cmdTree,cmdTreeNodes );
		if ( cmd2==null )
		{
			System.out.println( "hiba" );
		}
		else
		{
			while ( true )
			{
				String cmd3 = skipFirstSpaces( cmd2 );
				String cmd4 = getNextCmdTreeNode( cmd3,cmdTreeNodes );
				if ( cmd4==null )
				{
					System.out.println( "hiba" );
					break;
				}
				else
				{
					cmd2 = cmd4;
				}
			}
		}
	}

	private static String skipFirstSpaces( String cmd )
	{
		int ic;
		for ( ic=0; Character.isWhitespace( cmd.charAt( ic ) )==true && ic<cmd.length(); ic++ );
		return cmd.substring( ic );
	}

	private static String getNextCmdTreeNode( String cmd2,LinkedList<CmdTreeNode> cmdTreeNodes )
	{
		System.out.println( String.format( "cmd2(%s)",cmd2 ) );

		CmdTreeNode node = cmdTreeNodes.getLast();
		String cmd3 = getFirstCmdTreeNode( cmd2,node.listSubNodes,cmdTreeNodes );

		System.out.println( String.format( "cmd3(%s)",cmd3 ) );
		return cmd3;
	}

	private static String getFirstCmdTreeNode( String cmd1,List<CmdTreeNode> cmdTree,LinkedList<CmdTreeNode> cmdTreeNodes )
	{
		String cmd2 = null;
		System.out.println( String.format( "cmd1(%s)",cmd1 ) );

		if ( cmdTree.isEmpty()==false )
		{
			CmdTreeNode firstNode = cmdTree.get( 0 );
			if ( firstNode.type==NODE_TYPE.choice )
			{
				for ( CmdTreeNode ctnode : cmdTree )
				{
//					- FullText-ig egyezik, ekkor a fulltext utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem 
//					- különben
//						- ha FixPart-ig egyezik, ekkor a FixPart utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem
//						- különben ez node nem jó
					CmdTreeChoice ctcnode = (CmdTreeChoice)ctnode;
					if ( cmd1.startsWith( ctcnode.getFullText() )==true )
					{
						cmdTreeNodes.add( ctcnode );
						cmd2 = cmd1.substring( ctcnode.getFullText().length() );
						break;
					}
					else if ( cmd1.startsWith( ctcnode.getFixPart() )==true )
					{
						cmdTreeNodes.add( ctcnode );
						cmd2 = cmd1.substring( ctcnode.getFixPart().length() );
						break;
					}
				}
			}
			else
			{
				for ( CmdTreeNode ctnode : cmdTree )
				{
//					- FullText-ig egyezik, ekkor a fulltext utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem 
//					- különben
//						- ha FixPart-ig egyezik, ekkor a FixPart utáni első karaktertől adom vissza a string-et, plusz ezt a node-ot találtnak veszem
//						- különben ez node nem jó
					CmdTreeSeq ctsnode = (CmdTreeSeq)ctnode;
					if ( ctsnode.type==NODE_TYPE.seqText )
					{
						CmdTreeSeqText ctstnode = (CmdTreeSeqText)ctnode;
						if ( cmd1.startsWith( ctstnode.getFullText() )==true )
						{
							cmdTreeNodes.add( ctstnode );
							cmd2 = cmd1.substring( ctstnode.getFullText().length() );
							break;
						}
						else if ( cmd1.startsWith( ctstnode.getFixPart() )==true )
						{
							cmdTreeNodes.add( ctstnode );
							cmd2 = cmd1.substring( ctstnode.getFixPart().length() );
							break;
						}
					}
					else if ( ctsnode.type==NODE_TYPE.seqSub )
					{
						CmdTreeSeqSub ctssnode = (CmdTreeSeqSub)ctnode;
						ctssnode.getListSubNodes();
					}

					if ( ctsnode.getbCanBeEmpty()==false )
						break;
				}
			}
		}
		

		System.out.println( String.format( "cmd2(%s)",cmd2 ) );
		return cmd2;
	}

	private static void handleTab( String cmd )
	{
		// TODO Auto-generated method stub
	}

	private static void createTsmCommandQueryNode()
	{
		TsmCommandItemSequence objQueryNode = new TsmCommandItemSequence();
		int indexMainItems = 0;
		int indexItems1 = 0;
		int indexItems2 = 0;
		TsmCommandItem ci;

//		------------------
		ci = new TsmStringToken( indexMainItems++,"q","query" );
		objQueryNode.listCommandItems.add( ci );

//		------------------
		ci = new TsmStringToken( indexMainItems++,"n","node" );
		objQueryNode.listCommandItems.add( ci );

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmItemFromFixList( indexItems1++,Arrays.asList( new String[] { "node1","node2" } ) );
			seq1.listCommandItems.add( ci );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"do","domain" );
			seq1.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"=","=" );
			seq1.listCommandItems.add( ci );

			indexItems2 = 0;
			ci = new TsmItemFromFixList( indexItems2++,Arrays.asList( new String[] { "domain1","domain2" } ) );
			TsmListSeparatedItems list1 = new TsmListSeparatedItems( indexItems1++,",",ci );
			seq1.listCommandItems.add( list1 );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"f","format" );
			seq1.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"=","=" );
			seq1.listCommandItems.add( ci );

			indexItems2 = 0;
			TsmCommandItemSequence seq2 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"s","standard" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"d","detailed" );
			seq2.listCommandItems.add( ci );

			TsmChoice ch1 = new TsmChoice( indexItems1++,false,seq2 );
			seq1.listCommandItems.add( ch1 );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"auth","authentication" );
			seq1.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"=","=" );
			seq1.listCommandItems.add( ci );

			indexItems2 = 0;
			TsmCommandItemSequence seq2 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"lo","local" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"ld","ldap" );
			seq2.listCommandItems.add( ci );

			TsmChoice ch1 = new TsmChoice( indexItems1++,false,seq2 );
			seq1.listCommandItems.add( ch1 );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"t","type" );
			seq1.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"=","=" );
			seq1.listCommandItems.add( ci );

			indexItems2 = 0;
			TsmCommandItemSequence seq2 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"c","client" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"n","nas" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"s","server" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"a","any" );
			seq2.listCommandItems.add( ci );

			TsmChoice ch1 = new TsmChoice( indexItems1++,false,seq2 );
			seq1.listCommandItems.add( ch1 );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}
	}
}
