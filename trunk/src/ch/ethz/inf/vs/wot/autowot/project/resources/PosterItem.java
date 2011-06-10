package ch.ethz.inf.vs.wot.autowot.project.resources;

import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;

/**
 * Implementation of a Poster
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class PosterItem extends AbstractResourceItem {

	protected String callbackMethod;
	protected HandlerCallbackType callbackMethodType;
	protected String argumentType;
	protected String presentationType;
	
	/**
	 * Create a new PosterItem from the name, method, argument type, presentation type and parent resource
	 * @param posterName - The name of the Poster
	 * @param posterMethod - The Poster's callback method name
	 * @param posterArgumentType - The Poster's argument type
	 * @param posterPresentationType - The Poster's presentation type
	 * @param parent - The parent of the Poster
	 */
	public PosterItem(String posterName, String posterMethod, HandlerCallbackType callbackMethodType, String posterArgumentType, String posterPresentationType, ResourceItem parent) {
		this.resourceName = posterName;
		this.callbackMethod = posterMethod;
		this.callbackMethodType = callbackMethodType;
		this.argumentType = posterArgumentType;
		this.presentationType = posterPresentationType;		
		this.parent = parent;
		
		this.setAsPoster();
	}
	
	/**
	 * Get the Poster's callback method name
	 * @return the Poster's callback method name
	 */
	public String getCallbackMethod() {
		return this.callbackMethod;
	}
	
	/**
	 * Set the Poster's callback method name
	 * @param callbackMethod - The callback method name to be set
	 */
	public void setCallbackMethod(String callbackMethod) {
		this.callbackMethod = callbackMethod;		
	}
	
	public HandlerCallbackType getCallbackMethodType() {
		return callbackMethodType;
	}

	public void setCallbackMethodType(HandlerCallbackType callbackMethodType) {
		this.callbackMethodType = callbackMethodType;
	}
	
	/**
	 * Get the Poster's argument type
	 * @return the Poster's argument type
	 */
	public String getPosterArgumentType() {
		return this.argumentType;
	}
	
	/**
	 * Get the Poster's presentation type
	 * @return the Poster's presentation type
	 */
	public String getPosterPresentationType() {
		return this.presentationType;
	}
}
