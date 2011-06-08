package ch.ethz.inf.vs.wot.autowot.project.resources;

/**
 * Implementation of a Getter
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;

public class GetterItem extends AbstractResourceItem {

	protected String callbackMethod;
	protected HandlerCallbackType callbackMethodType;
	protected String onChangeMethod;
	protected HandlerCallbackType onChangeMethodType;
	
	/**
	 * Create a new GetterItem from the name, method and parent resource
	 * @param getterName - The name of the Getter
	 * @param callbackMethod - The Getter method name
	 * @param parent - The parent of the Getter
	 */
	public GetterItem(String getterName, String callbackMethod, HandlerCallbackType callbackMethodType, ResourceItem parent) {
		this.resourceName = getterName;
		this.callbackMethod = Constants.trimMethod(callbackMethod);
		this.callbackMethodType = callbackMethodType;
		this.parent = parent;
		this.onChangeMethod = null;
		
		this.setAsGetter();
	}

	/**
	 * Create a new GetterItem from the name, method, onChange method and parent resource
	 * @param getterName - The name of the Getter
	 * @param callbackMethod - The Getter method name
	 * @param getterOnChangeMethod - The onChange method name
	 * @param parent - The parent of the Getter
	 */
	public GetterItem(String getterName, String callbackMethod, HandlerCallbackType callbackMethodType, String getterOnChangeMethod, HandlerCallbackType onChangeMethodType, ResourceItem parent) {
		this.resourceName = getterName;
		this.callbackMethod = Constants.trimMethod(callbackMethod);
		this.callbackMethodType = callbackMethodType;
		this.onChangeMethod = getterOnChangeMethod;
		this.onChangeMethodType = onChangeMethodType;
		this.parent = parent;
		
		this.setAsGetter();
	}
	
	/**
	 * Get the Getter's callback method name
	 * @return the Getter's callback method name
	 */
	public String getCallbackMethod() {
		return this.callbackMethod;
	}
		
	/**
	 * Set the Getter's callback method name
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
	 * Get the Getter's onChange method name
	 * @return the Getter's onChange method name
	 */
	public String getOnChangeMethod() {
		return this.onChangeMethod;
	}
	
	public HandlerCallbackType getOnChangeMethodType() {
		return onChangeMethodType;
	}

	public void setOnChangeMethodType(HandlerCallbackType onChangeMethodType) {
		this.onChangeMethodType = onChangeMethodType;
	}
}
