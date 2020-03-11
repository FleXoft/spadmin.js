package test.parsetxtcmdtree2;

import java.util.ArrayList;
import java.util.List;

public class GraphVertex
{
	public enum GraphVertexType { commandStart, commandEnd, separator, junction };

	public final GraphVertexType type;
	public final TxtTreePos pos;
	public final List<GraphEdge> listEdges = new ArrayList<GraphEdge>();

	public GraphVertex( GraphVertexType type,TxtTreePos pos )
	{
		this.type = type;
		this.pos = pos;
	}
}
