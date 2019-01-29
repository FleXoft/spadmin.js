package test.tsmlang;

import java.util.LinkedList;
import java.util.List;

public class TsmListSeparatedItems extends TsmCommandItem
{
	public final String separator;
	public final TsmCommandItem listItem;
	public final LinkedList<TsmCommandItem> listItems;


	public TsmListSeparatedItems( int index,String separator,TsmCommandItem listItem )
	{
		super( index );
		this.separator = separator;
		this.listItem = listItem;
		this.listItems = new LinkedList<TsmCommandItem>();
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
