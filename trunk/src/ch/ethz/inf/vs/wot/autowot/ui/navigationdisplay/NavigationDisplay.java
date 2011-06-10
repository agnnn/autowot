package ch.ethz.inf.vs.wot.autowot.ui.navigationdisplay;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;
import ch.ethz.inf.vs.wot.autowot.ui.views.UserInterface;

/**
 * Class providing the navigation displaying the tree structure of
 * the project
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class NavigationDisplay extends TreeViewer {
	
	protected AbstractResourceItem invisibleRoot = new ResourceItem("", "", "", "", false, "", HandlerCallbackType.NONE, null);
	protected final AutoWoT application;
	protected final UserInterface mainUI;
	
	public NavigationDisplay(Composite parent, final UserInterface mainUI, final AutoWoT application) {
		super(parent);
		this.application = application;
		this.mainUI = mainUI;
		setContentProvider(new NavigationDisplayContentProvider(application, invisibleRoot));
		setLabelProvider(new NavigationDisplayLabelProvider());
		addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection selection = (TreeSelection) event.getSelection();
				if((AbstractResourceItem) selection.getFirstElement() != null) {
					application.setCurrentResource((AbstractResourceItem) selection.getFirstElement());
					mainUI.refresh();
				}
			}
			
		});
		setInput(invisibleRoot);
		expandAll();
	}
	
}
