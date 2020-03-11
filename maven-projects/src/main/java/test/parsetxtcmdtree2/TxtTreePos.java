package test.parsetxtcmdtree2;

public class TxtTreePos
{
	public int indexOfLines = -1;
	public int posStartInLine = -1;
	public int posLength = -1;

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "TxtTreePos [indexOfLines=" );
		builder.append( indexOfLines );
		builder.append( ", posStartInLine=" );
		builder.append( posStartInLine );
		builder.append( ", posLength=" );
		builder.append( posLength );
		builder.append( "]" );
		return builder.toString();
	}
}
