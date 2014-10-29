/**
 * The Base Station which has control over the whole network
 */
package main;

import java.util.Map;

import message.Broadcast;
import message.Convergecast;
import message.Discover;
import message.MessageInterface;
import message.Payload;
import node.Node;
import node.NodeInterface;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class BaseStation {
	
	private static Map<Integer, Node> nodesInTheNetwork;
	private static int numberOfComponents;
	private static int nodeReadyForNextLevel;
	
	public BaseStation(Map<Integer, Node> nodes){
		BaseStation.nodesInTheNetwork = nodes;
	}
	
	public static void instructDiscovery(){
		// Instruct each node to send a discover message
		for (Node n : nodesInTheNetwork.values()){
			n.getNodeThread().start();
			MessageInterface discovery = new Discover();
			discovery.send(n);
			System.out.println("Sent discovery message for node " + n.getNodeID());
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Retrieve the neighbour nodes for each node in the network
		for (Node n : nodesInTheNetwork.values()){
			for (NodeInterface v : n.getNeighbourNodes().keySet()){
				System.out.println("Node ID: " + n.getNodeID() + " Neighbour Node: " + v.getNodeID() + " ComponentID: " + n.getComponentID());
			}
		}
	}

	public static void contstrucMST(){
		BaseStation.initComponentID();
		BaseStation.instructDiscovery();
		BaseStation.findMinimumWeightedEdge();
		
		
	}
	
	private static void findMinimumWeightedEdge(){
		for (Node n : nodesInTheNetwork.values()){
			
			// This is basicall a broadcast message that performs a convergecast message as its final step
			MessageInterface broadcastMessage = new Broadcast() {
				
				@Override
				public void performActionBeforeBroadcast(Node node) {
					// get the next minimum weighted edge of the MST
					// Store it somewhere
				}
				
				@Override
				public void performActionAfterBroadcast(Node node) {
					// After the broadcast is complete perform a convergecast
					// for the leader to find out which node has the minimum edge
					
					// This is the data for the payload
					// The data should actually be a tuple (NodeInterface, edgeWeight)
					// TODO Create an object which will contain this tuple
					int data = 0;
					MessageInterface convergeMessage = new Convergecast<Integer>(data){

						@Override
						public void performActionAtLeader(Node node, Payload<Integer> payload) {
							// Store the node which has the minimum edge in a variable
							// to be accessed in the next step of the MST construction
						}

						@Override
						public Integer updatePayload(Node node) {
							// 1) Get the next minimum weighted edge of this node, and add it to the list of 
							// received payloads from its children
							// 2) Compare all the payloads that were received from this node's children (including 
							// the payload which was added in the previous step) and select the payload with 
							// the minimum weighted edge payload
							// 3) Return that payload (which is the payload that will replace the payload
							// in the convergecast message, which is to be sent to this node's parent)
							
							return null;
						}
						
					};
					// Send the convergecast (note this is only sent from the leaf nodes)
					convergeMessage.send(node);
				}
			};
			
			// Broadcast the message with the structure above
			broadcastMessage.send(n);
		}
	}
	
	public static synchronized void ready(){
		BaseStation.nodeReadyForNextLevel++;
	}
	
	public static void initComponentID(){
		for (Node n : nodesInTheNetwork.values()){
			n.setComponentID(++numberOfComponents);
		}
	}
	
}