/**
 * Parses the input file intialising nodes
 */
package main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import node.Node;

/**
 * Singleton Parser
 * @author Calvin.T.Murray
 *
 */
public class Parser {
	
	private static Parser instance;
	
	// Input node configuration positions
	private final int NODE_ID_POSITION = 1;
	private final int NODE_POSITION_X = 2;
	private final int NODE_POSITION_Y = 3;
	private final int NODE_ENERGY = 4;

	// Input broadcast configuration positions
	private final int BCST_ID = 2;
	private Map<Integer, Node> nodes = new HashMap<Integer, Node>();
	private List<Node> broadcastFrom = new ArrayList<Node>();
	
	private  Parser() {}
	
	public static Parser getInstance(){
		if (instance != null){
			return instance;
		} else {
			instance = new Parser();
			return instance;
		}
	}
	
	public void parseInput(Path filePath){
		
		// Open the resource and scan the file
		try (Scanner scan = new Scanner(filePath)) {
			
			// Expect the first line to be the minimum budget
			double minimumBudget = Double.parseDouble(scan.nextLine());

			
			String line; // The line currently being parsed
			String[] nodeConfig; // The node configuration in the input file
			String[] bcstConfig; // The broadcast configuration in the input file
			
			// Node constructor arguments
			int nodeID; // Also overwritten for the broadcast instructions
			double positionX;
			double positionY;
			double energy;

			// Create a new node from each line that starts with "node" from the input file
			while ((line = scan.findInLine("node.+")) != null && scan.hasNextLine()){

				// Get the node configuration data from the line (removing whitespace and commas)
				nodeConfig = line.split("\\s+|,\\s+");

				nodeID = Integer.parseInt(nodeConfig[NODE_ID_POSITION]);
				positionX = Double.parseDouble(nodeConfig[NODE_POSITION_X]);
				positionY = Double.parseDouble(nodeConfig[NODE_POSITION_Y]);
				energy = Double.parseDouble(nodeConfig[NODE_ENERGY]);

				Node node = new Node(nodeID, positionX, positionY, energy, minimumBudget);
				
				if (nodes.containsKey(nodeID)){
					throw new ExceptionInInitializerError("More than one node with id: " + nodeID);
				} else {
					nodes.put(nodeID, node);
				}
				
				scan.nextLine();
			}

			// Create a lit of nodes that require broadcast messages to be sent
			while ((line = scan.findInLine("bcst.+")) != null && scan.hasNextLine()){
				
				bcstConfig = line.split("\\s+|,\\s+");
				
				nodeID = Integer.parseInt(bcstConfig[BCST_ID]);
				
				broadcastFrom.add(nodes.get(nodeID));
				
				scan.nextLine();
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, Node> getNodes() {
		return nodes;
	}

	public List<Node> getBroadcastFromNodes() {
		return broadcastFrom;
	}
	
}
