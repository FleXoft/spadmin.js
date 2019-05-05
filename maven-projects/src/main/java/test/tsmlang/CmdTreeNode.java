package test.tsmlang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import test.tsmlang.CmdTreeParsePosition.TYPE_MATCH;

public abstract class CmdTreeNode
{
	protected static final String ATTRNAME_KEYWORD = "keyWord";
	protected static final String ATTRNAME_CAN_BE_EMPTY = "canBeEmpty";
	protected static final String ATTRNAME_LIST = "list";
	protected static final String ATTRNAME_LIST_SEPARATOR = "listSeparator";
	protected static final String ATTRNAME_SUBNODE = "subnode";


	public enum NODE_TYPE { root,levelStart,choice,seqText,seqSub,seqList,seqListSep };

	protected final int indexNode;
	protected final Node xmlNode;
	protected final NODE_TYPE type;
	protected final CmdTreeNode parentCTNode;
	protected final int level;
	protected final boolean bHasWord;
	protected final String cmdSample;
	protected CmdTreeNode childCTNode = null;
	protected CmdTreeNode nextSiblingCTNode = null;

	protected abstract ObjectCTNodeMatch checkCTNode( String cmd );
	protected abstract List<String> addTabChoices( String cmd );


	public CmdTreeNode( Node xmlNode,NODE_TYPE type,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode,boolean bHasWord )
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
		this.bHasWord = bHasWord;
		this.level = ( this.parentCTNode==null ) ? 1 : this.parentCTNode.level + 1;
		if ( bHasWord==true )
		{
			if ( this.parentCTNode==null )
				this.cmdSample = String.format( "%02d",this.indexNode ); 
			else
				this.cmdSample = String.format( "%s %02d",this.parentCTNode.cmdSample,this.indexNode );
		}
		else
		{
			if ( this.parentCTNode==null )
				this.cmdSample = String.format( "" ); 
			else
				this.cmdSample = this.parentCTNode.cmdSample;
		}

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

	public static CmdTreeNode recursiveCreateNode( Node xmlNode,CmdTreeNode parentCTNode,CmdTreeNode prevSiblingCTNode )
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
				prevSiblingCTNode = CmdTreeNode.recursiveCreateNode( node1,this,prevSiblingCTNode );
			}
			else
			{
				System.out.println( String.format( "node(%s) tpye(%d) nodeValue(%s)",node1.getNodeName(),nodeType,node1.getNodeValue() ) );
			}
		}
	}

	protected String getFixPart( String keyWord )
	{
		StringBuilder sb = new StringBuilder();
		for ( int ic=0; ic<keyWord.length(); ic++ )
		{
			char ch = keyWord.charAt( ic );
			if ( Character.isUpperCase( ch )==true )
				sb.append( Character.toLowerCase( ch ) );
		}
		if ( sb.length()<=0 )
			return keyWord.toLowerCase();
		else
			return sb.toString();
	}

	protected ObjectCTNodeMatch checkCTNodeForText1( String cmd,String fixPart,String fullText )
	{
		ObjectCTNodeMatch result = new ObjectCTNodeMatch();
		if ( cmd.length()>0 )
		{
			int indexMatch = getLastStringMatchIndex( cmd,fullText );
			if ( indexMatch>=fixPart.length() )
			{
				result.nextCmd = cmd.substring( indexMatch );
				if ( indexMatch<fullText.length() )
					result.match = TYPE_MATCH.partial;
				else
					result.match = TYPE_MATCH.full;
			}
		}
		return result;
	}

	protected ObjectCTNodeMatch checkCTNodeForList1( String cmd,List<String> listValues )
	{
		ObjectCTNodeMatch result = new ObjectCTNodeMatch();
		if ( cmd.length()>0 )
		{
			for ( String value : listValues )
			{
				if ( cmd.startsWith( value )==true )
				{
					result.nextCmd = cmd.substring( value.length() );
					result.match = TYPE_MATCH.full;
					break;
				}
			}
		}
		return result;
	}

	protected String checkCTNodeText2( String cmd,String fixPart,String fullText )
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

	protected List<String> addTabChoicesForText( String cmd,String fixPart,String fullText )
	{
		List<String> result = null;
		if ( cmd.trim().length()<=0 )
		{
			result = Arrays.asList( new String[] { fullText } );
		}
		else
		{
			int indexMatch = getLastStringMatchIndex( cmd,fixPart );
			if ( indexMatch>0 )
			{
				String nextCmd = cmd.substring( indexMatch );
				if ( nextCmd.length()<=0 || Character.isWhitespace( nextCmd.charAt( 0 ) )==true )
					result = Arrays.asList( new String[] { fullText } );
			}
		}
		return result;
	}

	protected List<String> addTabChoicesForList( String cmd,List<String> listValues )
	{
		List<String> result = new ArrayList<String>();
		if ( cmd.trim().length()<=0 )
		{
			result.addAll( listValues );
		}
		else
		{
			for ( String val : listValues )
			{
				int indexMatch = getLastStringMatchIndex( cmd,val );
				if ( indexMatch>0 )
				{
					String nextCmd = cmd.substring( indexMatch );
					if ( nextCmd.length()<=0 || Character.isWhitespace( nextCmd.charAt( 0 ) )==true )
						result.add( val );
				}
			}
		}
		return result;
	}

	protected int getLastStringMatchIndex( String cmd,String chechStr )
	{
		int indexMatch = 0;
		for ( ; indexMatch<cmd.length() && indexMatch<chechStr.length(); indexMatch++ )
		{
			char ch1 = cmd.charAt( indexMatch );
			char ch2 = chechStr.charAt( indexMatch );
			if ( ch1!=ch2 )
				break;
		}
		return indexMatch;
	}

	protected String getFullText( String keyWord )
	{
		return keyWord.toLowerCase();
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
	public int getLevel()
	{
		return level;
	}
	public boolean getbHasWord()
	{
		return bHasWord;
	}
	public String getCmdSample()
	{
		return cmdSample;
	}
}
