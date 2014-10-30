/**
 * An abstract convergecast Message which sends a payload to the leader of a component
 * The payload may get updated at each parent nodes
 * 
 * Three things need to be specified:
 * 1) the data which should be sent in the convergecast
 * 2) the action to perform at each parent node to update the payload if necessary (null otherwise)
 * 3) the action to perform at the leader. Normally includes doing something with the payload.
 */
package message;

import node.Node;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 * @param <T> the data associated with this convergecast
 *
 */
public abstract class Convergecast<T> implements MessageInterface {
	
	private Payload<T> payload;
	
	/**
	 * A convergecast message with the initial payload
	 * @param data the data associated with this convergecast's payload
	 */
	public Convergecast(T data) {
		payload = new Payload<T>(data);
	}
	
	@Override
	public void performAction(Node node) {
		node.incrementReceivedConvergecastMessageCount();
		// Might replace this with a lock on the resource, such that we lock until this condition is true!!
		// Wait until we have received all the convergecast messages from our children
		
		while (node.getReceivedConvergecastMessageCount() < node.getMstChildNodes().size()){
			System.out.println("Waiting on receiving all my child convergecast messages! Receieved: " + 
					node.getReceivedConvergecastMessageCount() + " of " + node.getMstChildNodes().size());
		}
		
		// Reset the number of messages received from this node's children
		node.resetReceivedConvergecastMessageCount();
		
		performActionOnReceivedPayload(node, payload);
		
		// Only change the payload if we have data to set
		if (updatePayload(node) != null) {
			payload.setData(updatePayload(node));
		}
		
		// If we aren't the leader of the component then we should pass the message on to the parent
		// Otherwise we can perform further actions on ourself
		if (node.hasParentNode()){
			this.send(node);
		} else {
			performActionIfNoParent(node, payload);
		}
		
		// IMPORTANT!!! 
		// Clear the node's payloads so that another message can use this node's payloads
		node.clearReceivedPayloads();
	}
	
	@Override
	public void send(Node node) {
		
		if (node.hasParentNode()){
			node.getParentNode().getNodeThread().addMessageToNodeQueue(this);
		} else {
			System.out.println("Node " + node.getNodeID() + " does not have a parent node");
			System.out.println("I'm the leader? " + node.isLeader());
			
			node.getNodeThread().addMessageToNodeQueue(this);
			
//			if (node.isLeader()) {
//				node.getNodeThread().addMessageToNodeQueue(this);
//			} else {
//				throw new Error("Can't complete convergecast. Node " + node.getNodeID() + " does not have a parent node and not the leader");
//			}
		}
	}
	
	/**
	 * Each parent node should perform this action before updating the payload
	 * @param node the node which should perform the action
	 * @param payload the payload received from one of its child nodes
	 */
	public abstract void performActionOnReceivedPayload(Node node, Payload<T> payload);

	/**
	 * How each parent node should update the payload when they receive the convergecast message
	 * @param node the node to perform the update
	 */
	public abstract T updatePayload(Node node);

	/**
	 * The action which the leader of the component should perform once the convergecast is complete
	 * @param node the leader node of the component
	 */
	public abstract void performActionIfNoParent(Node node, Payload<T> payload);
	
}