package test.tsmlang.cmdtreenodes.choice;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import test.tsmlang.ObjectCTNodeMatch;
import test.tsmlang.cmdtreenodes.CmdTreeNode;

public class CmdTreeChoiceText extends CmdTreeChoice
{
	private final String fixPart;
	private final String fullText;

	public CmdTreeChoiceText( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.choiceText,parentCTNode,prevSiblingCTNode,true );
		NamedNodeMap attributes = node.getAttributes();
		String keyWord = setTextValue( attributes.getNamedItem( "keyWord" ) );
		this.fixPart = getFixPart( keyWord );
		this.fullText = getFullText( keyWord );
		setWordType();

		addChildNodes();
	}

	@Override
	public List<String> addTabChoices( String cmd )
	{
		return addTabChoicesForText( cmd,fixPart,fullText );
	}

	@Override
	public ObjectCTNodeMatch checkCTNode( String cmd )
	{
		return super.checkCTNodeForText1( cmd,fixPart,fullText );
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
		builder.append( "CmdTreeChoice [fixPart=" );
		builder.append( fixPart );
		builder.append( ", fullText=" );
		builder.append( fullText );
		builder.append( ", indexNode=" );
		builder.append( indexNode );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( ", level=" );
		builder.append( level );
		builder.append( "]" );
		return builder.toString();
	}
}
