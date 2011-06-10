package ch.ethz.inf.vs.wot.autowot.project.security;

import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;

/**
 * 
 * Class holding data about a user
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class UserLoginData {
	
	protected String username;
	protected String password;
	protected AbstractResourceItem resource;

	public UserLoginData(String username, String password, AbstractResourceItem resource) {
		this.username = username;
		this.password = password;
		this.resource = resource;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public AbstractResourceItem getResource() {
		return resource;
	}
	
	public String getBase64Hash() {
		return org.apache.commons.codec.binary.Base64.encodeBase64String((getUsername() + ":" + getPassword()).getBytes());
	}
}
