package test.tsmlang;

import java.util.List;

import test.tsmlang.CmdTreeNode.NODE_TYPE;

public class CmdTreeParsePosition
{
	private final String cmd;
	private final CmdTreeNode parentNode;
	private final List<CmdTreeNode> cmdTree;
	private final NODE_TYPE levelNodeType;
	private boolean bPartialMatch = false;

	public CmdTreeParsePosition( String cmd,CmdTreeNode parentNode )
	{
		this.cmd = cmd;
		this.parentNode = parentNode;
		this.cmdTree = parentNode.listSubNodes;
		if ( cmdTree!=null && cmdTree.isEmpty()==false )
			this.levelNodeType = cmdTree.get( 0 ).type;
		else
			this.levelNodeType = null;
		System.out.println( String.format( "new CmdTreeLevel(parent:%02d-%s),count=%02d,type=%s,cmd=(%s)",
				(this.getParentNode()==null) ? -1 : this.getParentNode().getIndexNode(),
				(this.getParentNode()==null) ? null : this.getParentNode().type,
				this.cmdTree.size(),
				(this.levelNodeType==null) ? null : this.levelNodeType.name(),
				cmd ) );
	}

	public String getCmd()
	{
		return cmd;
	}
	public CmdTreeNode getParentNode()
	{
		return parentNode;
	}
	public List<CmdTreeNode> getCmdTree()
	{
		return cmdTree;
	}
	public NODE_TYPE getLevelNodeType()
	{
		return levelNodeType;
	}
	public boolean isbPartialMatch()
	{
		return bPartialMatch;
	}
	public void setbPartialMatch( boolean bPartialMatch )
	{
		this.bPartialMatch = bPartialMatch;
	}
}
