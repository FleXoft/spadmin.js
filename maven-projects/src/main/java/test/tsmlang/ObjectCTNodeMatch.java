package test.tsmlang;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;
import test.tsmlang.cmdtreenodes.CmdTreeNode;

public class ObjectCTNodeMatch
{
	public final CmdTreeNode ctNode;
	public String nextCmd = null;
	public TYPE_MATCH match = TYPE_MATCH.noMatch;

	public ObjectCTNodeMatch( CmdTreeNode ctNode )
	{
		this.ctNode = ctNode;
	}

	public ObjectCTNodeMatch( CmdTreeNode ctNode,String nextCmd,TYPE_MATCH match )
	{
		this.ctNode = ctNode;
		this.nextCmd = nextCmd;
		this.match = match;
	}
}
