package test.tsmlang.cmdtreenodes;

import java.util.List;

import org.w3c.dom.Node;

import test.tsmlang.ObjectCTNodeMatch;

public class CmdTreeRootNode extends CmdTreeNode
{
	public CmdTreeRootNode( Node xmlNode )
	{
		super( xmlNode,NODE_TYPE.root,null,null,true );

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
		throw new RuntimeException( "unimplemented" );
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeRootNode [indexNode=" );
		builder.append( indexNode );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( "]" );
		return builder.toString();
	}
}
