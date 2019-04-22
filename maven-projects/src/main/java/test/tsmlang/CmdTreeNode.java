package test.tsmlang;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class CmdTreeNode
{
	public enum NODE_TYPE { choice,seqText,seqSub,seqList,seqListSep };

	protected final Node node;
	protected final NODE_TYPE type;
	protected final List<CmdTreeNode> listSubNodes;

	public CmdTreeNode( Node node,NODE_TYPE type )
	{
		this.type = type;
		this.node = node;
		this.listSubNodes = new ArrayList<CmdTreeNode>();
		System.out.println( String.format( "new CmdTreeNode (%d)",node.hashCode() ) );
	}

	protected void addChildNodes()
	{
		System.out.println( String.format( "addChildNodes start (%d)",node.hashCode() ) );
		NodeList nodeList1 = node.getChildNodes();
		for ( int ic=0; ic<nodeList1.getLength(); ic++ )
		{
			System.out.println( String.format( "addChildNodes(%d) (%d)",node.hashCode(),ic ) );
			Node node1 = nodeList1.item( ic );
			short nodeType = node1.getNodeType();
			if ( nodeType==Node.ELEMENT_NODE )
			{
				listSubNodes.add( CmdTreeNode.createNode( node1 ) );
			}
			else
			{
				System.out.println( String.format( "node(%s) tpye(%d) nodeValue(%s)",node1.getNodeName(),nodeType,node1.getNodeValue() ) );
			}
		}
	}

	protected String setTextValue( Node node )
	{
		String result = null;
		if ( node!=null )
		{
			result = node.getNodeValue();
		}
		return result;
	}

	protected Boolean setBooleanValue( Node node )
	{
		Boolean result = null;
		if ( node!=null )
		{
			String text = node.getNodeValue();
			result = Boolean.parseBoolean( text );
		}
		return result;
	}

	public static CmdTreeNode createNode( Node node )
	{
		CmdTreeNode result;
		String nodeName = node.getNodeName();
		NODE_TYPE tmpType = NODE_TYPE.valueOf( nodeName );
		if ( tmpType==NODE_TYPE.choice )
		{
			result = new CmdTreeChoice( node );
		}
		else if ( tmpType==NODE_TYPE.seqList )
		{
			result = new CmdTreeSeqList( node );
		}
		else if ( tmpType==NODE_TYPE.seqListSep )
		{
			result = new CmdTreeSeqListSep( node );
		}
		else if ( tmpType==NODE_TYPE.seqSub )
		{
			result = new CmdTreeSeqSub( node );
		}
		else if ( tmpType==NODE_TYPE.seqText )
		{
			result = new CmdTreeSeqText( node );
		}
		else
			throw new RuntimeException( String.format( "nodeName(%s)",nodeName ) );

		System.out.println( String.format( "createNode(%s)",result.toString() ) );

		return result;
	}

	public List<CmdTreeNode> getListSubNodes()
	{
		return listSubNodes;
	}
}
