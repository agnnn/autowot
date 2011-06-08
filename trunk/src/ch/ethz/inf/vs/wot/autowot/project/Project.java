package ch.ethz.inf.vs.wot.autowot.project;

/**
 * 
 * Class holding data about a AutoWoT project
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import java.util.List;

import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;

public class Project {
	
	private String projectName = "MyWebProject";
	private String packageCanonical = "ch.ethz.inf.vs.projectname";
	private String handlerCanonical = "ch.ethz.inf.vs.projectname.Handler";
	private Boolean makeStandalone = false;
	
	private ResourceItem rootResource = null;
	
	private List<String> bannedNames = null;

	public void setRootResource(ResourceItem rootResource) {
		this.rootResource = rootResource;
	}

	public ResourceItem getRootResource() {
		return rootResource;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setPackageCanonical(String packageCanonical) {
		this.packageCanonical = packageCanonical;
	}

	public String getPackageCanonical() {
		return packageCanonical;
	}

	public void setHandlerCanonical(String handlerCanonical) {
		this.handlerCanonical = handlerCanonical;
	}

	public String getHandlerCanonical() {
		return handlerCanonical;
	}

	public void setMakeStandalone(Boolean makeStandalone) {
		this.makeStandalone = makeStandalone;
	}

	public Boolean getMakeStandalone() {
		return makeStandalone;
	}

	public void setBannedNames(List<String> bannedNames) {
		this.bannedNames = bannedNames;
	}

	public List<String> getBannedNames() {
		return bannedNames;
	}

}
