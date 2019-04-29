package test.tsmlang;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqList extends CmdTreeSeq
{
	private final List<String> listValues;

	public CmdTreeSeqList( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.seqList,parentCTNode,prevSiblingCTNode );
		NamedNodeMap attributes = node.getAttributes();
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
		builder.append( ", indexNode=" );
		builder.append( indexNode );
		builder.append( ", listValues=" );
		builder.append( listValues );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( "]" );
		return builder.toString();
	}
}
