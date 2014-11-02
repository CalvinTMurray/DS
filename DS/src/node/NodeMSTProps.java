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
	private TreeMap<Integer, NodeInterface> mstNeighbourNodes;
	private NodeInterface mstParentNode;
	
	private int receivedConvergecastMessages;
	
	// The Minimum Weighted Outgoing Edge of this node
	protected MWOE localMWOE;
	private List<Payload<?>> receivedPayloads;
	
	// Set by the leader, other nodes use this when another node tries to merge components
	// Used for leader election
	public MWOE componentMWOE;
	
	protected int leaderOfComponent;
	
	public boolean readyForNextLevel;
	

	/**
	 * Initialise the MST data structures
	 */
	protected void initMSTStructures(){
		leaderOfComponent = this.getNodeID();
		mstNeighbourNodes = new TreeMap<Integer, NodeInterface>();
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
	 * Set whether this node is the leader of the component
	 * @param leader
	 */
	public void setLeaderID(int leader) {
		this.leaderOfComponent = leader;
	}
	
	public int getLeaderID(){
		return leaderOfComponent;
	}
	
	/**
	 * Get all the child nodes for this node's MST
	 * @return the child nodes of this node
	 */
	public synchronized TreeMap<Integer, NodeInterface> getMstNeighbourNodes() {
		return mstNeighbourNodes;
	}
	
	/**
	 * Add a child node to this node for this MST
	 * @param node the child node
	 */
	public void addMstNeighbourNode(NodeInterface node){
		mstNeighbourNodes.put(node.getNodeID(), node);
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
	public MWOE getLocalMWOE() {
		return localMWOE;
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