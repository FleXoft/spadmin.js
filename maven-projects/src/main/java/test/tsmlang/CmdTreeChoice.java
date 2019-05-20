package test.tsmlang;

import org.w3c.dom.Node;

public abstract class CmdTreeChoice extends CmdTreeNode
{
	public CmdTreeChoice( Node node,NODE_TYPE type,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode,boolean bHasWord )
	{
		super( node,type,parentCTNode,prevSiblingCTNode,bHasWord );
	}
}
