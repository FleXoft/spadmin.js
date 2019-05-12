package test.tsmlang;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import test.tsmlang.CmdTreeNode.NODE_TYPE;

public class SearchCmdTreeForNextWord
{
	public enum TYPE_RECURSION { writeContentThenTreeWalk };
	
	private static LinkedList<CmdTreeParsePosition> listTabPositions = null;
	private static List<CmdTreeParseTabChoices> listTabChoices = null;
	private static LinkedList<CmdTreeNode> listPossibleNextWords = null;
	private static CmdTreeParsePosition lastMatchingPos = null;
	private static CmdTreeNode currentCTNode = null;


	public static void search( CmdTreeParsePosition lastMatchingPos )
	{
		SearchCmdTreeForNextWord.lastMatchingPos = lastMatchingPos;

		listTabChoices = new ArrayList<CmdTreeParseTabChoices>();
		listTabPositions = new LinkedList<CmdTreeParsePosition>();
		listPossibleNextWords = new LinkedList<CmdTreeNode>();

//		végigmegyek az utsó matching node utáni node-tól a végéig
//		megnézem, hogyan tudok eljutni az aktuáis node-ból az utsó matching node-ig
//		ezt az utat vizsgálom meg, ez alapján döntök, hogy tab choice-e

		CmdTreeNode ctNode = lastMatchingPos.getCTNode();

		System.out.println( String.format( "lastMatchingNode=(%02d)",ctNode.indexNode ) );
		ListIterator<CmdTreeNode> listIterator = MainCheck.listCmdTreeNodes.listIterator( ctNode.indexNode );
		CmdTreeNode ctNode2 = listIterator.next();

		while ( listIterator.hasNext()==true )
		{
			ctNode2 = listIterator.next();
			System.out.println( String.format( "nextNode=(%02d)",ctNode2.indexNode ) );
			if ( ctNode2.bHasWord==false )
			{
//				listPossibleNextWords.add( ctNode2 );
//				continue;
			}

			CmdTreeNode ctNodeCommon = getLastCommonNode( ctNode,ctNode2 );
			break;
//			if ( listPossibleNextWords.isEmpty()==true )
//			{
//				listPossibleNextWords.add( ctNode2 );
//			}
//			else
//			{
//				CmdTreeNode ctNodeLast = listPossibleNextWords.getLast();
//				if ( ctNodeLast.nextSiblingCTNode.indexNode==ctNode2.indexNode )
//				{
//					if ( ctNodeLast.getType()==NODE_TYPE.choice &&
//						ctNode2.getType()==NODE_TYPE.choice )
//					{
//						listPossibleNextWords.add( ctNode2 );
//					}
//					if ( ctNodeLast instanceof CmdTreeSeq &&
//						ctNode2 instanceof CmdTreeSeq )
//					{
//						CmdTreeSeq ctNodeLastSeq = (CmdTreeSeq)ctNodeLast;
//						if ( ctNodeLastSeq.getbCanBeEmpty()==true )
//							listPossibleNextWords.add( ctNode2 );
//					}
//				}
//			}
		}

		for ( CmdTreeNode ctnode : SearchCmdTreeForNextWord.listPossibleNextWords )
			addTabChoices( ctnode,lastMatchingPos.getCmd() );
	}

