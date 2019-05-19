package test.tsmlang;

import org.apache.log4j.Logger;

public class PrintCmdTree
{
	private static final Logger logger = Logger.getLogger( PrintCmdTree.class );

	public static void recursivePrintCmdTreeNode( CmdTreeNode ctnode,String prefix )
	{
//		mélységi bejárás szinteket jelző prefix kezeléssel
		logger.debug( String.format( "%snode(%s)",prefix,ctnode ) );

		if ( ctnode.getChildCTNode()!=null )
			recursivePrintCmdTreeNode( ctnode.getChildCTNode(),prefix + "\t" );

		if ( ctnode.getNextSiblingCTNode()!=null )
			recursivePrintCmdTreeNode( ctnode.getNextSiblingCTNode(),prefix );
	}
}
