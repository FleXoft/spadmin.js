package test.tsmlang.cmdtreenodes.choice;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;
import test.tsmlang.ObjectCTNodeMatch;
import test.tsmlang.cmdtreenodes.CmdTreeNode;

public class CmdTreeChoiceSub extends CmdTreeChoice
{
	private final String subNodeName;

	public CmdTreeChoiceSub( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.choiceSub,parentCTNode,prevSiblingCTNode,false );
		NamedNodeMap attributes = node.getAttributes();
		this.subNodeName = setTextValue( attributes.getNamedItem( ATTRNAME_SUBNODE ) );

		addChildNodes();
	}

	@Override
	public ObjectCTNodeMatch checkCTNode( String cmd )
	{
		return new ObjectCTNodeMatch( cmd,TYPE_MATCH.subNode );
	}

	@Override
	public List<String> addTabChoices( String cmd )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getSubNodeName()
	{
		return subNodeName;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeChoiceSub [subNodeName=" );
		builder.append( subNodeName );
		builder.append( ", indexNode=" );
		builder.append( indexNode );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( ", level=" );
		builder.append( level );
		builder.append( ", cmdSample=" );
		builder.append( cmdSample );
		builder.append( "]" );
		return builder.toString();
	}
}
