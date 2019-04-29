package test.tsmlang;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CmdTreeSeqText extends CmdTreeSeq
{
	private final String fixPart;
	private final String fullText;

	public CmdTreeSeqText( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.seqText,parentCTNode,prevSiblingCTNode );
		NamedNodeMap attributes = node.getAttributes();
		this.fixPart = setTextValue( attributes.getNamedItem( "fixPart" ) );
		this.fullText = setTextValue( attributes.getNamedItem( "fullText" ) );

		addChildNodes();
	}

	@Override
	protected String checkCTNode( String cmd )
	{
		String result = null;
		if ( cmd.startsWith( fullText )==true )
		{
			result = cmd.substring( fullText.length() );
		}
		else if ( cmd.startsWith( fixPart )==true )
		{
			result = cmd.substring( fixPart.length() );
		}
		return result;
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
		builder.append( ", indexNode=" );
		builder.append( indexNode );
		builder.append( ", fixPart=" );
		builder.append( fixPart );
		builder.append( ", fullText=" );
		builder.append( fullText );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( "]" );
		return builder.toString();
	}
}
