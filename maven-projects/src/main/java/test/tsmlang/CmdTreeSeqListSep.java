package test.tsmlang;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqListSep extends CmdTreeSeq
{
	private final String listSeparator;
	private final List<String> listValues;

	public CmdTreeSeqListSep( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.seqListSep,parentCTNode,prevSiblingCTNode );
		NamedNodeMap attributes = node.getAttributes();
		this.listSeparator = setTextValue( attributes.getNamedItem( "listSeparator" ) );
		this.listValues = setListValues( attributes.getNamedItem( "list" ),"," );

		addChildNodes();
	}

	@Override
	protected String checkCTNode( String cmd )
	{
		String result = null;
		for ( String value : listValues )
		{
			if ( cmd.startsWith( value )==true )
			{
				result = cmd.substring( value.length() );
				break;
			}
		}
		return result;
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
		builder.append( ", indexNode=" );
		builder.append( indexNode );
		builder.append( ", listSeparator=" );
		builder.append( listSeparator );
		builder.append( ", listValues=" );
		builder.append( listValues );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( "]" );
		return builder.toString();
	}
}
