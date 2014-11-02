/**
 * 
 */
package concreteMessage;

import message.MessageInterface;
import message.Payload;
import node.Node;
import node.NodeInterface;

/**
 * The core message is a message which is sent between components. <br>
 * When the core message is received by a node it adds the node which sent the message to its MST neighbour nodes
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class CoreMessage implements MessageInterface{
	
	private NodeInterface fromNode;

	@Override
	public void performAction(Node node) {
		// add the node which sent this message to my MST neighbour nodes
		node.addMstNeighbourNode(fromNode);
		
		if (node.componentMWOE.getNode().getNodeID() == fromNode.getNodeID()){
			// We have selected each other
			
			// Leader election
			if (fromNode.getNodeID() < node.getNodeID()){
				node.setLeaderID(node.getNodeID());
//				node.readyForNextLevel = true;
				// TODO send out leader broadcast message to set all other nodes who think they are the leader to false and to set parent pointers
				// TODO could probably just have an instance variable which sets the node which is the leader.
				
				Payload<Integer> data = new Payload<Integer>(node.getLeaderID());
				MessageInterface newLeaderBroadcast = new NewLeaderBroadcast(data);
				System.out.println("Adding newLeaderBroadcast to node " + node.getNodeID() + "'s next round queue");
				node.getNodeThread().addMessageToNextRoundQueue(newLeaderBroadcast);
//				System.out.println("Processed CoreMessage for nodes " + node.getNodeID() + " and " + fromNode.getNodeID() + ". Set LeaderID: " + node.getNodeID());
				
//				newLeaderBroadcast.send(node);
			}
		}
	}

	@Override
	public void send(Node node) {
		this.fromNode = node;
		// Send the message over to the node in the other component asking it to merge
		node.getLocalMWOE().getNode().getNodeThread().addMessageToNodeQueue(this);
	}

}
