/**
 * 
 */
package main;

import internetLayer.NetworkRoutingProtocol;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import node.Node;
import node.NodeInterface;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class Main {

	/**
	 * @param args file path
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
	
		/**
		 * Input File
		 * First line (Node Configuration):
		 * Minimimum Budget - minimum battery life required for the node to continue its normal 
		 * operation (REAL Number)
		 * 
		 * Second line - X line (Node Configuration):
		 * node Z, positionX, positionY, energy - Where Z is the node ID, positionX and positionY are 
		 * the X and Y position of the node respectively (REAL Numbers) and energy is the amount of 
		 * energy left in the node (REAL Number)
		 * 
		 * X + 1 Line to Z line, where Z >= X + 1 (Communication Instructions):
		 * bcst from X - broadcast a message from node X to all its neighbours
		 */
		
		Map<Integer, Node> nodes = new HashMap<Integer, Node>();
		// TODO Don't need to do anything with this yet
//		List<Node> broadcastNodes = new ArrayList<Node>();
		
		Path filePath = Paths.get(args[0]);
		
		Parser parser = Parser.getInstance();
		
		parser.parseInput(filePath);
		nodes = parser.getNodes();
		// TODO Don't need to do anything with this yet
//		broadcastNodes = parser.getBroadcastFromNodes();
		
		// Initialise the Neighbour Discovery Protocol with the set of nodes created from the parser
		new NeighbourDiscoveryProtocol(nodes);
		
		// Network Routing Protocol
		new NetworkRoutingProtocol(nodes.size());
		
		// Initialise the Base Station
		BaseStation baseStation = new BaseStation(nodes);
		baseStation.contstrucMST();
		
		
		while (true){
			System.out.println("Number of threads running: " + Thread.activeCount());
			
			// Testing if the leader knows the node to inform that they have the minimum edge
			for (Node n : nodes.values()){
//				System.out.println("The minimum weighted edge to add for node " + n.getNodeID() + " is: " + n.componentMWOE.getNode().getNodeID());
				
				if (n.hasParentNode() && !n.getMstNeighbourNodes().isEmpty()){
					for (NodeInterface i : n.getMstNeighbourNodes().values()){
						System.out.println("The neighbour of node " + n.getNodeID() + " in the MST is: " + i.getNodeID() + " and has parent " + n.getParentNode().getNodeID());
					}
				}
			}
			
			Thread.sleep(1000);
		}
		
//		Logger log = new Logger();
//		log.bs("I'm writing to the log");
//		log.close();
//		
//		
//		for (Node n : broadcastNodes){
//			System.out.println("Broadcast a message from Node: " + n.getNodeID());
//		}
	}
}
