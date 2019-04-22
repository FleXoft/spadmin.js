package test.tsmlang;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqText extends CmdTreeSeq
{
	private final String fixPart;
	private final String fullText;

	public CmdTreeSeqText( Node node )
	{
		super( node,NODE_TYPE.seqText );
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
		builder.append( "CmdTreeSeq [bCanBeEmpty=" );
		builder.append( this.getbCanBeEmpty() );
		builder.append( ", fixPart=" );
		builder.append( fixPart );
		builder.append( ", fullText=" );
		builder.append( fullText );
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
