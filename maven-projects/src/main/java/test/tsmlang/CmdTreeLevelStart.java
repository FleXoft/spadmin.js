package test.tsmlang;

public class CmdTreeLevelStart extends CmdTreeNode
{
	public CmdTreeLevelStart( CmdTreeNode parentCTNode )
	{
		super( null,NODE_TYPE.levelStart,parentCTNode,null );
	}

	@Override
	protected String checkCTNode( String cmd )
	{
		return cmd;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeLevelStart [indexNode=" );
		builder.append( indexNode );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( "]" );
		return builder.toString();
	}

}
