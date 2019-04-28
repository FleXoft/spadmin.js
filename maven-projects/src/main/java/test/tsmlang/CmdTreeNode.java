package test.tsmlang;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class CmdTreeNode
{
	public enum NODE_TYPE { root,choice,seqText,seqSub,seqList,seqListSep };

	protected final int indexNode;
	protected final Node xmlNode;
	protected final NODE_TYPE type;
	protected final CmdTreeNode parentCTNode;
	protected CmdTreeNode childCTNode = null;
	protected CmdTreeNode nextSeqCTNode = null;


	public CmdTreeNode( Node xmlNode,NODE_TYPE type,CmdTreeNode parentCTNode )
	{
		this.indexNode = MainCheck.indexNode++;
		this.xmlNode = xmlNode;
		this.type = type;
		this.parentCTNode = parentCTNode;
		System.out.println( String.format( "new CmdTreeNode (%02d)",indexNode ) );
	}

	protected void addChildNodes()
	{
		System.out.println( String.format( "addChildNodes start (%d)",xmlNode.hashCode() ) );
		NodeList nodeList1 = xmlNode.getChildNodes();
		for ( int ic=0; ic<nodeList1.getLength(); ic++ )
		{
			System.out.println( String.format( "addChildNodes(%d) (%d)",xmlNode.hashCode(),ic ) );
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

	public int getIndexNode()
	{
		return indexNode;
	}
	public CmdTreeNode getChildCTNode()
	{
		return childCTNode;
	}
	public void setChildCTNode( CmdTreeNode childCTNode )
	{
		this.childCTNode = childCTNode;
	}
	public CmdTreeNode getNextSeqCTNode()
	{
		return nextSeqCTNode;
	}
	public void setNextSeqCTNode( CmdTreeNode nextSeqCTNode )
	{
		this.nextSeqCTNode = nextSeqCTNode;
	}
	public Node getXmlNode()
	{
		return xmlNode;
	}
	public NODE_TYPE getType()
	{
		return type;
	}
	public CmdTreeNode getParentCTNode()
	{
		return parentCTNode;
	}
}
