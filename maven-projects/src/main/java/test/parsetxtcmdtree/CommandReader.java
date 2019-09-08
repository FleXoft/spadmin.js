package test.parsetxtcmdtree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandReader
{
	public static String COMMAND_SEPARATOR_LINE = "---commandStart---";
	private final List<String> linesTxtCmdTree;
	private Iterator<String> iter = null;
	private boolean bStart = true;


	public CommandReader( List<String> linesTxtCmdTree )
	{
		this.linesTxtCmdTree = linesTxtCmdTree;
		this.iter = linesTxtCmdTree.iterator();
	}

	public List<String> readNextCommand()
	{
		List<String> result = new ArrayList<String>();
		while ( iter.hasNext()==true )
		{
			String line = iter.next();
			if ( line.compareTo( COMMAND_SEPARATOR_LINE )==0 )
			{
				if ( bStart==false )
				{
					return result;
				}
				else
				{
					bStart = false;
					continue;
				}
			}
			else
			{
				if ( bStart==true )
					throw new RuntimeException( "file start!" );
				result.add( line );
			}
		}
		if ( result.isEmpty()==false )
			return null;
		else
			return result;
	}

	public void reset()
	{
		this.iter = linesTxtCmdTree.iterator();
		this.bStart = true;
	}
}
