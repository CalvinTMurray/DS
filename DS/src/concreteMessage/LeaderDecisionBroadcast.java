package concreteMessage;

import message.AbstractBroadcast;
import message.MessageInterface;
import message.Payload;
import node.MWOE;
import node.Node;

public class LeaderDecisionBroadcast extends AbstractBroadcast<MWOE> {

	public LeaderDecisionBroadcast(Payload<MWOE> payload) {
		super(payload);
	}

	@Override
	public void performActionBeforeBroadcast(Node node, Payload<MWOE> payload) {
		
		node.componentMWOE = payload.getData();
		System.out.println("Setting the component MWOE for node " + node.getNodeID() + " to "  + payload.getData().getNode().getNodeID());
		
	}

	@Override
	public void performActionIfNoChildren(Node node, Payload<MWOE> payload) {
		
		// Check if this node's current MWOE is the node which the leader has selected (given in the payload)
		if (node.getLocalMWOE().getNode().getNodeID() == payload.getData().getNode().getNodeID()){
			// Add the MWOE node to the MST neighbourhood
			node.addMstNeighbourNode(payload.getData().getNode());
			
			// Sends the message to the node which is part of the other component
			MessageInterface coreMessage = new CoreMessage();
			System.out.println("Adding coreMessage to node " + node.getNodeID() + "'s next round queue");
			node.getNodeThread().addMessageToNextRoundQueue(coreMessage);
			
//			System.out.println("Processed LeaderDecisionBroadcast message for node " + node.getNodeID());
			
//			coreMessage.send(node);
		}
		
	}
}
