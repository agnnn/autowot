package ch.ethz.inf.vs.wot.autowot.ui.views;

/**
 * Abstract class providing basic elements required by a GUI
 * Inherit from this class when designing your own interface
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.ui.manipulatordisplay.ManipulatorDisplay;
import ch.ethz.inf.vs.wot.autowot.ui.navigationdisplay.NavigationDisplay;
import ch.ethz.inf.vs.wot.autowot.ui.resourcedisplay.ResourceDisplay;

public abstract class UserInterface {
	
	public AutoWoT application = null;
	
	public Display display = null;
	public Shell shell = null;
	
	public NavigationDisplay navigationDisplay = null;
	public ResourceDisplay resourceDisplay = null;
	public ManipulatorDisplay manipulatorDisplay = null;
	
	abstract public void launchUIandApplication();
	abstract public void refresh();
	
}
