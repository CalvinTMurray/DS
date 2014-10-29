/**
 * A generic payload which can contain any type of data
 */
package message;

/**
 * 
 * @author Calvin.T.Murray (S1126659)
 *
 */
public class Payload<T> {

	private T data;
	
	/**
	 * Create a new payload of any type
	 * @param data
	 */
	public Payload(T data){
		this.data = data;
	}
	
	/**
	 * 
	 * @return return the data this payload contains
	 */
	public T getData(){
		return data;
	}

	/**
	 * Set the data of this payload 
	 * @param data the data to be associated with this payload
	 */
	public void setData(T data){
		this.data = data;
	}
}
