package ch.ethz.inf.vs.wot.autowot.project.resources;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.vs.wot.autowot.project.security.UserLoginData;

/**
 * Implementation of an abstract Resource Item, this is the base class for ResourceItem, GetterItem, PosterItem and PutterItem
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public abstract class AbstractResourceItem {
	
	protected ResourceItem parent;
	
	protected String resourceName;
	protected String resourceDescription;
		
	private Boolean isResource;
	private Boolean isGetter;
	private Boolean isPoster;
	private Boolean isPutter;
	private Boolean isDeleter;
	
	protected List<UserLoginData> authorizedUsers = new ArrayList<UserLoginData>();
	protected Boolean inheritUsers = true;
	
	/**
	 * Construct a new AbstractResourceItem
	 */
	protected AbstractResourceItem() {
		resourceName = null;
		resourceDescription = null;
	}
	
	/**
	 * Get the name of the resource
	 * @return The name of the resource
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * Set the name of the resource
	 * @param resourceName - The name to be set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;		
	}
		
	/**
	 * Get the description of the resource
	 * @return The description of the resource
	 */
	public String getDescription() {
		return resourceDescription;
	}
	
	/**
	 * Set the description of the resource
	 * @param resourceDescription - The description to be set
	 */
	public void setDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	/**
	 * Get the parent resource of the resource
	 * @return The parent resource of the resource
	 */
	public ResourceItem getParentResource() {
		return parent;
	}
	
	/**
	 * Get the resource as ResourceItem
	 * @return the resource as ResourceItem
	 */
	public ResourceItem asResourceItem() {
		return (ResourceItem) this;
	}
	
	/**
	 * Get the resource as GetterItem
	 * @return the resource as GetterItem
	 */
	public GetterItem asGetterItem() {
		return (GetterItem) this;
	}
	
	/**
	 * Get the resource as PosterItem
	 * @return the resource as PosterItem
	 */
	public PosterItem asPosterItem() {
		return (PosterItem) this;
	}
	
	/**
	 * Get the resource as PutterItem
	 * @return the resource as PutterItem
	 */
	public PutterItem asPutterItem() {
		return (PutterItem) this;
	}	

	/**
	 * Get the resource as DeleterItem
	 * @return the resource as DeleterItem
	 */
	public DeleterItem asDeleterItem() {
		return (DeleterItem) this;
	}
	
	/**
	 * Set the resource to be a ResourceItem
	 */
	public void setAsResource() {
		isResource = true;
		isGetter = false;
		isPoster = false;
		isPutter = false;
		isDeleter = false;
	}
	
	/**
	 * Get whether the resource is a ResourceItem
	 * @return whether the resource is a ResourceItem
	 */
	public boolean getIsResource() {
		return isResource;
	}
	
	/**
	 * Set the resource to be a GetterItem
	 */
	public void setAsGetter() {
		isResource = false;
		isGetter = true;
		isPoster = false;
		isPutter = false;
		isDeleter = false;
	}
	
	/**
	 * Get whether the resource is a GetterItem
	 * @return whether the resource is a GetterItem
	 */
	public boolean getIsGetter() {
		return isGetter;
	}
	
	/**
	 * Set the resource to be a PosterItem
	 */
	public void setAsPoster() {
		isResource = false;
		isGetter = false;
		isPoster = true;
		isPutter = false;
		isDeleter = false;
	}
	
	/**
	 * Get whether the resource is a PosterItem
	 * @return whether the resource is a PosterItem
	 */
	public boolean getIsPoster() {
		return isPoster;
	}
	
	/**
	 * Set the resource to be a PutterItem
	 */
	public void setAsPutter() {
		isResource = false;
		isGetter = false;
		isPoster = false;
		isPutter = true;
		isDeleter = false;
	}
	
	/**
	 * Get whether the resource is a PutterItem
	 * @return whether the resource is a PutterItem
	 */
	public boolean getIsPutter() {
		return isPutter;
	}
	
	/**
	 * Set the resource to be a DeleterItem
	 */
	public void setAsDeleter() {
		isResource = false;
		isGetter = false;
		isPoster = false;
		isPutter = false;
		isDeleter = true;
	}
	
	/**
	 * Get whether the resource is a DeleterItem
	 * @return whether the resource is a DeleterItem
	 */
	public boolean getIsDeleter() {
		return isDeleter;
	}
	
	public synchronized boolean isRestricted() {
		return !authorizedUsers.isEmpty() || (inheritUsers && !getInheritedUsers().isEmpty());
	}
	
	public synchronized void addUser(String username, String password) {
		addUser(new UserLoginData(username, password, this));
	}
	
	public synchronized void addUser(UserLoginData user) {
		if(hasUser(user.getUsername())) {
			UserLoginData userData = getUser(user.getUsername());
			userData.setUsername(user.getUsername());
			userData.setPassword(user.getPassword());
		} else {
			authorizedUsers.add(user);
		}
	}
	
	public synchronized void addUser(String userHash) {
		String plainText = new String(org.apache.commons.codec.binary.Base64.decodeBase64(userHash));
		int splitPoint = plainText.indexOf(":");
		String userName = plainText.substring(0, splitPoint);
		String password = plainText.substring(splitPoint+1, plainText.length());
		addUser(userName, password);
	}
	
	public synchronized UserLoginData getUser(String username) {
		for(UserLoginData user : authorizedUsers) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		for(UserLoginData user : getInheritedUsers()) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}
	
	protected synchronized boolean hasUser(String username) {
		for(UserLoginData user : authorizedUsers) {
			if(user.getUsername().equals(username)) {
				return true;
			}
		}
		for(UserLoginData user : getInheritedUsers()) {
			if(user.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public synchronized List<UserLoginData> getInheritedUsers() {
		if(parent != null && parent != this && inheritUsers) {
			return parent.getInheritedUsersRecursive();
		}
		return new ArrayList<UserLoginData>();
	}
	
	public synchronized List<UserLoginData> getInheritedUsersRecursive() {
		List<UserLoginData> result = getAuthorizedUsers();
		if(parent != null && parent != this && inheritUsers) {
			result.addAll(parent.getInheritedUsersRecursive());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<UserLoginData> getAuthorizedUsers() {
		return (List<UserLoginData>) ((ArrayList<UserLoginData>) authorizedUsers).clone();
	}
	
	public synchronized void deleteUser(String username) {
		for(UserLoginData user : authorizedUsers) {
			if(user.getUsername().equals(username)) {
				authorizedUsers.remove(user);
				break;
			}
		}
	}

	public Boolean getInheritUsers() {
		return inheritUsers;
	}

	public void setInheritUsers(Boolean inheritUsers) {
		this.inheritUsers = inheritUsers;
	}
}
