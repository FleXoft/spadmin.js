package test.tsmlang;

import java.util.ArrayList;
import java.util.List;

import test.tsmlang.cmdtreenodes.CmdTreeNode;

public class ObjectTabChoices
{
	public static List<CmdTreeParseTabChoices> listTabChoices = new ArrayList<CmdTreeParseTabChoices>();
	public static void addTabChoices( CmdTreeNode ctnode,String cmd )
	{
		List<String> list = ctnode.addTabChoices( cmd );
		if ( list!=null && list.isEmpty()==false )
		{
			for ( String val : list )
			{
				CmdTreeParseTabChoices obj = new CmdTreeParseTabChoices( val,ctnode );
				listTabChoices.add( obj );
			}
		}
	}

}
