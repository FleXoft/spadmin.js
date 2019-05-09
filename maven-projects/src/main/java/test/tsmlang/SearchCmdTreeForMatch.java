package test.tsmlang;

import java.util.LinkedList;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;

public class SearchCmdTreeForMatch
{
	private static LinkedList<CmdTreeParsePosition> listPositions = null;

	public static void init( CmdTreeRootNode nodeRoot,String cmd )
	{
		listPositions = new LinkedList<CmdTreeParsePosition>();
		CmdTreeParsePosition level = new CmdTreeParsePosition( cmd.toLowerCase(),nodeRoot,TYPE_MATCH.full );
		listPositions.add( level );
	}

	public static void recursiveSearchCmdTreeForMatch( String prefix )
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

	public static void printResults( String cmd )
	{
		System.out.println( "---------result" );
		for ( CmdTreeParsePosition pos : listPositions )
		{
			String processedCmd = cmd.substring( 0,cmd.length()-pos.getCmd().length() );
			System.out.println( String.format( "node(%02d)type(%s)(processedCmd=%s)(remainingCmd=%s)(%s)",pos.getCTNode().indexNode,pos.getCTNode().type,processedCmd,pos.getCmd(),pos.getMatch().name() ) );
		}
	}

	public static CmdTreeParsePosition getLastMathcingPosition()
	{
		CmdTreeParsePosition lastMatchingPos = null;
		for ( CmdTreeParsePosition pos : listPositions )
		{
			if ( pos.getMatch()==TYPE_MATCH.full || pos.getMatch()==TYPE_MATCH.partial )
				lastMatchingPos = pos;
		}
		return lastMatchingPos;
	}
}
