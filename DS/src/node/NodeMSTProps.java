/**
 * All of the properties that are associated with the construction of the MST
 */
package node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import message.Payload;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 * @param <T>
 *
 */
public class NodeMSTProps extends NodeGeneralProps {

	// MST Structures
	private int componentID;
	private boolean leaderOfComponent;
	private TreeMap<Integer, NodeInterface> mstChildNodes;
	private NodeInterface mstParentNode;
	
	private int receivedConvergecastMessages;
	
	// TODO variable for the nextMinimumWeightedEdge in the MST
	private MWOE currentMWOE;
	// TODO variable that stores the payloads received from convergecast messages
	// so that they can be compared to find the payload that contains the minimum weighted edge
	private List<Payload<?>> receivedPayloads;
	
	
	public MWOE minimumWeightedOutgoingEdgeOfTheComponent;
	
	

	/**
	 * Initialise the MST data structures
	 */
	protected void initMSTStructures(){
		leaderOfComponent = true;
		mstChildNodes = new TreeMap<Integer, NodeInterface>(Collections.reverseOrder());
		receivedPayloads = new ArrayList<Payload<?>>();
	}
	
	/**
	 * 
	 * @return the component ID that this node belongs to
	 */
	public int getComponentID() {
		return componentID;
	}

	/**
	 * Set the component ID this node belong to
	 * @param componentID
	 */
	public void setComponentID(int componentID) {
		this.componentID = componentID;
	}
	
	/**
	 * 
	 * @return whether this node is the current leader in its component
	 */
	public boolean isLeader() {
		return leaderOfComponent;
	}

	/**
	 * Set whether this node is the leader of the component
	 * @param leader
	 */
	public void setLeader(boolean leader) {
		this.leaderOfComponent = leader;
	}
	
	/**
	 * Get all the child nodes for this node's MST
	 * @return the child nodes of this node
	 */
	public TreeMap<Integer, NodeInterface> getMstChildNodes() {
		return mstChildNodes;
	}
	
	/**
	 * Add a child node to this node for this MST
	 * @param node the child node
	 */
	public void addMstChildNode(NodeInterface node){
		mstChildNodes.put(node.getNodeID(), node);
	}

	/**
	 * 
	 * @return the parent node of this node in the current MST
	 */
	public NodeInterface getParentNode() {
		return mstParentNode;
	}

	/**
	 * Set the parent node of this node in the current MST
	 * @param parentNode
	 */
	public void setParentNode(NodeInterface parentNode) {
		this.mstParentNode = parentNode;
	}
	
	/**
	 * 
	 * @return whether it is true that this node has a parent
	 */
	public boolean hasParentNode(){
		return mstParentNode != null;
	}
	
	/**
	 * Increment the count of the number of received convergecast messages by 1
	 */
	public void incrementReceivedConvergecastMessageCount(){
		this.receivedConvergecastMessages++;
	}
	
	/**
	 * Reset the count of the number of received convergecast messages to 0
	 */
	public void resetReceivedConvergecastMessageCount(){
		this.receivedConvergecastMessages = 0;
	}
	
	/**
	 * 
	 * @return the current count of received convergecast messages
	 */
	public int getReceivedConvergecastMessageCount(){
		return this.receivedConvergecastMessages;
	}

	/**
	 * 
	 * @return the current Minimum Weighted Outgoing Edge
	 */
	public MWOE getCurrentMWOE() {
		return currentMWOE;
	}
	
	/**
	 * Set the current Minimum Weighted Outgoing Edge
	 * @param currentMWOE
	 */
	protected void setCurrentMWOE(MWOE currentMWOE) {
		this.currentMWOE = currentMWOE;
	}
	
	/**
	 * 
	 * @return the payloads which this node has received from its MST child nodes
	 */
	public List<Payload<?>> getReceivedPayloads(){
		return receivedPayloads;
	}
	
	/**
	 * Add a child's payload to this node's received payloads
	 * @param payload
	 */
	public void addPayload(Payload<?> payload){
		receivedPayloads.add(payload);
	}
	
	/**
	 * Empty the received payloads
	 */
	public void clearReceivedPayloads(){
		this.receivedPayloads = new ArrayList<Payload<?>>();
	}
}