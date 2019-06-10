package test.tsmlang;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import test.tsmlang.cmdtreenodes.CmdTreeLevelStart;
import test.tsmlang.cmdtreenodes.CmdTreeNode;
import test.tsmlang.cmdtreenodes.CmdTreeNode.WORD_TYPE;
import test.tsmlang.cmdtreenodes.choice.CmdTreeChoice;
import test.tsmlang.cmdtreenodes.seq.CmdTreeSeq;
import test.tsmlang.cmdtreenodes.seq.CmdTreeSeqSub;

public class CalcNextPossibleWords
{
	private static final Logger logger = Logger.getLogger( CalcNextPossibleWords.class );

//	ciklusban végigmegyeg a cmdTree-n a mélységi bejárás szerint
//		ciklus változó: aktuális node
//			a mélységi bejárás szerint veszem a következő elemet (listában a következő elem)
//			ha nincs ilyen, akkor kilépek
//		ciklus mag:
//			ha az "aktuális node" tartalmazhat szót ÉS lehet-e ez a "következő szó", akkor
//				feljegyzem az eredmény listába és 
//				"aktuális node" új értékét kiszámolom:
//					megkeresem a következő lehetséges szót tartalmazó node-ot
//					kihagyom az összes olyan node-ot, amely csak ezután a szó után jöhet
//			különben
//				"aktuális node" új értékét kiszámolom:
//					a mélységi bejárás szerint veszem a következő elemet (listában a következő elem)
//					ha nincs ilyen, akkor kilépek
	public static List<CmdTreeNode> calc( CmdTreeNode paramLastMatchingNode )
	{
		logger.debug( String.format( "lastMatchingNode=(%02d)",paramLastMatchingNode.getIndexNode() ) );

		List<CmdTreeNode> result = new ArrayList<CmdTreeNode>();

		CmdTreeNode ctNodeCurrent = MainCheck.safeGetNextCmdTreeNode( paramLastMatchingNode );
		while ( true )
		{
			if ( ctNodeCurrent==null )
				break;

			boolean bNoWord = ( ctNodeCurrent.getWordType()==WORD_TYPE.noWord );
			boolean bPossibleNextWord = isPossibleNextWord( paramLastMatchingNode,ctNodeCurrent );
			if ( bNoWord==true || bPossibleNextWord==false )
			{
				ctNodeCurrent = MainCheck.safeGetNextCmdTreeNode( ctNodeCurrent );
			}
			else
			{
				result.add( ctNodeCurrent );
				ctNodeCurrent = getNextCurrentNode( ctNodeCurrent );
			}
		}

		return result;
	}

