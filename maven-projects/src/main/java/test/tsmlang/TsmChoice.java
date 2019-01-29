package test.tsmlang;

import java.util.List;

public class TsmChoice extends TsmCommandItem
{
	public final boolean bCanBeEmpty;
	public final TsmCommandItemSequence seq;


	public TsmChoice( int index,boolean bCanBeEmpty,TsmCommandItemSequence seq )
	{
		super( index );
		this.bCanBeEmpty = bCanBeEmpty;
		this.seq = seq;
	}

	@Override
	public boolean check( String token )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> tabChoices( String token )
	{
		// TODO Auto-generated method stub
		return null;
	}

}
