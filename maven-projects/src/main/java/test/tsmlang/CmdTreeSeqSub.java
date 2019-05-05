package test.tsmlang;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;

public class CmdTreeSeqSub extends CmdTreeSeq
{
	private final String subNodeName;

	public CmdTreeSeqSub( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.seqSub,parentCTNode,prevSiblingCTNode,false );
		NamedNodeMap attributes = node.getAttributes();
		this.subNodeName = setTextValue( attributes.getNamedItem( ATTRNAME_SUBNODE ) );

		addChildNodes();
	}

	@Override
	protected List<String> addTabChoices( String cmd )
	{
		return null;
	}

	@Override
	protected ObjectCTNodeMatch checkCTNode( String cmd )
	{
		return new ObjectCTNodeMatch( cmd,TYPE_MATCH.subNode );
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
