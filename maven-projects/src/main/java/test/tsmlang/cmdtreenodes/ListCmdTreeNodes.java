package test.tsmlang.cmdtreenodes;

import java.util.LinkedList;

public class ListCmdTreeNodes
{
	public static LinkedList<CmdTreeNode> listCmdTreeNodes = new LinkedList<CmdTreeNode>();

	public static void addNextCmdTreeNode( CmdTreeNode cmdTreeNode )
	{
		listCmdTreeNodes.addLast( cmdTreeNode );
	}
	public static CmdTreeNode safeGetNextCmdTreeNode( CmdTreeNode cmdTreeNode )
	{
		int nextIndex = cmdTreeNode.getIndexNode()+1;
		if ( nextIndex<listCmdTreeNodes.size() )
		{
			return listCmdTreeNodes.get( nextIndex );
		}
		else
			return null;
	}
}
