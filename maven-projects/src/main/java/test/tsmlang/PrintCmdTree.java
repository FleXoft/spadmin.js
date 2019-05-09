package test.tsmlang;

public class PrintCmdTree
{
	public static void recursivePrintCmdTreeNode( CmdTreeNode ctnode,String prefix )
	{
//		mélységi bejárás szinteket jelző prefix kezeléssel
		System.out.println( String.format( "%snode(%s)",prefix,ctnode ) );

		if ( ctnode.getChildCTNode()!=null )
			recursivePrintCmdTreeNode( ctnode.getChildCTNode(),prefix + "\t" );

		if ( ctnode.getNextSiblingCTNode()!=null )
			recursivePrintCmdTreeNode( ctnode.getNextSiblingCTNode(),prefix );
	}
}
