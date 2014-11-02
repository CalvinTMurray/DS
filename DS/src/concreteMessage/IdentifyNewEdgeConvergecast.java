/**
 * 
 */
package concreteMessage;

import message.AbstractConvergecast;
import message.MessageInterface;
import message.Payload;
import node.MWOE;
import node.Node;

/**
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class IdentifyNewEdgeConvergecast extends AbstractConvergecast<MWOE>{

	public IdentifyNewEdgeConvergecast(MWOE data) {
		super(data);
	}

	@Override
	public void performActionOnReceivedPayload(Node node, Payload<MWOE> payload) {
		// Add the payload to received child payloads
		node.addPayload(payload);
		
	}

	@Override
	public MWOE updatePayload(Node node) {
		
		// 2) Return that payload (which is the payload that will replace the payload
		// in the convergecast message, which is to be sent to this node's parent)
		
		return null;
	}

	@Override
	public void performActionIfNoParent(Node node, Payload<MWOE> payload) {
		
		// Broadcast the componentMWOE to the rest of the component
		MessageInterface leaderDecisionBroadcast = new LeaderDecisionBroadcast(payload);
		System.out.println("Adding leaderDecisionBroadcast to node " + node.getNodeID() + "'s next round queue");
		node.getNodeThread().addMessageToNextRoundQueue(leaderDecisionBroadcast);
		
//		System.out.println("Processed IdentifyNewEdgeConvergecast for node " + node.getNodeID());
	}

	@Override
	public void performActionAfterConvergecast(Node node) {
		// Remove this node's parent pointer.
		// We remove the parent pointer because we want to perform a broadcast when a number of components merge
		node.setParentNode(null);
	}
}
