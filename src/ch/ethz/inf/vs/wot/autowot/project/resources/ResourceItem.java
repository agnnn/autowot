package ch.ethz.inf.vs.wot.autowot.project.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;

/**
 * Implementation of a Resource Item that can have subresources
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class ResourceItem extends AbstractResourceItem {

	protected String className;
	protected String resourceURI;
	protected String resourceMethods;
	protected Boolean isCollection;
	protected String collectionMethod;
	protected HandlerCallbackType collectionMethodType;
	
	protected Map<String, ResourceItem> children;
	protected GetterItem getter;
	protected PosterItem poster;
	protected PutterItem putter;
	protected DeleterItem deleter;
	
	/**
	 * Create a new ResourceItem from the className, resourceName, resourceURI, HTTPmethods, collection property, collectionMethod and its parent
	 * @param className - The Java class name to be used for this resource
	 * @param resourceName - The resource name
	 * @param resourceURI - The resource URI
	 * @param httpMethods - The supported HTTP methods
	 * @param isCollection - The collection property
	 * @param collectionMethod - The collection method
	 * @param parent - The parent of the resource
	 */
	public ResourceItem(String className, String resourceName, String resourceURI, String httpMethods, Boolean isCollection, String collectionMethod, HandlerCallbackType collectionMethodType, ResourceItem parent) {
		this.className = className;		
		this.resourceURI = resourceURI;
		this.resourceMethods = httpMethods;
		this.resourceName = resourceName;
		this.isCollection = isCollection;
		this.collectionMethod = Constants.trimMethod(collectionMethod);
		this.collectionMethodType = collectionMethodType;
		this.parent = parent;

		children = new HashMap<String, ResourceItem>();
		this.setAsResource();
	}

	/**
	 * Get the Java class name of the resource
	 * @return the Java class name of the resource
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * Get the URI of the resource
	 * @return the URI of the resource
	 */
	public String getURI() {
		return this.resourceURI;
	}

	/**
	 * Get the HTTP methods of the resource
	 * @return the HTTP methods of the resource
	 */
	public String getMethods() {
		return this.resourceMethods;
	}

	/**
	 * Get the Children of the resource as <code>HashMap&lt;String, ResourceItem&gt</code>
	 * @return the Children of the resource as <code>HashMap&lt;String, ResourceItem&gt</code>
	 */
	public Map<String, ResourceItem> getChildren() {
		return this.children;
	}
	
	/**
	 * Get the names of the Children of this resource
	 * @return the names of the Children of this resource
	 */
	public List<String> getChildNames() {
		return new ArrayList<String>(children.keySet());
	}
	
	/**
	 * Get the Getters of the resource as <code>HashMap&lt;String, GetterItem&gt</code>
	 * @return the Getters of the resource as <code>HashMap&lt;String, GetterItem&gt</code>
	 */
	public GetterItem getGetter() {
		return getter;
	}

	/**
	 * Get the Posters of the resource as <code>HashMap&lt;String, PosterItem&gt</code>
	 * @return the Posters of the resource as <code>HashMap&lt;String, PosterItem&gt</code>
	 */
	public PosterItem getPoster() {
		return poster;
	}

	/**
	 * Get the Putters of the resource as <code>HashMap&lt;String, PutterItem&gt</code>
	 * @return the Putters of the resource as <code>HashMap&lt;String, PutterItem&gt</code>
	 */
	public PutterItem getPutter() {
		return putter;
	}
	
	/**
	 * Get the Deleters of the resource as <code>HashMap&lt;String, DeleterItem&gt</code>
	 * @return the Deleters of the resource as <code>HashMap&lt;String, DeleterItem&gt</code>
	 */
	public DeleterItem getDeleter() {
		return deleter;
	}

	
	/**
	 * Add a sub-resource to this resource
	 * @param resourceItem - The sub-resource to be added to this resource
	 */
	public void addChild(ResourceItem resourceItem) {
		children.put(resourceItem.getResourceName(), resourceItem);		
	}

	/**
	 * Add a Getter to this resource
	 * @param getterItem - The Getter to be added to this resource
	 */
	public void setGetter(GetterItem getterItem) {
		this.getter = getterItem;
	}

	/**
	 * Add a Poster to this resource
	 * @param posterItem - The Poster to be added to this resource
	 */
	public void setPoster(PosterItem posterItem) {
		if (!this.resourceMethods.contains("POST")) {
			this.resourceMethods = this.resourceMethods += ", POST";
		}
		
		this.poster = posterItem;	
	}

	/**
	 * Add a Putter to this resource
	 * @param putterItem - The Putter to be added to this resource
	 */
	public void setPutter(PutterItem putterItem) {
		if (!this.resourceMethods.contains("PUT")) {
			this.resourceMethods = this.resourceMethods += ", PUT";
		}
		
		this.putter = putterItem;
	}
	
	/**
	 * Add a Deleter to this resource
	 * @param deleterItem - The Deleter to be added to this resource
	 */
	public void setDeleter(DeleterItem deleterItem) {
		if (!this.resourceMethods.contains("DELETE")) {
			this.resourceMethods = this.resourceMethods += ", DELETE";
		}
		
		this.deleter = deleterItem;		
	}
	
	/**
	 * Get a sub-resource of this resource by its name
	 * @param resourceName - the name of the sub-resource to be returned
	 * @return a sub-resource of this resource by its name
	 */
	public ResourceItem getChild(String resourceName) {
		if (resourceName.startsWith("<")) resourceName = resourceName.substring(1, resourceName.length() - 2);
		return children.get(resourceName);
	}
	
	@Override
	public String toString() {
		return this.className + " (" + this.resourceURI + ")";		
	}

	/**
	 * Remove a sub-resource of this resource
	 * @param resourceItem - The sub-resource to be removed
	 */
	public void removeChild(ResourceItem resourceItem) {
		Set<String> childrenNames = new HashSet<String>(this.children.keySet());

		for (String child : childrenNames) {
			if (child.equalsIgnoreCase(resourceItem.getClassName())) {
				this.children.remove(child);		
			}
		}

		children.remove(resourceItem);
	}
	
	/**
	 * Get whether this resource is a collection
	 * @return whether this resource is a collection
	 */
	public boolean isCollection() {
		return isCollection;
	}

	/**
	 * Get the collection method of this resource
	 * @return the collection method of this resource
	 */
	public String getCollectionMethod() {
		if (collectionMethod == null || collectionMethod.isEmpty()) return null;
		else return collectionMethod;
	}
	
	public HandlerCallbackType getCollectionMethodType() {
		return collectionMethodType;
	}

	public void setCollectionMethodType(HandlerCallbackType collectionMethodType) {
		this.collectionMethodType = collectionMethodType;
	}

	/**
	 * Set the collection method of this resource
	 * @param returnMethod - The collection method to be set
	 */
	public void setCollectionMethod(String returnMethod) {
		this.collectionMethod = returnMethod;
	}
	
	
	public boolean hasGetter() {
		return getter != null;
	}
	
	
	public boolean hasPoster() {
		return poster != null;
	}
	
	public boolean hasPutter() {
		return putter != null;
	}
	
	public boolean hasDeleter() {
		return deleter != null;
	}

	public boolean hasChildren() {
		return !this.getChildNames().isEmpty();
	}
	
	public List<String> getPathVariables() {
		List<String> result = new LinkedList<String>();
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(getURI());
		while (matcher.find()) {
		    result.add(matcher.group());
		}
		return result;
	}
	
	public void removeGetter() {
		getter = null;
	}
	
	public void removePoster() {
		poster = null;
	}
	
	public void removePutter() {
		putter = null;
	}
	
	public void removeDeleter() {
		deleter = null;
	}
	
	public boolean hasDynamicChild() {
		
		for(String childName : children.keySet()) {
			ResourceItem child = children.get(childName);
			if(child.isCollection()) {
				return true;
			}
		}
		
		return false;
	}
	
	public Map<String, ResourceItem> getDynamicChildren() {
		Map<String, ResourceItem> children = getChildren();
		Map<String, ResourceItem> dynamicChildren = new HashMap<String, ResourceItem>();
		for(String key : children.keySet()) {
			if(children.get(key).isCollection()) {
				dynamicChildren.put(key, children.get(key));
			}
		}
		return dynamicChildren;
	}
	
	public Map<String, ResourceItem> getNonDynamicChildren() {
		Map<String, ResourceItem> children = getChildren();
		Map<String, ResourceItem> nonDynamicChildren = new HashMap<String, ResourceItem>();
		for(String key : children.keySet()) {
			if(!children.get(key).isCollection()) {
				nonDynamicChildren.put(key, children.get(key));
			}
		}
		return nonDynamicChildren;
	}
}