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
public abstract class AbstractConvergecast<T> implements MessageInterface {
	
	private Payload<T> payload;
	
	/**
	 * A convergecast message with the initial payload
	 * @param data the data associated with this convergecast's payload
	 */
	public AbstractConvergecast(T data) {
		payload = new Payload<T>(data);
	}
	
	@Override
	public void performAction(Node node) {
		node.incrementReceivedConvergecastMessageCount();
		// Might replace this with a lock on the resource, such that we lock until this condition is true!!
		// Wait until we have received all the convergecast messages from our children
		
		// TODO this won't work because we're waiting on convergecast messages from its children but we're blocking,
		// so we can't process any other messages
//		while (node.getReceivedConvergecastMessageCount() < node.getMstNeighbourNodes().size()){
////			System.out.println("Waiting on receiving all my child convergecast messages! Receieved: " + 
////					node.getReceivedConvergecastMessageCount() + " of " + node.getMstNeighbourNodes().size());
//		}
		
		performActionOnReceivedPayload(node, payload);
		
		// Only change the payload if we have data to set
		if (updatePayload(node) != null) {
			payload.setData(updatePayload(node));
		}
		
		// If we aren't the leader of the component then we should pass the message on to the parent
		// Otherwise we can perform further actions on ourself
		if (node.hasParentNode()){
			node.getNodeThread().addMessageToNextRoundQueue(this);
//			this.send(node);
		} else {
			performActionIfNoParent(node, payload);
		}
		
	}
	@Override
	public void send(Node node) {
		
		if (node.hasParentNode()){
			node.getParentNode().getNodeThread().addMessageToNodeQueue(this);
		} else {
			System.out.println("Node " + node.getNodeID() + " does not have a parent node");
//			System.out.println("Node " + node.getNodeID() + " is the leader? " + node.isLeader());
			
			node.getNodeThread().addMessageToNodeQueue(this);
			
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
	 * (the updated payload is sent to the parent)
	 * @param node the node to perform the update
	 */
	public abstract T updatePayload(Node node);

	/**
	 * The action which the leader of the component should perform once the convergecast is complete
	 * @param node the leader node of the component
	 */
	public abstract void performActionIfNoParent(Node node, Payload<T> payload);
	
	/**
	 * Each node should perform this action after updating the payload 
	 * (this action is performed after the node has sent the message on to its parent)
	 * @param node the node which is to perform the action after updating the payload
	 */
	public abstract void performActionAfterConvergecast(Node node);
}