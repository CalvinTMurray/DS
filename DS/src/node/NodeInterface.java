/**
 * NodeInterface is used to perform polymorphism, which hides the underlying functionality of the node
 * from its neighbours.  These methods are the only methods that neighbour nodes should have access to.
 */
package node;

/**
 * @author Calvin.T.Murray (S1126659)
 *
 */
public interface NodeInterface {

	/**
	 * Get the node ID of this node
	 * @return
	 */
	int getNodeID();

	/**
	 * Get the thread which is responsible for this node
	 * The thread can be used to communicate with this node
	 * @return 
	 */
	NodeThread getNodeThread();
	
}