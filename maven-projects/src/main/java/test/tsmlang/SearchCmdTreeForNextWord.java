package test.tsmlang;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import test.tsmlang.CmdTreeNode.NODE_TYPE;

public class SearchCmdTreeForNextWord
{
	private static LinkedList<CmdTreeParsePosition> listTabPositions = null;
	private static List<CmdTreeParseTabChoices> listTabChoices = null;
	private static List<CmdTreeNode> listPossibleNextWords = null;
	private static CmdTreeParsePosition lastMatchingPos = null;


	public static void search( CmdTreeParsePosition lastMatchingPos )
	{
		SearchCmdTreeForNextWord.lastMatchingPos = lastMatchingPos;

		listTabChoices = new ArrayList<CmdTreeParseTabChoices>();	
		listTabPositions = new LinkedList<CmdTreeParsePosition>();
		listPossibleNextWords = new ArrayList<CmdTreeNode>();

		CmdTreeNode lastMatchingNode = lastMatchingPos.getCTNode();
		ListIterator<CmdTreeNode> listIterator = MainCheck.listCmdTreeNodes.listIterator( lastMatchingPos.getCTNode().indexNode );
		while ( listIterator.hasNext()==true )
		{
			CmdTreeNode ctNode = listIterator.next();
			if ( lastMatchingNode.getType()==NODE_TYPE.choice )
				continue;
			else if ( lastMatchingNode instanceof CmdTreeSeq )
			{
				CmdTreeSeq ctNodeSeq = (CmdTreeSeq)ctNode;
				if ( ctNodeSeq.getbCanBeEmpty()==false )
				{
					getNextWordOnly( ctNodeSeq );
					break;
				}
			}
		}

		for ( CmdTreeNode ctnode : SearchCmdTreeForNextWord.listPossibleNextWords )
			addTabChoices( ctnode,lastMatchingPos.getCmd() );
	}

	private static void getNextWordOnly( CmdTreeNode ctNode )
	{
		if ( ctNode.getType()==NODE_TYPE.choice )
		{
			listPossibleNextWords.add( ctNode );
			if ( ctNode.nextSiblingCTNode!=null )
				getNextWordOnly( ctNode.nextSiblingCTNode );
		}
		else if ( ctNode instanceof CmdTreeSeq )
		{
			CmdTreeSeq ctNodeSeq = (CmdTreeSeq)ctNode;
			if ( ctNodeSeq.getbCanBeEmpty()==false )
			{
				getNextWordOnly( ctNodeSeq );
				XXX
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
}
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
