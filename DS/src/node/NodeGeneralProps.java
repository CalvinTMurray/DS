/**
 * Node Properties - stores the properties of a node which are used during in a node's constructor
 */
package node;

import java.awt.geom.Point2D;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class NodeGeneralProps {
	
	/**
	 * Node constructor properties
	 */
	private int nodeID;
	private Point2D.Double position;
	private double energy;
	private double minimumBudget;
	private int communicationRadius;
	

	/**
	 * Get this node's current ID
	 * @return the node ID
	 */
	public int getNodeID() {
		return nodeID;
	}

	/**
	 * Set this node's ID
	 * @param the ID to set
	 */
	protected void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	/**
	 * Get this node's current energy levels
	 * @return the energy levels
	 */
	public double getEnergy() {
		return energy;
	}

	/**
	 * Set this node's current energy level
	 * @param the current energy level
	 */
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	/**
	 * Get the minimum energy levels this node requires to operate
	 * @return minimum energy level
	 */
	public double getMinimumBudget(){
		return minimumBudget;
	}
	
	/**
	 * Set the minimum energy levels this node requires to operate
	 * @param minimumBudget minimum energy level
	 */
	protected void setMinimumBudget(double minimumBudget){
		this.minimumBudget = minimumBudget;
	}

	/**
	 * Get the position of the node
	 * @return node position as a Point2D.Double
	 */
	public Point2D.Double getPosition() {
		return position;
	}

	/**
	 * Set this node's position
	 * @param positionX x-coordinate of the node
	 * @param positionY y-coordinate of the node
	 */
	protected void setPosition(double positionX, double positionY) {
		this.position = new Point2D.Double(positionX, positionY);
	}

	/**
	 * Set this node's position
	 * @param point the coordinate of the node
	 */
	protected void setPosition(Point2D.Double point) {
		this.position = point;
	}

	/**
	 * Get this node's radius of communication
	 * @return radius of communication
	 */
	public int getCommunicationRadius() {
		return communicationRadius;
	}

	/**
	 * Set this node's radius of communication
	 * @param communicationRadius the distance the node can communicate
	 */
	protected void setCommunicationRadius(int communicationRadius) {
		this.communicationRadius = communicationRadius;
	}
}
