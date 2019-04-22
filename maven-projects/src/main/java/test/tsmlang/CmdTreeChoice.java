package test.tsmlang;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeChoice extends CmdTreeNode
{
	private final String fixPart;
	private final String fullText;

	public CmdTreeChoice( Node node )
	{
		super( node,NODE_TYPE.choice );
		NamedNodeMap attributes = node.getAttributes();
		this.fixPart = setTextValue( attributes.getNamedItem( "fixPart" ) );
		this.fullText = setTextValue( attributes.getNamedItem( "fullText" ) );

		addChildNodes();
	}

	public String getFixPart()
	{
		return fixPart;
	}
	public String getFullText()
	{
		return fullText;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeChoice [hashCode=" );
		builder.append( node.hashCode() );
		builder.append( ", fixPart=" );
		builder.append( fixPart );
		builder.append( ", fullText=" );
		builder.append( fullText );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( ", listSubNodes=" );
		builder.append( listSubNodes );
		builder.append( "]" );
		return builder.toString();
	}
}