	private static CmdTreeNode getNextCurrentNode( CmdTreeNode ctNodeCurrent )
	{
		CmdTreeNode ctNodeNext = null;
		if ( ctNodeCurrent.getWordType()==WORD_TYPE.choiceNecessary || 
			ctNodeCurrent.getWordType()==WORD_TYPE.seqCanBeEmpty )
		{
			CmdTreeNode prevParentNode = ctNodeCurrent;
			ctNodeNext = ctNodeCurrent.getNextSiblingCTNode();
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
		else if ( ctNodeCurrent.getWordType()==WORD_TYPE.seqNecessary )
		{
			CmdTreeNode prevParentNode = ctNodeCurrent;
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
			throw new RuntimeException( "impossible:" + ctNodeCurrent.getWordType() );

		return ctNodeNext;
	}

	private static boolean isPossibleNextWord( CmdTreeNode paramLastMatchingNode,CmdTreeNode ctNodeDestination )
	{
//		össze kell számolni, hogy a paramLastMatchingNode node utáni node-ok közül hány "kötelező" szó van a mélységi bejárás szerint amíg elérünk ctNodeCurrent-ig
//		ha már egyet találunk, akkor nem kell tovább számolni

		CmdTreeNode ctNodeNext = getNextRequiredWord( paramLastMatchingNode );
		if ( ctNodeNext==null )
			return true;
		else
			return ( ctNodeNext.getIndexNode()>ctNodeDestination.getIndexNode() );
	}

	private static CmdTreeNode getNextRequiredWord( CmdTreeNode paramLastMatchingNode )
	{
		CmdTreeNode ctNodeCurrent = MainCheck.safeGetNextCmdTreeNode( paramLastMatchingNode );
		while ( true )
		{
			if ( ctNodeCurrent==null )
				break;
			if ( ctNodeCurrent instanceof CmdTreeSeq )
			{
				CmdTreeSeq ctNode = (CmdTreeSeq)ctNodeCurrent;
				if ( ctNode.getbCanBeEmpty()==true )
				{
					CmdTreeNode prevParentNode = ctNodeCurrent;
					ctNodeCurrent = ctNodeCurrent.getNextSiblingCTNode();
					while ( true )
					{
						if ( ctNodeCurrent!=null )
							break;
						CmdTreeNode ctNode2 = prevParentNode.getParentCTNode();
						if ( ctNode2==null )
							break;
						prevParentNode = ctNode2;
						ctNodeCurrent = ctNode2.getNextSiblingCTNode();
					}
				}
				else
				{
					if ( ctNodeCurrent instanceof CmdTreeSeqSub )
					{
						CmdTreeNode ctNodeNext = ctNodeCurrent.getChildCTNode();
						if ( ctNodeNext==null )
							throw new RuntimeException( "empty sub node:" + ctNodeCurrent.getWordType() );
						ctNodeCurrent = ctNodeNext; 
					}
					else
						return ctNodeCurrent;
				}
			}
			else if ( ctNodeCurrent instanceof CmdTreeChoice )
			{
				return ctNodeCurrent;
			}
			else if ( ctNodeCurrent instanceof CmdTreeLevelStart )
			{
				CmdTreeNode ctNodeNext = MainCheck.safeGetNextCmdTreeNode( ctNodeCurrent );
				if ( ctNodeNext==null )
					throw new RuntimeException( "empty levelstart node:" + ctNodeCurrent.getWordType() );
				ctNodeCurrent = ctNodeNext;
			}
//			if ( ctNodeCurrent)
//			if ( ctNodeCurrent.getWordType()==WORD_TYPE.noWord )
//			{
//				ctNodeCurrent = MainCheck.safeGetNextCmdTreeNode( ctNodeCurrent );
//			}
//			else
//			{
//				ctNodeCurrent = getNextCurrentNode( ctNodeCurrent );
//			}
		}
		return null;

//		egyszerű eset, közvetlen leszármazás
//		if ( ctNodeCurrent.getCmdSample().startsWith( paramLastMatchingNode.getCmdSample() )==true )
//		{
//			paramLastMatchingNode
//			return true;
//		}

//		 bonyolult következés, pl:
//			"query node format=Standard" cmd után "AUTHentication" node
//			node(18)(00 01 02 03 04 05 06 07 12 13 14 15 16 17 18)
//			node(22)(00 01 02 03 04 05 06 07 12 20 21 22)
//			közös node 12, ami bCanBeEmpty=true
//			de pl 
//			node(38)(00 01 02 03 04 38)
//			közös node 04, ami choiceText, ezért hibás
//		CmdTreeNode lastCommonNode = getLastCommonNode( paramLastMatchingNode,ctNodeCurrent );
//		List<CmdTreeNode> listCTNodesFromCommonNode = getListCTNodesFromCommonNode( paramLastMatchingNode,lastCommonNode );
//		if ( lastCommonNode instanceof CmdTreeRootNode )
//			return true;
//		if ( lastCommonNode instanceof CmdTreeChoice )
//		{
//			
//		}
//		if ( lastCommonNode instanceof CmdTreeSeq )
//		{
//			CmdTreeSeq ctNode = (CmdTreeSeq)lastCommonNode;
//			if ( ctNode.getbCanBeEmpty()==true )
//				return true;
//		}
//		return false;
	}

	private static List<CmdTreeNode> getListCTNodesFromCommonNode( CmdTreeNode ctNode2,CmdTreeNode ctNodeCommon )
	{
		List<CmdTreeNode> result = new ArrayList<CmdTreeNode>();

		String[] list = ctNode2.getCmdSample().split( " " );
		boolean bNodeToList = false;
		for ( String strIndex : list )
		{
			int indexNode = Integer.parseInt( strIndex );
			if ( indexNode==ctNodeCommon.getIndexNode() )
			{
				bNodeToList = true;
			}
			else
			{
				if ( bNodeToList==true )
					result.add( MainCheck.listCmdTreeNodes.get( indexNode ) );
			}
		}
		return result;
	}

	private static CmdTreeNode getLastCommonNode( CmdTreeNode ctNode,CmdTreeNode ctNode2 )
	{
		int indexFirstDiff = 0;
		for ( ; true; indexFirstDiff++ )
		{
			if ( ctNode.getCmdSample().length()<=indexFirstDiff )
				break;
			if ( ctNode2.getCmdSample().length()<=indexFirstDiff )
				break;
			if ( ctNode.getCmdSample().charAt( indexFirstDiff )!=ctNode2.getCmdSample().charAt( indexFirstDiff ) )
				break;
		}
		String strCommon = ctNode.getCmdSample().substring( 0,indexFirstDiff );
		String[] list = strCommon.split( " " );
		String strLastCommonIndex = null;
		if ( list.length>0 )
			strLastCommonIndex = list[list.length-1];
		int index = Integer.parseInt( strLastCommonIndex );

//		logger.debug( String.format( "lastCommonIndex(%02d)",index ) );
		return MainCheck.listCmdTreeNodes.get( index );
	}
}
