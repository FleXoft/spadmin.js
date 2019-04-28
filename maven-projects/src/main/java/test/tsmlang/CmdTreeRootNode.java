package test.tsmlang;

public class CmdTreeRootNode extends CmdTreeNode
{
	public CmdTreeRootNode()
	{
		super( null,NODE_TYPE.root,null );
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeRootNode [indexNode=" );
		builder.append( indexNode );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( ", childCTNode=" );
		builder.append( childCTNode );
		builder.append( "]" );
		return builder.toString();
	}
}