	private static CmdTreeNode getLastCommonNode( CmdTreeNode ctNode,CmdTreeNode ctNode2 )
	{
		int indexFirstDiff = 0;
		for ( ; true; indexFirstDiff++ )
		{
			if ( ctNode.cmdSample.length()<=indexFirstDiff )
				break;
			if ( ctNode2.cmdSample.length()<=indexFirstDiff )
				break;
			if ( ctNode.cmdSample.charAt( indexFirstDiff )!=ctNode2.cmdSample.charAt( indexFirstDiff ) )
				break;
		}
		String strCommon = ctNode.cmdSample.substring( 0,indexFirstDiff );
		String[] list = strCommon.split( " " );
		String strLastCommonIndex = null;
		if ( list.length>0 )
			strLastCommonIndex = list[list.length-1];
		int index = Integer.parseInt( strLastCommonIndex );

		System.out.println( String.format( "lastCommonIndex(%02d)",index ) );
		return null;
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

	public static void addTabChoices( CmdTreeNode ctnode,String cmd )
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

	public static void printResults( String cmd )
	{
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

//	public static void main( String[] args )
//	{
//		LinkedList<String> ll = new LinkedList<String>();
//		ll.addLast( "0" );
//		ll.addLast( "1" );
//		ll.addLast( "2" );
//		ll.addLast( "3" );
//		ListIterator<String> listIterator = ll.listIterator( 2 );
//		for ( int ic=0; listIterator.hasNext()==true; ic++ )
//		{
//			String next = listIterator.next();
//			System.out.println( String.format( "%d,%s",ic,next ) );
//		}
//	}
}
//private static void level0()
//{
//	while ( true )
//	{
//		if ( ctNode.childCTNode!=null )
//		{
//			currentCTNode = ctNode.childCTNode;
//			listPossibleNextWords.add( currentCTNode );
//			getNextWordOnly( 1,currentCTNode );
//		}
//
//		if ( ctNode.getType()==NODE_TYPE.choice )
//		{
//			// nem nézem tovább
//		}
//		else if ( ctNode instanceof CmdTreeSeq )
//		{
//			CmdTreeSeq ctNodeSeq = (CmdTreeSeq)ctNode;
//			ctNodeSeq = (CmdTreeSeq)ctNodeSeq.nextSiblingCTNode;
//			if ( ctNodeSeq!=null )
//				getNextWordOnly( 2,ctNodeSeq );
////			if ( ctNodeSeq.getbCanBeEmpty()==false )
////				break;
//		}
//
//		while ( true )
//		{
//			ctNode = ctNode.parentCTNode;
//			if ( ctNode==null )
//				break;
//			if ( ctNode.nextSiblingCTNode!=null )
//			{
//				ctNode = ctNode.nextSiblingCTNode;
//				break;
//			}
//		}
//		if ( ctNode==null )
//			break;
//	}
//}

//private static void getNextWordOnly( int callIndex,CmdTreeNode ctNode )
//{
//	System.out.println( String.format( "---getNextWordOnly(%d) called on node(%02d)",callIndex,ctNode.indexNode ) );
//	for ( CmdTreeNode ctn : SearchCmdTreeForNextWord.listPossibleNextWords )
//		System.out.println( String.format( "   listPossibleNextWords node(%02d)",ctn.indexNode ) );
//
//	if ( ctNode.getType()==NODE_TYPE.levelStart )
//		ctNode = ctNode.nextSiblingCTNode;
//
//	if ( ctNode.getType()==NODE_TYPE.choice )
//	{
//		if ( ctNode.childCTNode!=null )
//			getNextWordOnly( 3,ctNode.childCTNode );
//
//		while ( ctNode!=null )
//		{
//			listPossibleNextWords.add( ctNode );
//			ctNode = ctNode.nextSiblingCTNode;
//		}
//	}
//	else if ( ctNode instanceof CmdTreeSeq )
//	{
//		CmdTreeSeq ctNodeSeq = (CmdTreeSeq)ctNode;
//		while ( true )
//		{
//			if ( ctNodeSeq.getbHasWord()==true )
//				listPossibleNextWords.add( ctNode );
//			if ( ctNodeSeq instanceof CmdTreeSeqSub )
//				getNextWordOnly( 4,ctNodeSeq.childCTNode );
//			if ( ctNodeSeq.getbCanBeEmpty()==false )
//				break;
//
//			ctNodeSeq = (CmdTreeSeq)ctNodeSeq.nextSiblingCTNode;
//			if ( ctNodeSeq==null )
//				break;
//		}
//	}
//}

//private static boolean bEndSignSearchCmdTreeForNextWord = false;
//private static void recursiveSearchCmdTreeForNextWord( CmdTreeNode ctNode,String prefix )
//{
//	if ( bEndSignSearchCmdTreeForNextWord==true )
//		return;
//
////	mélységi bejárás szinteket jelző prefix kezeléssel
//	System.out.println( String.format( "%snode(%s)",prefix,ctNode ) );
//	boolean bSearchForNextWord = ( ctNode.indexNode>lastMatchingPos.getCTNode().indexNode );
//
//	if ( bSearchForNextWord==true )
//	{
//		if ( checkNextWord( ctNode )==true )
//			listPossibleNextWords.add( ctNode );
//	}
//
//	if ( ctNode.getChildCTNode()!=null )
//		recursiveSearchCmdTreeForNextWord( ctNode.getChildCTNode(),prefix + " " );
//
//	if ( ctNode.getNextSiblingCTNode()!=null )
//		recursiveSearchCmdTreeForNextWord( ctNode.getNextSiblingCTNode(),prefix );
//}
//
//private static boolean checkNextWord( CmdTreeNode ctNode )
//{
//	String cmdSampleBase = lastMatchingPos.getCTNode().getCmdSample();
//	String cmdSample = ctNode.getCmdSample();
//	if ( cmdSample.startsWith( cmdSampleBase )==true )
//	{
//		int wordCountDiff = ( cmdSample.split( " " ).length - cmdSampleBase.split( " " ).length );
//		if ( wordCountDiff==1 )
//			return true;
//	}
//	return false;
//}
//
//private static void recursiveSetCmdSample( CmdTreeNode ctNode,String prefix )
//{
//	System.out.println( String.format( "%srecursiveSetCmdSample (%02d)",prefix,ctNode.indexNode ) );
//
////	if ( posLast.getCTNode().indexNode>=2 )
////		System.out.println( "X" );
//	CmdTreeNode childCTNode = ctNode.getChildCTNode();
//	if ( childCTNode!=null )
//	{
//		ctNode = childCTNode;
//		boolean bPrevNodeMatch = false;
//		while ( true )
//		{
//			CmdTreeNode nextSiblingCTNode = ctNode.getNextSiblingCTNode();
//			if ( nextSiblingCTNode==null )
//				break;
//
//			CmdTreeParsePosition nextPos = new CmdTreeParsePosition( matchResult.nextCmd,nextSiblingCTNode,matchResult.match );
//			listPositions.add( nextPos );
//			recursiveSetCmdSample( prefix + " " );
//
//			if ( nextSiblingCTNode instanceof CmdTreeSeq )
//			{
//				CmdTreeSeq ctsNode = (CmdTreeSeq)nextSiblingCTNode;
//				if ( ctsNode.getbCanBeEmpty()==false )
//					break;
//			}
//			else if ( nextSiblingCTNode instanceof CmdTreeChoice )
//			{
//				if ( bPrevNodeMatch==true )
//					break;
//			}
//			ctNode = nextSiblingCTNode;
//			bPrevNodeMatch = ( matchResult.match!=TYPE_MATCH.noMatch );
//		}
//	}
//}
