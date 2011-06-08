package ch.ethz.inf.vs.wot.autowot.ui.manipulatordisplay;

/**
 * Class providing the sidebar displaying possible actions for the
 * current resource
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.ui.views.UserInterface;

public class ManipulatorDisplay extends Table {
	
	protected AutoWoT application = null;
	protected UserInterface mainUI = null;
	
	//private Color bgColor = null;
	private Color childColor = new Color(Display.getCurrent(), 0, 255, 255);
	private Color getterColor = new Color(Display.getCurrent(), 0, 255, 127);
	private Color posterColor = new Color(Display.getCurrent(), 0, 191, 255);
	private Color putterColor = new Color(Display.getCurrent(), 191, 255, 0);
	private Color deleterColor = new Color(Display.getCurrent(), 255, 191, 0);
	//private Color whiteColor = new Color(Display.getCurrent(), 255, 255, 255);
	private Color deletionColor = new Color(Display.getCurrent(), 255, 100, 100);

	public ManipulatorDisplay(AutoWoT application, UserInterface mainUI, Composite arg0, int arg1) {
		super(arg0, arg1);
		this.application = application;
		this.mainUI = mainUI;
	}
	
	public void updateDisplay() {
		removeAll();
		//bgColor = whiteColor;
		TableItem item = new TableItem(this, SWT.NONE);
		item.setText("Create a new root resource...");
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.RESOURCE_STRING);
		item = new TableItem(this, SWT.NONE);
		item = new TableItem(this, SWT.NONE);

		if (application.getCurrentResource() != null) {
			item.setText("Add subresources...");
			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.CHILD_STRING);
			item.setBackground(childColor);

			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.GETTER_STRING);
			item.setBackground(getterColor);

			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.POSTER_STRING);
			item.setBackground(posterColor);

			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.PUTTER_STRING);
			item.setBackground(putterColor);

			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.DELETER_STRING);
			item.setBackground(deleterColor);

			item = new TableItem(this, SWT.NONE);

			item = new TableItem(this, SWT.NONE);
			item.setText("Modifiers...");
			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.DELETION_STRING);
			item.setBackground(deletionColor);

			if (application.getCurrentResource().getIsResource()) addResourceDragModifiers();
			else if (application.getCurrentResource().getIsGetter()) addGetterDragModifiers();
			else if (application.getCurrentResource().getIsPoster()) addPosterDragModifiers();
			else if (application.getCurrentResource().getIsPutter()) addPutterDragModifiers();
			else if (application.getCurrentResource().getIsDeleter()) addDeleterDragModifiers();
			
			item = new TableItem(this, SWT.NONE);
				
			item = new TableItem(this, SWT.NONE);
			item.setText("Access Control...");
			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.ADD_USER);
			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.EDIT_USER);
			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.DELETE_USER);
			item.setBackground(deletionColor);
			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.INHERIT_USERS);
		}
	}

	
	private void addGetterDragModifiers() {
		TableItem item = new TableItem(this, SWT.NONE);
		item.setText(Constants.RESOURCENAME_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.DESCRIPTION_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.METHOD_STRING);
	}

	private void addPosterDragModifiers() {
		TableItem item = new TableItem(this, SWT.NONE);
		item.setText(Constants.RESOURCENAME_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.DESCRIPTION_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.METHOD_STRING);
	}

	private void addPutterDragModifiers() {
		TableItem item = new TableItem(this, SWT.NONE);
		item.setText(Constants.RESOURCENAME_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.DESCRIPTION_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.METHOD_STRING);
	}

	private void addDeleterDragModifiers() {
		TableItem item = new TableItem(this, SWT.NONE);
		item.setText(Constants.RESOURCENAME_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.DESCRIPTION_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.METHOD_STRING);
	}

	private void addResourceDragModifiers() {
		TableItem item = new TableItem(this, SWT.NONE);
		item.setText(Constants.RESOURCENAME_STRING);
		item = new TableItem(this, SWT.NONE);
		item.setText(Constants.DESCRIPTION_STRING);

		if (application.getCurrentResource().asResourceItem().isCollection()) {
			item = new TableItem(this, SWT.NONE);
			item.setText(Constants.METHOD_STRING);
		}
	}
	
	@Override
	protected void checkSubclass() { 
		// Allow SWT subclassing 
	} 

}
