package ch.ethz.inf.vs.wot.autowot.core;

/**
 * Main class of an AutoWoT instance
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import org.eclipse.swt.widgets.Display;

import ch.ethz.inf.vs.wot.autowot.project.Project;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.ui.views.MainUserInterface;
import ch.ethz.inf.vs.wot.autowot.ui.views.UserInterface;

public class AutoWoT {
	
	/**
	 * The currentProject
	 */
	protected Project currentProject = null;
	
	/**
	 * The currently active resource
	 */
	protected AbstractResourceItem currentResource = null;
	
	/**
	 * Main User Interface
	 */
	protected UserInterface mainUI = null;
	
	/**
	 * Constructor
	 */
	public AutoWoT() {
		setCurrentProject(new Project());
	}
	
	/**
	 * Links user interface to application
	 */
	public void setUserInterface(UserInterface mainUI) {
		this.mainUI = mainUI;
	}
	
	/**
	 * Sets a project
	 */
	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}
	
	/**
	 * @return current project
	 */
	public Project getCurrentProject() {
		return currentProject;
	}
	
	/**
	 * Sets current resource to be manipulated or displayed
	 */
	public void setCurrentResource(AbstractResourceItem currentResource) {
		this.currentResource = currentResource;
	}
	
	/**
	 * @return current resource
	 */
	public AbstractResourceItem getCurrentResource() {
		return currentResource;
	}
	
	/**
	 * Step up one level in the resource hierarchy
	 */
	public void ascend() {
		if (currentResource != null && currentResource.getParentResource() != null) {
			currentResource = currentResource.getParentResource();
			mainUI.refresh();
		}
	}
	
	/**
	 * Entry point of the program
	 */
	public static void main(String[] args) {
		Display display = new Display();
		AutoWoT application = new AutoWoT();
		UserInterface ui = new MainUserInterface(application, display);
		application.setUserInterface(ui);
		ui.launchUIandApplication();
		display.dispose();
	}
}