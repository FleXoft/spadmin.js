package test.tsmlang;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqSub extends CmdTreeSeq
{
	private final String subNodeName;

	public CmdTreeSeqSub( Node node )
	{
		super( node,NODE_TYPE.seqSub );
		NamedNodeMap attributes = node.getAttributes();
		this.subNodeName = setTextValue( attributes.getNamedItem( "subnode" ) );

		addChildNodes();
	}

	public String getSubNodeName()
	{
		return subNodeName;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeSeq [bCanBeEmpty=" );
		builder.append( this.getbCanBeEmpty() );
		builder.append( ", subNodeName=" );
		builder.append( subNodeName );
		builder.append( ", node=" );
		builder.append( node.hashCode() );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( ", listSubNodes=" );
		builder.append( listSubNodes );
		builder.append( "]" );
		return builder.toString();
	}
}
