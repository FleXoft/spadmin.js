package test.tsmlang;

import java.util.List;

public abstract class TsmCommandItem
{
	public final int index;


	public TsmCommandItem( int index )
	{
		this.index = index;
	}

	public abstract boolean check( String token );
	public abstract List<String> tabChoices( String token );
}
