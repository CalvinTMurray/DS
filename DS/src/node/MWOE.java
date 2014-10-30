/**
 * The Minimum Weighted Outgoing Edge (MWOE) of a node
 * Note that this edge is local to the node.  Therefore it should be the minimum 
 * weighted edge among its neighbours that are not included in the MST
 */
package node;

/**
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class MWOE {
	
	private NodeInterface node;
	private double distance;
	
	/**
	 * A Minimum Weighted Outgoing Edge
	 * @param node the node which is the Minimum Weighted Outgoing Edge
	 * @param distance the distance which was minimum
	 */
	public MWOE(NodeInterface node, double distance) {
		this.node = node;
		this.distance = distance;
	}
	
	/**
	 * 
	 * @return the Minimum Weihted Outgoing Edge node
	 */
	public NodeInterface getNode(){
		return node;
	}
	
	/**
	 * 
	 * @return The distance for the Minimum Weighted Outgoing Edge
	 */
	public double getDistance() {
		return distance;
	}
	
}