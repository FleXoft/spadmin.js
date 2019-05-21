package test.tsmlang.cmdtreenodes.choice;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import test.tsmlang.ObjectCTNodeMatch;
import test.tsmlang.cmdtreenodes.CmdTreeNode;
import test.tsmlang.cmdtreenodes.CmdTreeNode.WORD_TYPE;

public class CmdTreeChoiceList extends CmdTreeChoice
{
	private final List<String> listValues;

	public CmdTreeChoiceList( Node node,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		super( node,NODE_TYPE.choiceList,parentCTNode,prevSiblingCTNode,true );
		NamedNodeMap attributes = node.getAttributes();
		this.listValues = setListValues( attributes.getNamedItem( ATTRNAME_LIST ),"," );
		setWordType();

		addChildNodes();
	}

	@Override
	public ObjectCTNodeMatch checkCTNode( String cmd )
	{
		return super.checkCTNodeForList1( cmd,listValues );
	}

	@Override
	public List<String> addTabChoices( String cmd )
	{
		return super.addTabChoicesForList( cmd,listValues );
	}

	public List<String> getListValues()
	{
		return listValues;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "CmdTreeChoiceList [listValues=" );
		builder.append( listValues );
		builder.append( ", indexNode=" );
		builder.append( indexNode );
		builder.append( ", type=" );
		builder.append( type );
		builder.append( ", level=" );
		builder.append( level );
		builder.append( ", cmdSample=" );
		builder.append( cmdSample );
		builder.append( "]" );
		return builder.toString();
	}
}
