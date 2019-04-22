package test.tsmlang;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class CmdTreeSeq extends CmdTreeNode
{
	private final Boolean bCanBeEmpty;

	public CmdTreeSeq( Node node,NODE_TYPE type )
	{
		super( node,type );
		NamedNodeMap attributes = node.getAttributes();
		this.bCanBeEmpty = setBooleanValue( attributes.getNamedItem( "canbeempty" ) );
	}

	public Boolean getbCanBeEmpty()
	{
		return bCanBeEmpty;
	}
}
