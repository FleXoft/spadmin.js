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
		super( node,NODE_TYPE.seqListSep,parentCTNode,prevSiblingCTNode,true );
		NamedNodeMap attributes = node.getAttributes();
		this.listSeparator = setTextValue( attributes.getNamedItem( ATTRNAME_LIST_SEPARATOR ) );
		this.listValues = setListValues( attributes.getNamedItem( ATTRNAME_LIST ),this.listSeparator );

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
