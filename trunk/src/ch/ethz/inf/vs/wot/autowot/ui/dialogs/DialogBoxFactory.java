package ch.ethz.inf.vs.wot.autowot.ui.dialogs;

/**
 * Utility class providing different dialog boxes
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.ethz.inf.vs.wot.autowot.builders.BuildMode;
import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.commons.ToolTips;
import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;
import ch.ethz.inf.vs.wot.autowot.project.security.UserLoginData;

public class DialogBoxFactory {
	
	protected AutoWoT application = null;

	public String returnName = null;
	public String returnURI = null;
	public String returnDescription = null;
	public String returnMethod = null;
	public HandlerCallbackType returnMethodType = HandlerCallbackType.NONE;
	public String returnArgumentType = null;
	public String returnShowType = null;
	public Boolean returnIsCollection = false;
	public Boolean returnIsDynamicChild = false;
	public Boolean returnIsStandalone = false;
	public String returnCollectionMethod = null;
	public HandlerCallbackType returnCollectionMethodType = HandlerCallbackType.NONE;
	public String returnOnChangeMethod = null;
	public HandlerCallbackType returnOnChangeMethodType = HandlerCallbackType.NONE;
	public BuildMode returnBuildMode = null;
	public String returnJavaPackage = null;
	public String returnHandlerPackage = null;
	public String returnUsername = null;
	public String returnPassword = null;
	
	protected Shell parentShell = null;
	public Boolean cancelled = true;
	
	/**
	 * Creates a new DialogBoxFactory
	 * @param parentShell - The parent shell of the dialog boxes 
	 */
	public DialogBoxFactory(AutoWoT application, Shell parentShell) {
		this.parentShell = parentShell;
		this.application = application;
	}
	
	public void getUserData() {
		final Shell dialog = prepareDialog("Add a new user");
		GridData data;

		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("Login:");
		data = new GridData();
		resNameLabel.setLayoutData (data);

		final Text resNameText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setLayoutData (data);

		Label resPasswordLabel = new Label (dialog, SWT.NONE);
		resPasswordLabel.setText ("Password:");
		data = new GridData();
		resPasswordLabel.setLayoutData (data);
		
		final Text resPasswordText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resPasswordText.setLayoutData (data);
		resPasswordText.setEchoChar('*');
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Create User!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnUsername = resNameText.getText().trim();
				returnPassword = resPasswordText.getText().trim();
				
				if (returnUsername.isEmpty()) {
					System.out.println("Login cannot be empty!");
					return;
				}
				if (returnPassword.isEmpty()) {
					System.out.println("Password cannot be empty!");
					return;
				}
				if (returnUsername.contains(" ") || returnUsername.contains(":")) {
					System.out.println("Login can only consits of \"a-z, A-Z, 0-9\"!");
					return;
				}
				
				cancelled = false;
				dialog.close();
			}
		});
		
				
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	public void modifyUserData(UserLoginData user) {
		final Shell dialog = prepareDialog("Modify a user");
		GridData data;

		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("Login:");
		data = new GridData();
		resNameLabel.setLayoutData (data);

		final Text resNameText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setText(user.getUsername());
		resNameText.setLayoutData (data);
		resNameText.setEditable(false);
		resNameText.setEnabled(false);

		Label resPasswordLabel = new Label (dialog, SWT.NONE);
		resPasswordLabel.setText ("Password:");
		data = new GridData();
		resPasswordLabel.setLayoutData (data);
		
		final Text resPasswordText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resPasswordText.setLayoutData (data);
		resPasswordText.setEchoChar('*');
		resPasswordText.setText(user.getPassword());
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Update User!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnUsername = resNameText.getText().trim();
				returnPassword = resPasswordText.getText().trim();
				
				if (returnUsername.isEmpty()) {
					System.out.println("Login cannot be empty!");
					return;
				}
				if (returnPassword.isEmpty()) {
					System.out.println("Password cannot be empty!");
					return;
				}
				if (returnUsername.contains(" ") || returnUsername.contains(":")) {
					System.out.println("Login can only consits of \"a-z, A-Z, 0-9\"!");
					return;
				}
				
				cancelled = false;
				dialog.close();
			}
		});
		
				
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}

	/**
	 * Get the necessary data to create a new child resource
	 */
	public void getChildData() {
		final Shell dialog = prepareDialog("Create new Resource");

		GridData data;
		
		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("Name:");
		data = new GridData();
		resNameLabel.setLayoutData (data);
		final Text resNameText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setLayoutData (data);
		
		Label resURILabel = new Label (dialog, SWT.NONE);
		resURILabel.setText ("Relative URI of the resource:");
		data = new GridData();
		resURILabel.setLayoutData (data);
		final Text resURIText = new Text (dialog, SWT.BORDER);
		resURIText.setText("/");
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resURIText.setLayoutData(data);
		
		Label resTypeLabel = new Label (dialog, SWT.NONE);
		resTypeLabel.setText ("Type of the resource:");
		data = new GridData();
		resTypeLabel.setLayoutData (data);
		
		final Button resNoTypeButton = new Button (dialog, SWT.RADIO);
		data = new GridData ();
		resNoTypeButton.setText("Auto");
		resNoTypeButton.setLayoutData(data);
		resNoTypeButton.setSelection(true);
		
		
		final Button resDynamicButton = new Button (dialog, SWT.RADIO);
		data = new GridData ();
		resDynamicButton.setText("Dynamic Child");
		resDynamicButton.setLayoutData(data);
		
		final Button resCollectionButton = new Button (dialog, SWT.RADIO);
		data = new GridData ();
		resCollectionButton.setText("Collection");
		resCollectionButton.setLayoutData(data);
		
		
		Label resColMethodLabel = new Label (dialog, SWT.NONE);
		resColMethodLabel.setText ("Method to load the collection:");
		data = new GridData();
		resColMethodLabel.setLayoutData (data);
		final Text resColMethodText = new Text (dialog, SWT.BORDER);
		resColMethodText.setText("Not required!");
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resColMethodText.setLayoutData(data);
		resColMethodText.setEnabled(false);
		

		new Label (dialog, SWT.NONE);
		final Combo collectionMethodTypeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		collectionMethodTypeCombo.setLayoutData(data);
		collectionMethodTypeCombo.add("Java");
		collectionMethodTypeCombo.add("Shell Script");
		collectionMethodTypeCombo.add("Python");
		collectionMethodTypeCombo.select(0);
		collectionMethodTypeCombo.setEnabled(false);
		
		resNoTypeButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				resColMethodText.setEnabled(false);
				resColMethodText.setText("Not required!");
				collectionMethodTypeCombo.setEnabled(false);
			}
			
		});
		
		resCollectionButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				resColMethodText.setEnabled(true);
				collectionMethodTypeCombo.setEnabled(true);
				resColMethodText.setText("");
				
				if(resCollectionButton.getSelection()) {
					resURIText.setText("/" + resNameText.getText().toLowerCase() + "/{" + resNameText.getText().toLowerCase() + "}");
				} else if (resDynamicButton.getSelection()) {
					resURIText.setText("/{" + resNameText.getText().toLowerCase() + "}");
				} else if (resNoTypeButton.getSelection()) {
					resURIText.setText("/" + resNameText.getText().toLowerCase());
				}
			}
			
		});
		
		resDynamicButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				resColMethodText.setEnabled(true);
				collectionMethodTypeCombo.setEnabled(true);
				resColMethodText.setText("");
				
				if(resCollectionButton.getSelection()) {
					resURIText.setText("/" + resNameText.getText().toLowerCase() + "/{" + resNameText.getText().toLowerCase() + "}");
				} else if (resDynamicButton.getSelection()) {
					resURIText.setText("/{" + resNameText.getText().toLowerCase() + "}");
				} else if (resNoTypeButton.getSelection()) {
					resURIText.setText("/" + resNameText.getText().toLowerCase());
				}
			}
			
		});
		
		resNameText.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(resCollectionButton.getSelection()) {
					resURIText.setText("/" + resNameText.getText().toLowerCase() + "/{" + resNameText.getText().toLowerCase() + "}");
				} else if (resDynamicButton.getSelection()) {
					resURIText.setText("/{" + resNameText.getText().toLowerCase() + "}");
				} else if (resNoTypeButton.getSelection()) {
					resURIText.setText("/" + resNameText.getText().toLowerCase());
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {	}
		});
		
		
		resURIText.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {				
				if(resCollectionButton.getSelection()) {
					if(!java.util.regex.Pattern.matches("/(.*?)/\\{(.*?)\\}", resURIText.getText())) {
						resURIText.setText("/" + resNameText.getText().toLowerCase() + "/{" + resNameText.getText().toLowerCase() + "}");
					}
				} else if (resDynamicButton.getSelection()) {
					if(!java.util.regex.Pattern.matches("/\\{(.*?)\\}", resURIText.getText())) {
						resURIText.setText("/{" + resNameText.getText().toLowerCase() + "}");
					}
				} else if (resNoTypeButton.getSelection()) {
					if(!resURIText.getText().startsWith("/")) {
						resURIText.setText("/" + resNameText.getText().toLowerCase());
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {	}
		});
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Create Resource!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnName = resNameText.getText().trim();
				returnURI = resURIText.getText().trim();
				returnMethod = "GET";
				returnIsCollection = resCollectionButton.getSelection();
				returnIsDynamicChild = resDynamicButton.getSelection();
				if (returnIsCollection || returnIsDynamicChild) {
					returnCollectionMethod = Constants.trimMethod(resColMethodText.getText());
					if(collectionMethodTypeCombo.getText().equalsIgnoreCase("Java")) {
						returnCollectionMethodType = HandlerCallbackType.JAVA;
					} else if(collectionMethodTypeCombo.getText().equalsIgnoreCase("Shell Script")) {
						returnCollectionMethodType = HandlerCallbackType.SHELL;
					} else  if(collectionMethodTypeCombo.getText().equalsIgnoreCase("Python")) {
						returnCollectionMethodType = HandlerCallbackType.PYTHON;
					} else {
						returnCollectionMethodType = HandlerCallbackType.JAVA;
					}
				} else {
					returnCollectionMethod = "";
					returnCollectionMethodType = HandlerCallbackType.NONE;
				}
				
				if (returnName.isEmpty()) {
					System.out.println("Name cannot be empty!");
					return;
				}
				
				if (returnURI.isEmpty()) {
					System.out.println("URI cannot be empty!");
					return;
				}
				
				if (returnMethod.isEmpty()) {
					System.out.println("Method cannot be empty!");
					return;
				}
				
				if ((returnIsCollection || returnIsDynamicChild) && returnCollectionMethod.isEmpty()) {
					System.out.println("Methods cannot be empty!");
					return;
				}
				
				if (!returnURI.startsWith("/")) returnURI = "/" + returnURI;
				cancelled = false;
				dialog.close();
			}
		});

		dialog.setToolTipText(ToolTips.GET_CHILD_DATA_TOOLTIP);
		resNameLabel.setToolTipText(ToolTips.GET_CHILD_DATA_RESOURCE_NAME_TOOLTIP);
		resURILabel.setToolTipText(ToolTips.GET_CHILD_DATA_RESOURCE_URI_TOOLTIP);
		resTypeLabel.setToolTipText(ToolTips.GET_CHILD_DATA_RESOURCE_TYPE_TOOLTIP);
		
		resColMethodLabel.setToolTipText(ToolTips.GET_CHILD_COLLECTION_METHOD_TOOLTIP);
		
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep();
		}
	}

	/**
	 * Get the necessary data to create a new getter
	 */
	public void getGetterData() {
		final Shell dialog = prepareDialog("Create new Getter");
		GridData data;

		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("Name:");
		data = new GridData();
		resNameLabel.setLayoutData (data);
		final Text resNameText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setLayoutData (data);

		Label resMethodLabel = new Label (dialog, SWT.NONE);
		resMethodLabel.setText ("Callback Method:");
		data = new GridData();
		resMethodLabel.setLayoutData (data);
		final Text resMethodText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resMethodText.setLayoutData (data);
		
		new Label (dialog, SWT.NONE);
		final Combo methodTypeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		methodTypeCombo.setLayoutData(data);
		methodTypeCombo.add("Java");
		methodTypeCombo.add("Shell Script");
		methodTypeCombo.add("Python");
		methodTypeCombo.select(0);
		
		Label resOnChangeMethodLabel = new Label (dialog, SWT.NONE);
		resOnChangeMethodLabel.setText ("OnChange Method:");
		data = new GridData();
		resOnChangeMethodLabel.setLayoutData (data);
		final Text resOnChangeMethodText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resOnChangeMethodText.setLayoutData (data);
		
		new Label (dialog, SWT.NONE);
		final Combo onChangeMethodTypeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		onChangeMethodTypeCombo.setLayoutData(data);
		onChangeMethodTypeCombo.add("Java");
		onChangeMethodTypeCombo.add("Shell Script");
		onChangeMethodTypeCombo.add("Python");
		methodTypeCombo.select(0);
		onChangeMethodTypeCombo.setEnabled(false);
		
		resOnChangeMethodText.setEnabled(false);
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Create Getter!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnName = resNameText.getText().trim();
				returnMethod = resMethodText.getText().trim();
				if(methodTypeCombo.getText().equalsIgnoreCase("Java")) {
					returnMethodType = HandlerCallbackType.JAVA;
				} else if(methodTypeCombo.getText().equalsIgnoreCase("Shell Script")) {
					returnMethodType = HandlerCallbackType.SHELL;
				} else  if(methodTypeCombo.getText().equalsIgnoreCase("Python")) {
					returnMethodType = HandlerCallbackType.PYTHON;
				} else {
					returnMethodType = HandlerCallbackType.JAVA;
				}
				
				returnOnChangeMethod = resOnChangeMethodText.getText().trim();
				if(onChangeMethodTypeCombo.getText().equalsIgnoreCase("Java")) {
					returnOnChangeMethodType = HandlerCallbackType.JAVA;
				} else if(onChangeMethodTypeCombo.getText().equalsIgnoreCase("Shell Script")) {
					returnOnChangeMethodType = HandlerCallbackType.SHELL;
				} else  if(onChangeMethodTypeCombo.getText().equalsIgnoreCase("Python")) {
					returnOnChangeMethodType = HandlerCallbackType.PYTHON;
				} else {
					returnOnChangeMethodType = HandlerCallbackType.JAVA;
				}
				
				if (returnName.isEmpty()) {
					System.out.println("Name cannot be empty!");
					return;
				}
				if (returnMethod.isEmpty()) {
					System.out.println("Method cannot be empty!");
					return;
				}

				cancelled = false;
				dialog.close();
			}
		});
		
		dialog.setToolTipText(ToolTips.GET_GETTER_DATA_TOOLTIP);
		resNameLabel.setToolTipText(ToolTips.GET_GETTER_DATA_RESOURCE_NAME_TOOLTIP);
		resMethodLabel.setToolTipText(ToolTips.GET_GETTER_COLLECTION_METHOD_TOOLTIP);
				
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	/**
	 * Get the necessary data to create a new getter
	 */
	public void getDeleterData() {
		final Shell dialog = prepareDialog("Create new Deleter");
		GridData data;

		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("Name:");
		data = new GridData();
		resNameLabel.setLayoutData (data);
		final Text resNameText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setLayoutData (data);

		Label resMethodLabel = new Label (dialog, SWT.NONE);
		resMethodLabel.setText ("Callback Method:");
		data = new GridData();
		resMethodLabel.setLayoutData (data);
		final Text resMethodText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resMethodText.setLayoutData (data);

		new Label (dialog, SWT.NONE);
		final Combo methodTypeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		methodTypeCombo.setLayoutData(data);
		methodTypeCombo.add("Java");
		methodTypeCombo.add("Shell Script");
		methodTypeCombo.add("Python");
		methodTypeCombo.select(0);

		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Create Deleter!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnName = resNameText.getText().trim();
				returnMethod = resMethodText.getText().trim();
				if(methodTypeCombo.getText().equalsIgnoreCase("Java")) {
					returnMethodType = HandlerCallbackType.JAVA;
				} else if(methodTypeCombo.getText().equalsIgnoreCase("Shell Script")) {
					returnMethodType = HandlerCallbackType.SHELL;
				} else  if(methodTypeCombo.getText().equalsIgnoreCase("Python")) {
					returnMethodType = HandlerCallbackType.PYTHON;
				} else {
					returnMethodType = HandlerCallbackType.JAVA;
				}
				
				if (returnName.isEmpty()) {
					System.out.println("Name cannot be empty!");
					return;
				}
				if (returnMethod.isEmpty()) {
					System.out.println("Method cannot be empty!");
					return;
				}

				cancelled = false;
				dialog.close();
			}
		});
		
		dialog.setToolTipText(ToolTips.GET_DELETER_DATA_TOOLTIP);
		resNameLabel.setToolTipText(ToolTips.GET_DELETER_DATA_RESOURCE_NAME_TOOLTIP);
		resMethodLabel.setToolTipText(ToolTips.GET_DELETER_METHOD_TOOLTIP);
				
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	/**
	 * Get the necessary data to create a new poster
	 */
	public void getPosterData() {
		final Shell dialog = prepareDialog("Create new Poster");
		GridData data;
		
		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("Name:");
		data = new GridData();
		resNameLabel.setLayoutData (data);
		final Text resNameText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setLayoutData (data);

		Label resMethodLabel = new Label (dialog, SWT.NONE);
		resMethodLabel.setText ("Callback Method:");
		data = new GridData();
		resMethodLabel.setLayoutData (data);
		final Text resMethodText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resMethodText.setLayoutData (data);
		

		new Label (dialog, SWT.NONE);
		final Combo methodTypeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		methodTypeCombo.setLayoutData(data);
		methodTypeCombo.add("Java");
		methodTypeCombo.add("Shell Script");
		methodTypeCombo.add("Python");
		methodTypeCombo.select(0);

		Label resArgTypeLabel = new Label (dialog, SWT.NONE);
		resArgTypeLabel.setText ("Argument Type:");
		data = new GridData();
		resArgTypeLabel.setLayoutData (data);
		final Combo resArgCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		resArgCombo.add("None");
		resArgCombo.add("Integer");
		resArgCombo.add("Double");
		resArgCombo.add("String");
		resArgCombo.select(3);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resArgCombo.setLayoutData (data);		
		
		Label resShowTypeLabel = new Label (dialog, SWT.NONE);
		resShowTypeLabel.setText ("Presentation Type:");
		data = new GridData();
		resShowTypeLabel.setLayoutData (data);
		final Combo resShowCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		resShowCombo.add("None");
		resShowCombo.add("Binary");
		resShowCombo.add("Text");
		resShowCombo.select(2);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resShowCombo.setLayoutData (data);
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Create Poster!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnName = resNameText.getText().trim();
				returnMethod = resMethodText.getText().trim();
				if(methodTypeCombo.getText().equalsIgnoreCase("Java")) {
					returnMethodType = HandlerCallbackType.JAVA;
				} else if(methodTypeCombo.getText().equalsIgnoreCase("Shell Script")) {
					returnMethodType = HandlerCallbackType.SHELL;
				} else  if(methodTypeCombo.getText().equalsIgnoreCase("Python")) {
					returnMethodType = HandlerCallbackType.PYTHON;
				} else {
					returnMethodType = HandlerCallbackType.JAVA;
				}
				returnArgumentType = resArgCombo.getItem(resArgCombo.getSelectionIndex());
				returnShowType = resShowCombo.getItem(resShowCombo.getSelectionIndex());
				
				if (returnName.isEmpty()) {
					System.out.println("Name cannot be empty!");
					return;
				}
				if (returnMethod.isEmpty()) {
					System.out.println("Method cannot be empty!");
					return;
				}
				if (returnArgumentType.isEmpty()) {
					System.out.println("ArgType cannot be empty!");
					return;
				}

				// Trim returnMethod
				returnMethod = Constants.trimMethod(returnMethod);

				cancelled = false;
				dialog.close();
			}
		});

		dialog.setToolTipText(ToolTips.GET_POSTER_DATA_TOOLTIP);
		resNameLabel.setToolTipText(ToolTips.GET_POSTER_DATA_RESOURCE_NAME_TOOLTIP);
		resMethodLabel.setToolTipText(ToolTips.GET_POSTER_DATA_RESOURCE_METHOD_TOOLTIP);
		resArgTypeLabel.setToolTipText(ToolTips.GET_POSTER_DATA_RESOURCE_ARG_TYPE_TOOLTIP);
		resShowTypeLabel.setToolTipText(ToolTips.GET_POSTER_COLLECTION_SHOW_TYPE_TOOLTIP);
		
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	/**
	 * Get the necessary data to create a new putter
	 */
	public void getPutterData() {
		final Shell dialog = prepareDialog("Create new Putter");
		GridData data;
		
		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("Name:");
		data = new GridData();
		resNameLabel.setLayoutData (data);
		final Text resNameText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setLayoutData (data);

		Label resMethodLabel = new Label (dialog, SWT.NONE);
		resMethodLabel.setText ("Callback Method:");
		data = new GridData();
		resMethodLabel.setLayoutData (data);
		final Text resMethodText = new Text (dialog, SWT.BORDER);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resMethodText.setLayoutData (data);
		

		new Label (dialog, SWT.NONE);
		final Combo methodTypeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		methodTypeCombo.setLayoutData(data);
		methodTypeCombo.add("Java");
		methodTypeCombo.add("Shell Script");
		methodTypeCombo.add("Python");
		methodTypeCombo.select(0);

		Label resArgTypeLabel = new Label (dialog, SWT.NONE);
		resArgTypeLabel.setText ("Argument Type:");
		data = new GridData();
		resArgTypeLabel.setLayoutData (data);
		final Combo resArgCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		resArgCombo.add("None");
		resArgCombo.add("Integer");
		resArgCombo.add("Double");
		resArgCombo.add("String");
		resArgCombo.select(3);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resArgCombo.setLayoutData (data);		
		
		Label resShowTypeLabel = new Label (dialog, SWT.NONE);
		resShowTypeLabel.setText ("Presentation Type:");
		data = new GridData();
		resShowTypeLabel.setLayoutData (data);

		final Combo resShowCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		resShowCombo.add("None");
		resShowCombo.add("Binary");
		resShowCombo.add("Text");
		resShowCombo.select(2);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resShowCombo.setLayoutData (data);
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Create Putter");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnName = resNameText.getText().trim();
				returnMethod = resMethodText.getText().trim();
				if(methodTypeCombo.getText().equalsIgnoreCase("Java")) {
					returnMethodType = HandlerCallbackType.JAVA;
				} else if(methodTypeCombo.getText().equalsIgnoreCase("Shell Script")) {
					returnMethodType = HandlerCallbackType.SHELL;
				} else  if(methodTypeCombo.getText().equalsIgnoreCase("Python")) {
					returnMethodType = HandlerCallbackType.PYTHON;
				} else {
					returnMethodType = HandlerCallbackType.JAVA;
				}
				returnArgumentType = resArgCombo.getItem(resArgCombo.getSelectionIndex());
				returnShowType = resShowCombo.getItem(resShowCombo.getSelectionIndex());
				
				if (returnName.isEmpty()) {
					System.out.println("Name cannot be empty!");
					return;
				}
				if (returnMethod.isEmpty()) {
					System.out.println("Method cannot be empty!");
					return;
				}
				if (returnArgumentType.isEmpty()) {
					System.out.println("ArgType cannot be empty!");
					return;
				}

				// Trim returnMethod
				returnMethod = Constants.trimMethod(returnMethod);

				cancelled = false;
				dialog.close();
			}
		});

		dialog.setToolTipText(ToolTips.GET_PUTTER_DATA_TOOLTIP);
		resNameLabel.setToolTipText(ToolTips.GET_PUTTER_DATA_RESOURCE_NAME_TOOLTIP);
		resMethodLabel.setToolTipText(ToolTips.GET_PUTTER_DATA_RESOURCE_METHOD_TOOLTIP);
		resArgTypeLabel.setToolTipText(ToolTips.GET_PUTTER_DATA_RESOURCE_ARG_TYPE_TOOLTIP);
		resShowTypeLabel.setToolTipText(ToolTips.GET_PUTTER_COLLECTION_SHOW_TYPE_TOOLTIP);
		
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	public void showInstructions() {
		final Shell dialog = new Shell (parentShell);
		dialog.setText("Instructions");
		dialog.setSize(800, 500);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginBottom = 5;
		gridLayout.marginTop = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginRight = 5;
		dialog.setLayout(gridLayout);
		
		Rectangle bds = dialog.getDisplay().getBounds();
		Point p = dialog.getSize();
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;
		dialog.setBounds(nLeft, nTop, p.x, p.y);
		
		
		Label dummy = new Label (dialog, SWT.NONE);
		dummy.setText("User's Manual\n\n");
		GridData data = new GridData ();
		data.horizontalSpan = 4;
		data.heightHint = 20;
		data.horizontalAlignment = GridData.CENTER;
		dummy.setLayoutData (data);	
		
		Label resDescriptionLabel = new Label (dialog, SWT.NONE);
		resDescriptionLabel.setText (ToolTips.USER_MANUAL_TEXT);
		data = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 3;
		resDescriptionLabel.setLayoutData (data);

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("OK!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 30;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				cancelled = false;
				dialog.close();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	

	/**
	 * Get the necessary data to create the java classes
	 */
	public void getJavaCreationData() {
		final Shell dialog = prepareDialog("Create plain Java Server Code");
		GridData data;

		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("Java Project Name:");
		data = new GridData();
		resNameLabel.setLayoutData (data);
		final Text resNameText = new Text (dialog, SWT.BORDER);
		resNameText.setText(application.getCurrentProject().getProjectName());
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setLayoutData (data);
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.widthHint = 300;
		data.horizontalSpan = 1;
		data.heightHint = 20;
		dummy.setLayoutData(data);
		final Button resIsStandaloneButton = new Button (dialog, SWT.CHECK);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resIsStandaloneButton.setText("Create Standalone Application");
		resIsStandaloneButton.setSelection(application.getCurrentProject().getMakeStandalone());
		resIsStandaloneButton.setLayoutData(data);
		
		Label resJavaPackageLabel = new Label (dialog, SWT.NONE);
		resJavaPackageLabel.setText ("Java package for resources:");
		data = new GridData();
		data.widthHint = 300;
		data.horizontalSpan = 1;
		resJavaPackageLabel.setLayoutData (data);
		final Text resJavaPackageText = new Text (dialog, SWT.BORDER);
		resJavaPackageText.setText(application.getCurrentProject().getPackageCanonical());
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resJavaPackageText.setLayoutData (data);

		Label resHandlerPackageLabel = new Label (dialog, SWT.NONE);
		resHandlerPackageLabel.setText ("Canonical name of callback Handler:");
		data = new GridData();
		data.widthHint = 300;
		data.horizontalSpan = 1;
		resHandlerPackageLabel.setLayoutData (data);
		final Text resHandlerPackageText = new Text (dialog, SWT.BORDER);
		resHandlerPackageText.setText(application.getCurrentProject().getHandlerCanonical());
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resHandlerPackageText.setLayoutData (data);
		
		Label resLibLabel = new Label (dialog, SWT.NONE);
		resLibLabel.setText ("Preferred Library:");
		data = new GridData();
		data.widthHint = 300;
		data.horizontalSpan = 1;
		resLibLabel.setLayoutData (data);

		final Button jerseyButton = new Button(dialog, SWT.RADIO);
		jerseyButton.setText("Jersey");
		jerseyButton.setSelection(true);
		final Button restletButton = new Button(dialog, SWT.RADIO);
		restletButton.setText("Restlet");
		restletButton.setSelection(false);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		
		dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.widthHint = 300;
		data.horizontalSpan = 4;
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Create Java Code!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnName = resNameText.getText().trim();
				returnJavaPackage = resJavaPackageText.getText().trim();
				returnHandlerPackage = resHandlerPackageText.getText().trim();
				returnIsStandalone = resIsStandaloneButton.getSelection();
				if(jerseyButton.getSelection()) {
					returnBuildMode = BuildMode.JERSEY;
				} else if(restletButton.getSelection()) {
					returnBuildMode = BuildMode.RESTLET;
				} else {
					returnBuildMode = BuildMode.NONE;
				}
				
				if (returnName.isEmpty()) {
					System.out.println("Project Name cannot be empty!");
					return;
				}
				if (returnJavaPackage.isEmpty()) {
					System.out.println("Package cannot be empty!");
					return;
				}
				if (returnHandlerPackage.isEmpty()) {
					System.out.println("Handler cannot be empty!");
					return;
				}
				if (returnIsCollection == null) {
					System.out.println("Boolean cannot be null!");
					return;
				}

				cancelled = false;
				dialog.close();
			}
		});

		dialog.setToolTipText(ToolTips.GET_JAVA_CREATION_DATA_TOOLTIP);
		resNameLabel.setToolTipText(ToolTips.GET_JAVA_CREATION_DATA_PROTOTYPES_PACKAGE);
		resHandlerPackageLabel.setToolTipText(ToolTips.GET_JAVA_CREATION_DATA_MANAGEMENT_PACKAGE);
		
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	/**
	 * Get the necessary data to create the OSGi Bundle
	 */
	public void getOSGiCreationData() {
		final Shell dialog = prepareDialog("Create OSGi Server Code");
		GridData data;
				
		Label driverBundleNameLabel = new Label (dialog, SWT.NONE);
		driverBundleNameLabel.setText ("Driver Bundle Name:");
		data = new GridData();
		driverBundleNameLabel.setLayoutData (data);

		final Text driverBundleNameText = new Text (dialog, SWT.BORDER);
		driverBundleNameText.setText("PhidgetsRFID_DriverCore");
		data = new GridData ();
		data.widthHint = 500;
		data.horizontalSpan = 2;
		driverBundleNameText.setLayoutData (data);
		
		Label restletHandlerCanonicalLabel = new Label (dialog, SWT.NONE);
		restletHandlerCanonicalLabel.setText ("Handler Package + Class Name:");
		data = new GridData();
		restletHandlerCanonicalLabel.setLayoutData (data);

		final Text restletHandlerCanonicalText = new Text (dialog, SWT.BORDER);
		restletHandlerCanonicalText.setText("ch.ethz.inf.vs.wot.drivers.phidgets.rfid.core.RFIDHandler");
		data = new GridData ();
		data.widthHint = 500;
		data.horizontalSpan = 2;
		restletHandlerCanonicalText.setLayoutData (data);
		
		Label interfacesPackageLabel = new Label (dialog, SWT.NONE);
		interfacesPackageLabel.setText ("Interfaces Package + Class Name:");
		data = new GridData();
		interfacesPackageLabel.setLayoutData (data);

		final Text interfacesPackageText = new Text (dialog, SWT.BORDER);
		interfacesPackageText.setText("ch.ethz.inf.vs.wot.drivers.phidgets.rfid.interfaces.RestletAccessInterface");
		data = new GridData ();
		data.widthHint = 500;
		data.horizontalSpan = 2;
		interfacesPackageText.setLayoutData (data);

		Label implementationPackageLabel = new Label (dialog, SWT.NONE);
		implementationPackageLabel.setText ("Implementation Package + Class Name:");
		data = new GridData();
		implementationPackageLabel.setLayoutData (data);

		final Text implementationPackageText = new Text (dialog, SWT.BORDER);
		implementationPackageText.setText("ch.ethz.inf.vs.wot.drivers.phidgets.rfid.implementations.RestletAccessImplementation");
		data = new GridData ();
		data.widthHint = 500;
		data.horizontalSpan = 2;
		implementationPackageText.setLayoutData (data);

		Label restletPackageLabel = new Label (dialog, SWT.NONE);
		restletPackageLabel.setText ("Restlet Package Name:");
		data = new GridData();
		restletPackageLabel.setLayoutData (data);

		final Text restletPackageText = new Text (dialog, SWT.BORDER);
		restletPackageText.setText("ch.ethz.inf.vs.wot.drivers.mydriver.restlet");
		data = new GridData ();
		data.widthHint = 500;
		data.horizontalSpan = 2;
		restletPackageText.setLayoutData (data);
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Create OSGi Bundle!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 3;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnName = interfacesPackageText.getText().trim();
				returnMethod = implementationPackageText.getText().trim();
				returnDescription = restletPackageText.getText().trim();
				returnCollectionMethod = restletHandlerCanonicalText.getText().trim();
				returnShowType = driverBundleNameText.getText();
				
				if (returnName.isEmpty()) {
					System.out.println("Interface Package Name cannot be empty!");
					return;
				}
				if (returnMethod.isEmpty()) {
					System.out.println("Implementation Package Name cannot be empty!");
					return;
				}
				if (returnDescription.isEmpty()) {
					System.out.println("Restlet Package Name cannot be empty!");
					return;
				}
				if (returnCollectionMethod.isEmpty()) {
					System.out.println("Restlet Handler Class cannot be empty!");
					return;
				}
				if (returnShowType.isEmpty()) {
					System.out.println("Driver Bundle Name cannot be empty!");
					return;
				}

				cancelled = false;
				dialog.close();
			}
		});

		dialog.setToolTipText(ToolTips.GET_OSGI_CREATION_DATA_TOOLTIP);
		
		driverBundleNameLabel.setToolTipText(ToolTips.GET_OSGI_CREATION_DATA_DRIVERBUNDLENAME_TOOLTIP);
		restletHandlerCanonicalLabel.setToolTipText(ToolTips.GET_OSGI_CREATION_DATA_HANDLERNAME_TOOLTOP);
		interfacesPackageLabel.setToolTipText(ToolTips.GET_OSGI_CREATION_DATA_INTERFACES_TOOLTIP);
		implementationPackageLabel.setToolTipText(ToolTips.GET_OSGI_CREATION_DATA_IMPLEMENTATIONS_TOOLTIP);
		restletPackageLabel.setToolTipText(ToolTips.GET_OSGI_CREATION_DATA_RESTLETPACKAGENAME_TOOLTIP);
		
		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	/**
	 * Get the necessary data to change a description
	 */
	public void modifyDescriptionData(String currentDescription) {
		if (currentDescription == null) currentDescription = "";
		
		final Shell dialog = prepareDialog("Modify Description");
		GridData data;
		
		Label resDescriptionLabel = new Label (dialog, SWT.NONE);
		resDescriptionLabel.setText ("New Description:");
		data = new GridData();
		resDescriptionLabel.setLayoutData (data);

		final Text resDescriptionText = new Text (dialog, SWT.BORDER);
		resDescriptionText.setText(currentDescription);
		resDescriptionText.setSelection(0, currentDescription.length());
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resDescriptionText.setLayoutData (data);

		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Change Description!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnDescription = resDescriptionText.getText().trim();
				
				cancelled = false;
				dialog.close();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}	
	
	/**
	 * Get the necessary data to change a resource name
	 */
	public void modifyResourceNameData(String currentName) {
		final Shell dialog = prepareDialog("Modify Resource Name");
		GridData data;
		
		Label resNameLabel = new Label (dialog, SWT.NONE);
		resNameLabel.setText ("New Name:");
		data = new GridData();
		resNameLabel.setLayoutData (data);

		final Text resNameText = new Text (dialog, SWT.BORDER);
		resNameText.setText(currentName);
		resNameText.setSelection(0, currentName.length());
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resNameText.setLayoutData (data);

		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Change Name!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnName = resNameText.getText().trim();
				
				cancelled = false;
				dialog.close();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}	
	
	/**
	 * Get the necessary data to change a callback method
	 */
	public void modifyMethodData(String currentMethod, HandlerCallbackType currentMethodType) {
		final Shell dialog = prepareDialog("Modify Method");
		GridData data;
		
		Label resMethodLabel = new Label (dialog, SWT.NONE);
		resMethodLabel.setText ("New Method:");
		data = new GridData();
		resMethodLabel.setLayoutData (data);

		final Text resMethodText = new Text (dialog, SWT.BORDER);
		resMethodText.setText(currentMethod);
		resMethodText.setSelection(0, currentMethod.length());
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		resMethodText.setLayoutData (data);
		
		new Label (dialog, SWT.NONE);
		final Combo methodTypeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
		data = new GridData ();
		data.widthHint = 300;
		data.horizontalSpan = 3;
		methodTypeCombo.setLayoutData(data);
		methodTypeCombo.add("Java");
		methodTypeCombo.add("Shell Script");
		methodTypeCombo.add("Python");
		if(currentMethodType.name().equalsIgnoreCase("Java")) {
			methodTypeCombo.select(0);
		}
		if(currentMethodType.name().equalsIgnoreCase("Shell")) {
			methodTypeCombo.select(1);
		}
		if(currentMethodType.name().equalsIgnoreCase("Python")) {
			methodTypeCombo.select(2);
		}
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Change Method!");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				returnMethod = resMethodText.getText().trim();
				if(methodTypeCombo.getText().equalsIgnoreCase("Java")) {
					returnMethodType = HandlerCallbackType.JAVA;
				} else if(methodTypeCombo.getText().equalsIgnoreCase("Shell Script")) {
					returnMethodType = HandlerCallbackType.SHELL;
				} else  if(methodTypeCombo.getText().equalsIgnoreCase("Python")) {
					returnMethodType = HandlerCallbackType.PYTHON;
				} else {
					returnMethodType = HandlerCallbackType.JAVA;
				}
				cancelled = false;
				dialog.close();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	public void displayMessage(String title, String message) {
		final Shell dialog = prepareDialog(title);
		GridData data;
		
		Label messageLabel = new Label (dialog, SWT.NONE);
		messageLabel.setText (message);
		data = new GridData();
		data.horizontalSpan = 4;
		messageLabel.setLayoutData (data);
		
		Label dummy = new Label (dialog, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		dummy.setLayoutData(data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Ok");
		data = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		data.horizontalSpan = 4;
		data.heightHint = 50;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				dialog.close();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack();
		dialog.open();

		while (!dialog.isDisposed ()) {
			if (!parentShell.getDisplay().readAndDispatch ()) parentShell.getDisplay().sleep ();
		}
	}
	
	/**
	 * Setup the dialog box
	 * @param title - The title of the dialog box
	 * @return The dialog box
	 */
	private Shell prepareDialog(String title) {
		final Shell dialog = new Shell (parentShell);
		dialog.setText(title);
		dialog.setSize(700, 500);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.marginBottom = 5;
		gridLayout.marginTop = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginRight = 5;
		dialog.setLayout(gridLayout);
		
		Rectangle bds = dialog.getDisplay().getBounds();
		Point p = dialog.getSize();
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;
		dialog.setBounds(nLeft, nTop, p.x, p.y);
		
		
		Label dummy = new Label (dialog, SWT.NONE);
		dummy.setText("");
		GridData data = new GridData ();
		data.horizontalSpan = 4;
		data.heightHint = 30;
		data.horizontalAlignment = GridData.CENTER;
		dummy.setLayoutData (data);	
		
		return dialog;
	}
}