/**
 * All the data about a node in the network
 */
package node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class Node extends NodeMSTProps implements NodeInterface {
	
	private static final int DEFAULT_COMMUNICATION_RADIUS = 10;

	// Node Structures
	private Map<NodeInterface, Double> neighbourNodes;
	
	// The corresponding thread for this node
	private NodeThread nodeThread;
	
	/**
	 * Node using the default communication radius
	 * @param nodeID the ID of the node, should be unique
	 * @param positionX the X position of the node
	 * @param positionY the Y position of the node
	 * @param energy the amount of energy remaining in the node
	 * @param minimumBudget the minimum amount of energy required for the node to operate
	 */
	public Node(int nodeID, double positionX, double positionY, double energy, double minimumBudget) {
		
		this(nodeID, positionX, positionY, energy, minimumBudget, DEFAULT_COMMUNICATION_RADIUS);
		
	}
	
	/**
	 * Node
	 * @param nodeID the node ID
	 * @param positionX the x-coordinate of the node
	 * @param positionY the y-coordinate of the node
	 * @param energy the current energy levels of the node
	 * @param comumunicationRadius the distance this node can communicate with other nodes
	 */
	public Node(int nodeID, double positionX, double positionY, double energy, double minimumBudget, int comumunicationRadius) {
		super.setNodeID(nodeID);
		super.setPosition(positionX, positionY);
		super.setEnergy(energy);
		super.setMinimumBudget(minimumBudget);
		super.setCommunicationRadius(comumunicationRadius);
		
		neighbourNodes = new HashMap<NodeInterface, Double>();
		
		super.initMSTStructures();
		
		nodeThread = new NodeThread(this);
	}

	/**
	 * 
	 * @return the neighbour nodes of this node
	 */
	public Map<NodeInterface, Double> getNeighbourNodes() {
		return neighbourNodes;
	}
	
	/**
	 * Add the node to this node's neighbourhood
	 * @param distance the distance between the two nodes
	 * @param node the node
	 */
	public void addNeighbourNode(NodeInterface node, Double distance){
		neighbourNodes.put(node, distance);
	}
	
	/**
	 * Find the node which has the minimum edge weight form this node's neighbourhood
	 * @return the node which has the least edge weight
	 */
	public NodeInterface findMinimumDistanceNeighbourNode(){
		
		NodeInterface minimumDistanceNode = null;
		double distance = 0;
		
		boolean firstEntry = true;
		for (NodeInterface node : neighbourNodes.keySet()){
			if (firstEntry) {
				distance = neighbourNodes.get(node);
				minimumDistanceNode = node;
				firstEntry = false;
			} else if (neighbourNodes.get(node) < distance){
				distance = neighbourNodes.get(node);
				minimumDistanceNode = node;
			}
		}
		
		return minimumDistanceNode;
	}
	
	/**
	 * Find the current Minimum Weighted Outgoing Edge and set it
	 */
	public void setCurrentMinimumWeightedOutgoingEdge(){
		
		
		NodeInterface minimumDistanceNode = null;
		double distance = 0;
		
		boolean firstEntry = true;
		
		Map<NodeInterface, Double> nodes = new HashMap<NodeInterface, Double>(neighbourNodes);
		
		// Remove the nodes which are already part of the MST
		nodes.keySet().removeAll(getMstChildNodes().values());
		
		for (NodeInterface node : nodes.keySet()){
			if (firstEntry) {
				distance = nodes.get(node);
				minimumDistanceNode = node;
				firstEntry = false;
			} else if (nodes.get(node) < distance){
				distance = nodes.get(node);
				minimumDistanceNode = node;
			}
		}
		
		super.setCurrentMWOE(new MWOE(minimumDistanceNode, distance));
	}
	
	@Override
	public NodeThread getNodeThread() {
		return nodeThread;
	}
	
}
