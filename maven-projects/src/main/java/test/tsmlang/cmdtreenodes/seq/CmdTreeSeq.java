package test.tsmlang.cmdtreenodes.seq;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import test.tsmlang.cmdtreenodes.CmdTreeNode;

public abstract class CmdTreeSeq extends CmdTreeNode
{
	private final Boolean bCanBeEmpty;

	public CmdTreeSeq( Node node,NODE_TYPE type,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode,boolean bHasWord )
	{
		super( node,type,parentCTNode,prevSiblingCTNode,bHasWord );
		NamedNodeMap attributes = node.getAttributes();
		this.bCanBeEmpty = setBooleanValue( attributes.getNamedItem( ATTRNAME_CAN_BE_EMPTY ) );
	}

	protected void setWordType()
	{
		if ( bHasWord==true )
		{
			if ( this.bCanBeEmpty==true )
				super.setWordType( WORD_TYPE.seqCanBeEmpty );
			else
				super.setWordType( WORD_TYPE.seqNecessary );
		}
	}

	public Boolean getbCanBeEmpty()
	{
		return bCanBeEmpty;
	}
}
