package test.tsmlang;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class LoadCmdTreeXml
{
	public static int indexNode = 0;
	public static CmdTreeRootNode loadXml() throws Exception
	{
		if ( indexNode>0 )
			throw new RuntimeException( "reloadXml..." );

//		egy olyan fát készítek, ahol 1 node-ból megmondható 
//		- a parent
//		- a child
//		- a szinten következő node (nextSibling)
//		ha a parent node-nak több child node-ja van, akkor a prent node az első child-ot tartalmazza, ahonnan végig lehet járni az összes sibling-et
//		ez azért kell, hogy folytathassam a parse-olást, bármelyik node-ról, tudjak bármerre mozogni

		CmdTreeRootNode rootNode = null;
		try ( FileInputStream fileIS = new FileInputStream( MainCheck.COMMAND_TREE_XML ) )
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse( fileIS );
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "/root";
			Node xmlNodeRoot = (Node)xPath.compile( expression ).evaluate( xmlDocument,XPathConstants.NODE );
			rootNode = (CmdTreeRootNode)CmdTreeNode.recursiveCreateNode( xmlNodeRoot,null,null );
		}
		return rootNode;
	}
}
