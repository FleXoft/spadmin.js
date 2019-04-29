package test.tsmlang;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqSub extends CmdTreeSeq
{
	private final String subNodeName;

	public CmdTreeSeqSub( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.seqSub,parentCTNode,prevSiblingCTNode );
		NamedNodeMap attributes = node.getAttributes();
		this.subNodeName = setTextValue( attributes.getNamedItem( "subnode" ) );

		addChildNodes();
	}

	@Override
	protected String checkCTNode( String cmd )
	{
		return cmd;
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
		builder.append( ", indexNode=" );
		builder.append( indexNode );
		builder.append( ", subNodeName=" );
		builder.append( subNodeName );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( "]" );
		return builder.toString();
	}
}
