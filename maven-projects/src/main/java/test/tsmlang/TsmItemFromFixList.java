package test.tsmlang;

import java.util.List;

public class TsmItemFromFixList extends TsmCommandItem
{
	public String selectedItem = null;
	public final List<String> listFixItems;


	public TsmItemFromFixList( int index,List<String> listFixItems )
	{
		super( index );
		this.listFixItems = listFixItems;
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
