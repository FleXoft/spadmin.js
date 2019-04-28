package test.tsmlang;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqListSep extends CmdTreeSeq
{
	private final String listSeparator;
	private final List<String> listValues;

	public CmdTreeSeqListSep( Node node )
	{
		super( node,NODE_TYPE.seqListSep );
		NamedNodeMap attributes = node.getAttributes();
		this.listSeparator = setTextValue( attributes.getNamedItem( "listSeparator" ) );
		this.listValues = setListValues( attributes.getNamedItem( "list" ),"," );

		addChildNodes();
	}

	public String getListSeparator()
	{
		return listSeparator;
	}
	public List<String> getListValues()
	{
		return listValues;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeSeq [bCanBeEmpty=" );
		builder.append( this.getbCanBeEmpty() );
		builder.append( ", listSeparator=" );
		builder.append( listSeparator );
		builder.append( ", listValues=" );
		builder.append( listValues );
		builder.append( ", node=" );
		builder.append( node.hashCode() );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( ", listSubNodes=" );
		builder.append( listSubNodes );
		builder.append( "]" );
		return builder.toString();
	}
}
