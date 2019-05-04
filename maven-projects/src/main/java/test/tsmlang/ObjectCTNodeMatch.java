package test.tsmlang;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;

public class ObjectCTNodeMatch
{
	public String nextCmd = null;
	public TYPE_MATCH match = TYPE_MATCH.noMatch;

	public ObjectCTNodeMatch()
	{
	}

	public ObjectCTNodeMatch( String nextCmd,TYPE_MATCH match )
	{
		this.nextCmd = nextCmd;
		this.match = match;
	}
}
