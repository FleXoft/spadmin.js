package test.tsmlang;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqList extends CmdTreeSeq
{
	private final List<String> listValues;

	public CmdTreeSeqList( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.seqList,parentCTNode,prevSiblingCTNode,true );
		NamedNodeMap attributes = node.getAttributes();
		this.listValues = setListValues( attributes.getNamedItem( ATTRNAME_LIST ),"," );

		addChildNodes();
	}

	@Override
	protected List<String> addTabChoices( String cmd )
	{
		return super.addTabChoicesForList( cmd,listValues );
	}

	@Override
	protected ObjectCTNodeMatch checkCTNode( String cmd )
	{
		return super.checkCTNodeForList1( cmd,listValues );
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
