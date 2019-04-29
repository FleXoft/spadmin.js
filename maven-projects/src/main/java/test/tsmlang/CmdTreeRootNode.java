package test.tsmlang;

import org.w3c.dom.Node;

public class CmdTreeRootNode extends CmdTreeNode
{
	public CmdTreeRootNode( Node xmlNode )
	{
		super( xmlNode,NODE_TYPE.root,null,null );

		addChildNodes();
	}

	@Override
	protected String checkCTNode( String cmd )
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
