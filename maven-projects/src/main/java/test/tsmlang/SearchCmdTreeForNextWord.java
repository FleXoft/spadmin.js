package test.tsmlang;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import test.tsmlang.cmdtreenodes.CmdTreeNode;
import test.tsmlang.cmdtreenodes.CmdTreeNode.WORD_TYPE;
import test.tsmlang.cmdtreenodes.seq.CmdTreeSeq;

public class SearchCmdTreeForNextWord
{
	private static final Logger logger = Logger.getLogger( SearchCmdTreeForNextWord.class );

	private static LinkedList<CmdTreeNode> listPossibleNextWords = null;


	public static void search( CmdTreeParsePosition lastMatchingPos )
	{
		listPossibleNextWords = new LinkedList<CmdTreeNode>();

		CmdTreeNode ctNodeLastMatching = lastMatchingPos.getCTNode();
		logger.debug( String.format( "lastMatchingNode=(%02d)",ctNodeLastMatching.getIndexNode() ) );

		CmdTreeNode ctNodeLastMatchingChild = ctNodeLastMatching.getChildCTNode();
		if ( ctNodeLastMatchingChild!=null ) 
			logger.debug( String.format( "lastMatchingNode child=(%02d)",ctNodeLastMatchingChild.getIndexNode() ) );

		int nextIndex = ctNodeLastMatching.getIndexNode()+1;
		if ( nextIndex<MainCheck.listCmdTreeNodes.size() )
		{
			CmdTreeNode ctNodeBase = MainCheck.listCmdTreeNodes.get( nextIndex );
			logger.debug( String.format( "ctNodeBase=(%02d)",ctNodeBase.getIndexNode() ) );

			boolean bContinue = false;
			if ( (ctNodeLastMatchingChild==null || ctNodeBase.getCmdSample().startsWith( ctNodeLastMatchingChild.getCmdSample() )==false) &&
				(ctNodeBase instanceof CmdTreeSeq) )
				bContinue = true;
			// benne vagyunk-e a ctNodeLastMatching node-ból induló fában
			if ( (ctNodeLastMatchingChild!=null && ctNodeBase.getCmdSample().startsWith( ctNodeLastMatchingChild.getCmdSample() )==true ) )
				bContinue = true;

			if ( bContinue==true )
			{
				CmdTreeNode ctNode = ctNodeBase;
				while ( true )
				{
					// benne vagyunk-e a ctNodeBase node-ból induló fában
					if ( ctNode.getCmdSample().startsWith( ctNodeBase.getCmdSample() )==false )
						break;

					if ( ctNode.getbHasWord()==true )
					{
						listPossibleNextWords.add( ctNode );

						CmdTreeNode ctNodeNext = null;
						if ( ctNode.getWordType()==WORD_TYPE.choiceNecessary || 
							ctNode.getWordType()==WORD_TYPE.seqCanBeEmpty )
						{
							CmdTreeNode prevParentNode = ctNode;
							ctNodeNext = ctNode.getNextSiblingCTNode();
							while ( true )
							{
								if ( ctNodeNext!=null )
									break;
								CmdTreeNode ctNode2 = prevParentNode.getParentCTNode();
								if ( ctNode2==null )
									break;
								prevParentNode = ctNode2;
								ctNodeNext = ctNode2.getNextSiblingCTNode();
							}
						}
						else if ( ctNode.getWordType()==WORD_TYPE.seqNecessary )
						{
							CmdTreeNode prevParentNode = ctNode;
							while ( true )
							{
								CmdTreeNode ctNode2 = prevParentNode.getParentCTNode();
								if ( ctNode2==null )
									break;
								prevParentNode = ctNode2;
								ctNodeNext = ctNode2.getNextSiblingCTNode();
								if ( ctNodeNext!=null )
									break;
							}
						}
						else
							throw new RuntimeException( "impossible:" + ctNode.getWordType() );

						if ( ctNodeNext==null )
							break;
						ctNode = ctNodeNext;
					}
					else
					{
//						folytatom a mélységi bejárást
						nextIndex = ctNode.getIndexNode()+1;
						if ( nextIndex>=MainCheck.listCmdTreeNodes.size() )
							break;
						ctNode = MainCheck.listCmdTreeNodes.get( nextIndex );
					}
				}
			}
		}

		logger.debug( "---------listPossibleNextWords" );
		for ( CmdTreeNode ctnode : SearchCmdTreeForNextWord.listPossibleNextWords )
		{
			logger.debug( String.format( " node(%02d)",ctnode.getIndexNode() ) );
		}
		for ( CmdTreeNode ctnode : SearchCmdTreeForNextWord.listPossibleNextWords )
		{
			ObjectTabChoices.addTabChoices( ctnode,lastMatchingPos.getCmd() );
		}
	}


	public static void printResults( String cmd )
	{
//		logger.debug( "---------tabPositions" );
//		for ( CmdTreeParsePosition pos : listTabPositions )
//		{
//			logger.debug( String.format( "node(%02d)",pos.getCTNode().getIndexNode() ) );
//		}
		logger.debug( "---------tab choices" );
		for ( CmdTreeParseTabChoices item : ObjectTabChoices.listTabChoices )
		{
			logger.debug( String.format( "%s (%d)",item.getStrChoice(),item.getCtnode().getIndexNode() ) );
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
//			logger.debug( String.format( "%d,%s",ic,next ) );
//		}
//	}
}
