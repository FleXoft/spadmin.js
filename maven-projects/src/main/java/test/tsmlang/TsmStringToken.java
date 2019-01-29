package test.tsmlang;

import java.util.List;

public class TsmStringToken extends TsmCommandItem
{
	public final String fixPart;
	public final String fullText;


	public TsmStringToken( int index,String fixPart,String fullText )
	{
		super( index );
		this.fixPart = fixPart;
		this.fullText = fullText;
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
