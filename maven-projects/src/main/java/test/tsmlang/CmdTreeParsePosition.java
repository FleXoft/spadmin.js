package test.tsmlang;

public class CmdTreeParsePosition
{
	private final String cmd;
	private final CmdTreeNode ctnode;
	private final Boolean bMatch;
//	private final NODE_TYPE levelNodeType;
//	private boolean bPartialMatch = false;

	public CmdTreeParsePosition( String cmd,CmdTreeNode ctnode,Boolean bMatch )
	{
		this.cmd = MainCheck.skipFirstSpaces( cmd );
		this.ctnode = ctnode;
		this.bMatch = bMatch;
//		this.levelNodeType = null;
//		this.cmdTree = parentNode.listSubNodes;
//		if ( cmdTree!=null && cmdTree.isEmpty()==false )
//			this.levelNodeType = cmdTree.get( 0 ).type;
//		else
//			this.levelNodeType = null;
//		System.out.println( String.format( "new CmdTreeLevel(parent:%02d-%s),count=%02d,type=%s,cmd=(%s)",
//				(this.getParentNode()==null) ? -1 : this.getParentNode().getIndexNode(),
//				(this.getParentNode()==null) ? null : this.getParentNode().type,
//				this.cmdTree.size(),
//				(this.levelNodeType==null) ? null : this.levelNodeType.name(),
//				cmd ) );
	}

	public String getCmd()
	{
		return cmd;
	}
	public CmdTreeNode getCTNode()
	{
		return ctnode;
	}
	public Boolean isbMatch()
	{
		return bMatch;
	}
//	public NODE_TYPE getLevelNodeType()
//	{
//		return levelNodeType;
//	}
//	public boolean isbPartialMatch()
//	{
//		return bPartialMatch;
//	}
//	public void setbPartialMatch( boolean bPartialMatch )
//	{
//		this.bPartialMatch = bPartialMatch;
//	}
}
