package test.tsmlang.cmdtreenodes;

import java.util.List;

import test.tsmlang.ObjectCTNodeMatch;

public class CmdTreeLevelStart extends CmdTreeNode
{
	public CmdTreeLevelStart( CmdTreeNode parentCTNode )
	{
		super( null,NODE_TYPE.levelStart,parentCTNode,null,false );
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
