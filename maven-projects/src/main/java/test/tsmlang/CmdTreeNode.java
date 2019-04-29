package test.tsmlang;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class CmdTreeNode
{
	public enum NODE_TYPE { root,levelStart,choice,seqText,seqSub,seqList,seqListSep };

	protected final int indexNode;
	protected final Node xmlNode;
	protected final NODE_TYPE type;
	protected final CmdTreeNode parentCTNode;
	protected CmdTreeNode childCTNode = null;
	protected CmdTreeNode nextSiblingCTNode = null;

	protected abstract String checkCTNode( String cmd );


	public CmdTreeNode( Node xmlNode,NODE_TYPE type,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		CmdTreeNode ctnodeLevelStart = null;
		if ( prevSiblingCTNode==null && parentCTNode!=null )
		{
			if ( type.equals( NODE_TYPE.levelStart )==false )
			{
				ctnodeLevelStart = new CmdTreeLevelStart( parentCTNode );
				parentCTNode.setChildCTNode( ctnodeLevelStart );
			}
		}

		this.indexNode = MainCheck.indexNode++;
		this.xmlNode = xmlNode;
		this.type = type;
		this.parentCTNode = parentCTNode;

		if ( ctnodeLevelStart!=null )
			ctnodeLevelStart.setNextSiblingCTNode( this );

		if ( prevSiblingCTNode!=null )
		{
			prevSiblingCTNode.setNextSiblingCTNode( this );
			if ( similar( prevSiblingCTNode )!=true )
				throw new RuntimeException( String.format( "siblings' types are not equal(%s,%s)(%02d)",type,prevSiblingCTNode.type,this.indexNode ) );
		}

		System.out.println( String.format( "new CmdTreeNode (%02d)",indexNode ) );
	}

	private boolean similar( CmdTreeNode prevSiblingCTNode )
	{
		if ( prevSiblingCTNode.type==NODE_TYPE.choice && type==NODE_TYPE.choice )
			return true;
		if ( (prevSiblingCTNode instanceof CmdTreeSeq)==true && (this instanceof CmdTreeSeq)==true )
			return true;
		return false;
	}

	public static CmdTreeNode createNode( Node xmlNode,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
	{
		CmdTreeNode result;
		String nodeName = xmlNode.getNodeName();
		NODE_TYPE tmpType = NODE_TYPE.valueOf( nodeName );
		if ( tmpType==NODE_TYPE.root )
		{
			result = new CmdTreeRootNode( xmlNode );
		}
		else if ( tmpType==NODE_TYPE.choice )
		{
			result = new CmdTreeChoice( xmlNode,parentCTNode,prevSiblingCTNode );
		}
		else if ( tmpType==NODE_TYPE.seqList )
		{
			result = new CmdTreeSeqList( xmlNode,parentCTNode,prevSiblingCTNode );
		}
		else if ( tmpType==NODE_TYPE.seqListSep )
		{
			result = new CmdTreeSeqListSep( xmlNode,parentCTNode,prevSiblingCTNode );
		}
		else if ( tmpType==NODE_TYPE.seqSub )
		{
			result = new CmdTreeSeqSub( xmlNode,parentCTNode,prevSiblingCTNode );
		}
		else if ( tmpType==NODE_TYPE.seqText )
		{
			result = new CmdTreeSeqText( xmlNode,parentCTNode,prevSiblingCTNode );
		}
		else
			throw new RuntimeException( String.format( "nodeName(%s)",nodeName ) );

		System.out.println( String.format( "createNode(%s)",result.toString() ) );

		return result;
	}

	protected void addChildNodes()
	{
		System.out.println( String.format( "addChildNodes start (%d)",xmlNode.hashCode() ) );
		NodeList nodeList1 = xmlNode.getChildNodes();
		CmdTreeNode prevSiblingCTNode = null;
		for ( int ic=0; ic<nodeList1.getLength(); ic++ )
		{
			System.out.println( String.format( "addChildNodes(%d) (%d)",xmlNode.hashCode(),ic ) );
			Node node1 = nodeList1.item( ic );
			short nodeType = node1.getNodeType();
			if ( nodeType==Node.ELEMENT_NODE )
			{
				prevSiblingCTNode = CmdTreeNode.createNode( node1,this,prevSiblingCTNode );
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
	public CmdTreeNode getNextSiblingCTNode()
	{
		return nextSiblingCTNode;
	}
	public void setNextSiblingCTNode( CmdTreeNode nextSiblingCTNode )
	{
		this.nextSiblingCTNode = nextSiblingCTNode;
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
