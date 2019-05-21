package test.tsmlang.cmdtreenodes.choice;

import org.w3c.dom.Node;

import test.tsmlang.cmdtreenodes.CmdTreeNode;

public abstract class CmdTreeChoice extends CmdTreeNode
{
	public CmdTreeChoice( Node node,NODE_TYPE type,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode,boolean bHasWord )
	{
		super( node,type,parentCTNode,prevSiblingCTNode,bHasWord );
	}

	protected void setWordType()
	{
		if ( bHasWord==true )
		{
			super.setWordType( WORD_TYPE.choiceNecessary );
		}
	}

}
