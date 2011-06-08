package ch.ethz.inf.vs.wot.autowot.project.resources;

/**
 * Implementation of a Deleter
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;

public class DeleterItem extends AbstractResourceItem {

	private String callbackMethod;
	protected HandlerCallbackType callbackMethodType;
	
	/**
	 * Create a new GetterItem from the name, method and parent resource
	 * @param deleterName - The name of the Deleter
	 * @param callbackMethod - The Deleter method name
	 * @param parent - The parent of the Deleter
	 */
	public DeleterItem(String deleterName, String callbackMethod, HandlerCallbackType callbackMethodType, ResourceItem parent) {
		this.resourceName = deleterName;
		this.callbackMethod = Constants.trimMethod(callbackMethod);
		this.callbackMethodType = callbackMethodType;
		this.parent = parent;
		
		this.setAsDeleter();
	}

	/**
	 * Get the Deleter's callback method name
	 * @return the Deleter's callback method name
	 */
	public String getCallbackMethod() {
		return this.callbackMethod;
	}
	
	/**
	 * Set the Deleter's callback method name
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
}
