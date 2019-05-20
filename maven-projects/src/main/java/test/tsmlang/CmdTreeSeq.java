package test.tsmlang;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class CmdTreeSeq extends CmdTreeNode
{
	private final Boolean bCanBeEmpty;

	public CmdTreeSeq( Node node,NODE_TYPE type,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode,boolean bHasWord )
	{
		super( node,type,parentCTNode,prevSiblingCTNode,bHasWord );
		NamedNodeMap attributes = node.getAttributes();
		this.bCanBeEmpty = setBooleanValue( attributes.getNamedItem( ATTRNAME_CAN_BE_EMPTY ) );
	}

	public Boolean getbCanBeEmpty()
	{
		return bCanBeEmpty;
	}
}
