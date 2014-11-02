/**
 * 
 */
package concreteMessage;

import message.AbstractBroadcast;
import message.MessageInterface;
import message.Payload;
import node.MWOE;
import node.Node;
import node.NodeInterface;

/**
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class IdentifyNewEdgeBroadcast extends AbstractBroadcast<Object>{

	public IdentifyNewEdgeBroadcast(Payload<Object> payload) {
		super(payload);
	}

	@Override
	public void performActionBeforeBroadcast(Node node, Payload<Object> payload) {
		// get the next minimum weighted edge of the MST
		// Store it somewhere
		// TODO instead of calling this method, we need to send a test message to each of the nodes
		// which are not in my MST neighbours to check if they belong to the same component
		node.setCurrentMWOE();
		System.out.println("Setting the local MWOE for node " + node.getNodeID() + " to node " + node.getLocalMWOE().getNode().getNodeID());
		
	}

	@Override
	public void performActionIfNoChildren(Node node, Payload<Object> payload) {
		MWOE data = node.getLocalMWOE();
		MessageInterface convergecastMessage = new IdentifyNewEdgeConvergecast(data);
		System.out.println("Adding convergecastMessage to node " + node.getNodeID() + "'s next round queue");
		node.getNodeThread().addMessageToNextRoundQueue(convergecastMessage);
//		convergecastMessage.send(node);
	}
	
	@Override
	public void send(Node node) {
		if (!(node.getMstNeighbourNodes().isEmpty())){

			for (NodeInterface n : node.getMstNeighbourNodes().values()){
				n.getNodeThread().addMessageToNodeQueue(new IdentifyNewEdgeBroadcast(payload));
				System.out.println("Sent a broadcast message from " + node.getNodeID() + " to " + n.getNodeID());
			}
		} else {
			node.getNodeThread().addMessageToNodeQueue(this);
		}
	}
}
