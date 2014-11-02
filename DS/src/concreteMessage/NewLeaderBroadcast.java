/**
 * 
 */
package concreteMessage;

import message.AbstractBroadcast;
import message.Payload;
import node.Node;

/**
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class NewLeaderBroadcast extends AbstractBroadcast<Integer> {

	public NewLeaderBroadcast(Payload<Integer> payload) {
		super(payload);
	}

	@Override
	public void performActionBeforeBroadcast(Node node, Payload<Integer> payload) {
		node.setLeaderID(payload.getData());
		node.setParentNode(fromNode);
		node.readyForNextLevel = true;
		
	}

	@Override
	public void performActionIfNoChildren(Node node, Payload<Integer> payload) {
		// TODO i will want to perform some action after i have completed the broadcast
		System.out.println("NewLeaderBroadcast complete");
	}
}
