package test.tsmlang;

public class CmdTreeParseTabChoices
{
	private final String strChoice;
	private final CmdTreeNode ctnode;

	public CmdTreeParseTabChoices( String strChoice,CmdTreeNode ctnode )
	{
		this.strChoice = strChoice;
		this.ctnode = ctnode;
	}

	public String getStrChoice()
	{
		return strChoice;
	}
	public CmdTreeNode getCtnode()
	{
		return ctnode;
	}
}
