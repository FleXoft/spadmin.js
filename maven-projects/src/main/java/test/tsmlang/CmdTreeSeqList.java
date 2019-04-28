package test.tsmlang;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqList extends CmdTreeSeq
{
	private final List<String> listValues;

	public CmdTreeSeqList( Node node )
	{
		super( node,NODE_TYPE.seqList );
		NamedNodeMap attributes = node.getAttributes();
		this.listValues = setListValues( attributes.getNamedItem( "list" ),"," );

		addChildNodes();
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
		builder.append( getbCanBeEmpty() );
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
