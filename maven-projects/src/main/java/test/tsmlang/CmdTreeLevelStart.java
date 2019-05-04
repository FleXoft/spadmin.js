package test.tsmlang;

import java.util.List;

public class CmdTreeLevelStart extends CmdTreeNode
{
	public CmdTreeLevelStart( CmdTreeNode parentCTNode )
	{
		super( null,NODE_TYPE.levelStart,parentCTNode,null );
	}

	@Override
	protected List<String> addTabChoices( String cmd )
	{
		return null;
	}

	@Override
	protected ObjectCTNodeMatch checkCTNode( String cmd )
	{
		throw new RuntimeException( "unimplemented" );
//		return new ObjectCTNodeMatch( cmd,TYPE_MATCH.full );
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
