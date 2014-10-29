/**
 * 
 */
package message;



// TODO Not sure if we need this yet



/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public enum BroadcastType {
	// A broadcast message to identify a new edge to add to the MST
	MST_GET_MINIMUM_EDGE,
	// A broadcast message to inform all the nodes in the MST which node has the minimum edge
	MST_INFORM_NODE_OF_MINIMUM_EDGE, 
	// A broadcast message sent by the basestation to instruct the leaders to merge components
	MST_MERGE, 
	// A broadcast message which has their ID number
	MST_ID 
}
