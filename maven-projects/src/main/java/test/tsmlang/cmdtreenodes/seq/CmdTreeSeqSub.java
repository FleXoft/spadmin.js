package test.tsmlang.cmdtreenodes.seq;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import test.tsmlang.ObjectCTNodeMatch;
import test.tsmlang.cmdtreenodes.CmdTreeNode;

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
	public List<String> addTabChoices( String cmd )
	{
		return null;
	}

	@Override
	public ObjectCTNodeMatch checkCTNode( String cmd )
	{
		return null; // new ObjectCTNodeMatch( this,cmd,TYPE_MATCH.subNode );
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
