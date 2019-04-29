package test.tsmlang;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class CmdTreeSeq extends CmdTreeNode
{
	private final Boolean bCanBeEmpty;

	public CmdTreeSeq( Node node,NODE_TYPE type,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,type,parentCTNode,prevSiblingCTNode );
		NamedNodeMap attributes = node.getAttributes();
		this.bCanBeEmpty = setBooleanValue( attributes.getNamedItem( "canbeempty" ) );
	}

	public Boolean getbCanBeEmpty()
	{
		return bCanBeEmpty;
	}

	public List<String> setListValues( Node node,String listSeparator )
	{
		String nodeValue = node.getNodeValue();
		return Arrays.asList( nodeValue.split( listSeparator ) );
	}
}
