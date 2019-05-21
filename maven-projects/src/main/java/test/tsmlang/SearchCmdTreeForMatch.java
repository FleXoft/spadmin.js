package test.tsmlang;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;
import test.tsmlang.cmdtreenodes.CmdTreeNode;
import test.tsmlang.cmdtreenodes.CmdTreeRootNode;
import test.tsmlang.cmdtreenodes.choice.CmdTreeChoice;
import test.tsmlang.cmdtreenodes.seq.CmdTreeSeq;

public class SearchCmdTreeForMatch
{
	private static final Logger logger = Logger.getLogger( SearchCmdTreeForMatch.class );

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
		logger.debug( String.format( "%srecursiveSearchCmdTreeForMatch (%02d,%s)",prefix,ctNode.getIndexNode(),posLast.getCmd() ) );

//		if ( posLast.getCTNode().indexNode>=2 )
//			logger.debug( "X" );

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
		logger.debug( "---------result" );
		for ( CmdTreeParsePosition pos : listPositions )
		{
			String processedCmd = cmd.substring( 0,cmd.length()-pos.getCmd().length() );
			logger.debug( String.format( "node(%02d)type(%s)(processedCmd=%s)(remainingCmd=%s)(%s)",pos.getCTNode().getIndexNode(),pos.getCTNode().getType(),processedCmd,pos.getCmd(),pos.getMatch().name() ) );
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
