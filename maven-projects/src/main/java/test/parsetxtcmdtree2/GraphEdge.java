package test.parsetxtcmdtree2;

public class GraphEdge
{
	public enum GraphEdgeType { empty, fixString, singleValue, listOfValues, asterisk };

	public final GraphEdgeType type;
	public final TxtTreePos pos;
	public final GraphVertex nextVertex;

	public GraphEdge( GraphEdgeType type,TxtTreePos pos,GraphVertex nextVertex )
	{
		this.type = type;
		this.pos = pos;
		this.nextVertex = nextVertex;
	}

}
