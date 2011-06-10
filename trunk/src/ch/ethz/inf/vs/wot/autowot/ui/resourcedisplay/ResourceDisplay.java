package ch.ethz.inf.vs.wot.autowot.ui.resourcedisplay;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.DeleterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.GetterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PosterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PutterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.security.UserLoginData;
import ch.ethz.inf.vs.wot.autowot.ui.ViewStyle;
import ch.ethz.inf.vs.wot.autowot.ui.views.UserInterface;

/**
 * Class providing the resource display
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class ResourceDisplay extends Table {
	
	protected ViewStyle viewStyle = ViewStyle.PRETTY;
	protected AutoWoT application = null;
	protected UserInterface mainUI = null;
	protected ResourceDisplayPaintListener paintListener = null;
	protected ResourceDisplayMouseListener mouseListener = null;
	
	protected int currentIndentationLevel = 0;
	
	private Color bgColor = null;
	private Color childColor = new Color(Display.getCurrent(), 0, 255, 255);
	private Color getterColor = new Color(Display.getCurrent(), 0, 255, 127);
	private Color posterColor = new Color(Display.getCurrent(), 0, 191, 255);
	private Color putterColor = new Color(Display.getCurrent(), 191, 255, 0);
	private Color deleterColor = new Color(Display.getCurrent(), 255, 191, 0);
	private Color whiteColor = new Color(Display.getCurrent(), 255, 255, 255);
	
	public ResourceDisplay(AutoWoT application, UserInterface mainUI, Composite arg0, int arg1) {
		super(arg0, arg1);
		this.application = application;
		this.mainUI = mainUI;
		this.viewStyle = ViewStyle.PRETTY;
		addSelectionListener(new ResourceDisplaySelectionListener(application, mainUI));
		TableItem item = new TableItem(this, SWT.NONE);
		item.setText("Welcome to the AutoWoT Prototyper for automatic WoTizing of Resources!");
	}
	
	public void setViewStyle(ViewStyle viewStyle) {
		this.viewStyle = viewStyle;
	}
	
	public ViewStyle getViewStyle() {
		return viewStyle;
	}
	
	public void printCurrentResourceInfo() {
		if (paintListener != null) {
			removePaintListener(paintListener);
			paintListener = null;
		
		}
		if (mouseListener != null) {
			removeMouseListener(mouseListener);
			mouseListener = null;
		}
		if(application.getCurrentProject().getRootResource() == null) {
			removeAll();
			TableItem item = new TableItem(this, SWT.NONE);
			item.setText("Welcome to the AutoWoT Prototyper for automatic WoTizing of Resources!");
			return;
		}
		if (viewStyle == ViewStyle.PRETTY) {
			printCurrentResourceInfoPretty();
		} else if (viewStyle == ViewStyle.XML) {
			printCurrentResourceInfoXML();
		} else if (viewStyle == ViewStyle.GRAPHICAL) {
			printCurrentResourceInfoGraphical();
		}
		
		bgColor = whiteColor;
		addBlankLine();
	}
	
	private void printCurrentResourceInfoGraphical() {
		if (application.getCurrentResource() != null) {
			currentIndentationLevel = 0;
			removeAll();
			mouseListener = new ResourceDisplayMouseListener(application, mainUI);
			paintListener = new ResourceDisplayPaintListener(application, mainUI, mouseListener);
			addMouseListener(mouseListener);
			addPaintListener(paintListener);
			addBlankLine();
		}
	}
	
	/**
	 * Update information in dropArea with the information about the currently
	 * selected resource
	 */
	protected void printCurrentResourceInfoPretty() {
		if (application.getCurrentResource() != null) {
			currentIndentationLevel = 0;
			removeAll();
			bgColor = whiteColor;
			
			if (application.getCurrentResource().getParentResource() != null) {
				addTableText("Go to parent...");
				addBlankLine();
			}
			
			if (application.getCurrentResource().getIsResource()) {
				printPrettyAsResource();
			} else if (application.getCurrentResource().getIsGetter()) {
				printPrettyAsGetter();
			} else if (application.getCurrentResource().getIsPoster()) {
				printPrettyAsPoster();
			} else if (application.getCurrentResource().getIsPutter()) {
				printPrettyAsPutter();
			} else if (application.getCurrentResource().getIsDeleter()) {
				printPrettyAsDeleter();
			}
		}
	}
	
	protected void printPrettyAsResource() {
		// Cast Resource to ResourceItem
		ResourceItem currentRes = application.getCurrentResource().asResourceItem();
		
		// Information about the resource
		addTableText("Resource " + currentRes.getClassName());
		indent();
		addBlankLine();
		addTableText("Name: " + currentRes.getResourceName());
		addTableText("URI: " + currentRes.getURI());
		addTableText("Allowed Methods: " + currentRes.getMethods());
		
		List<String> restrictedMethods = new ArrayList<String>();
		List<String> publicMethods = new ArrayList<String>();
		if(currentRes.hasGetter()) {
			if(currentRes.getGetter().isRestricted()) {
				restrictedMethods.add("GET");
			} else {
				publicMethods.add("GET");
			}
		}
		if(currentRes.hasPoster()) {
			if(currentRes.getPoster().isRestricted()) {
				restrictedMethods.add("POST");
			} else {
				publicMethods.add("POST");
			}
		}
		if(currentRes.hasPutter()) {
			if(currentRes.getPutter().isRestricted()) {
				restrictedMethods.add("PUT");
			} else {
				publicMethods.add("PUT");
			}
		}
		if(currentRes.hasDeleter()) {
			if(currentRes.getDeleter().isRestricted()) {
				restrictedMethods.add("DELETE");
			} else {
				publicMethods.add("DELETE");
			}
		}
		if(!restrictedMethods.isEmpty()) {
			addTableText("Methods requiring authentication: " + restrictedMethods.toString().substring(1, restrictedMethods.toString().length()-1));
		}
		if(!publicMethods.isEmpty()) {
			addTableText("Methods without authentication: " + publicMethods.toString().substring(1, publicMethods.toString().length()-1));
		}
		
		// In case it is a collection
		if (currentRes.isCollection()) {
			addTableText("This is a collection Resource. Its collection method is \"" + currentRes.getCollectionMethod() + "(...)\"");
		}
		
		// In case a description has been defined
		if (currentRes.getDescription() == null) {
			addTableText("No description given...");
		} else {
			addTableText("Description: " + currentRes.getDescription());
		}
		
		bgColor = whiteColor;
		addBlankLine();
		
		// Display children
		if (currentRes.hasChildren()) {
			addTableText("Children");
			bgColor = childColor;
			indent();
			for (String childName : currentRes.getChildNames()) {
				String firstLine = childName;
				if (currentRes.getChild(childName).getDescription() != null) {
					firstLine += ": " + currentRes.getChild(childName).getDescription();
				}
				addTableText(firstLine);
				indent();
				if (currentRes.getChild(childName).isCollection()) {
					addTableText("This is a collection of dynamic resources.");
				}
				outdent();
			}
			outdent();
		} else {
			addTableText("Drag Child resources here...");
		}
		
		bgColor = whiteColor;
		addBlankLine();
		
		if (currentRes.hasGetter()) {
			bgColor = getterColor;
			String name = currentRes.getGetter().getResourceName();
			
			String info = "Getter \"" + name + "\"";
			if (currentRes.getGetter().getDescription() != null)
				info += ": " + currentRes.getGetter().getDescription();
			
			addTableText(info);
		} else {
			addTableText("Drag Getter resources here...");
		}
		bgColor = whiteColor;
		
		addBlankLine();
		if (currentRes.hasPoster()) {
			bgColor = posterColor;
			String name = currentRes.getPoster().getResourceName();
			
			String info = "Poster \"" + name + "\"";
			if (currentRes.getPoster().getDescription() != null)
				info += ": " + currentRes.getPoster().getDescription();
			
			addTableText(info);
		} else {
			addTableText("Drag Poster resources here...");
		}
		bgColor = whiteColor;
		
		addBlankLine();
		if (currentRes.hasPutter()) {
			bgColor = putterColor;
			String name = currentRes.getPutter().getResourceName();
			
			String info = "Putter \"" + name + "\"";
			if (currentRes.getPutter().getDescription() != null)
				info += ": " + currentRes.getPutter().getDescription();
			
			addTableText(info);
		} else {
			addTableText("Drag Putter resources here...");
		}
		bgColor = whiteColor;
		
		addBlankLine();
		if (currentRes.hasDeleter()) {
			bgColor = deleterColor;
			String name = currentRes.getDeleter().getResourceName();
			
			String info = "Deleter \"" + name + "\"";
			if (currentRes.getDeleter().getDescription() != null)
				info += ": " + currentRes.getDeleter().getDescription();
			
			addTableText(info);
		} else {
			addTableText("Drag Deleter resources here...");
		}
		bgColor = whiteColor;
		
		addBlankLine();
		printPrettySecurity(currentRes);
		
		outdent();
	}
	
	protected void printPrettyAsGetter() {
		GetterItem currentRes = application.getCurrentResource().asGetterItem();
		bgColor = getterColor;
		addTableText("Getter \"" + currentRes.getResourceName() + "\"");
		
		addBlankLine();
		indent();
		
		addTableText("Handler Method: " + currentRes.getCallbackMethod() + "(...)");
		addTableText("Handler Method Type: " + currentRes.getCallbackMethodType().toString());
		
		if (currentRes.getOnChangeMethod() != null) {
			addTableText("OnChange Method: " + currentRes.getOnChangeMethod() + "(...)");
			addTableText("OnChange Method Type: " + currentRes.getOnChangeMethodType().toString());
		}
		
		if (currentRes.getDescription() != null) {
			addBlankLine();
			addTableText("Description: " + currentRes.getDescription());
		}
		
		addBlankLine();
		printPrettySecurity(currentRes);
		
		outdent();
	}
	
	protected void printPrettyAsPoster() {
		PosterItem currentRes = application.getCurrentResource().asPosterItem();
		bgColor = posterColor;
		addTableText("Poster \"" + currentRes.getResourceName() + "\"");
		
		addBlankLine();
		indent();
		addTableText("Handler Method: " + currentRes.getCallbackMethod() + "(...)");
		addTableText("Handler Method Type: " + currentRes.getCallbackMethodType().toString());
		addTableText("Argument Type: " + currentRes.getPosterArgumentType());
		addTableText("Presentation Type: " + currentRes.getPosterPresentationType());
		
		if (currentRes.getDescription() != null) {
			addBlankLine();
			addTableText("Description: " + currentRes.getDescription());
		}
		
		addBlankLine();
		printPrettySecurity(currentRes);
		
		outdent();
	}
	
	protected void printPrettyAsPutter() {
		PutterItem currentRes = application.getCurrentResource().asPutterItem();
		bgColor = putterColor;
		addTableText("Putter \"" + currentRes.getResourceName() + "\"");
		
		addBlankLine();
		indent();
		addTableText("Handler Method: " + currentRes.getCallbackMethod() + "(...)");
		addTableText("Handler Method Type: " + currentRes.getCallbackMethodType().toString());
		addTableText("Argument Type: " + currentRes.getPutterArgumentType());
		addTableText("Presentation Type: " + currentRes.getPutterPresentationType());
		
		if (currentRes.getDescription() != null) {
			addBlankLine();
			addTableText("Description: " + currentRes.getDescription());
		}
		
		addBlankLine();
		printPrettySecurity(currentRes);
		
		outdent();
	}
	
	protected void printPrettyAsDeleter() {
		DeleterItem currentRes = application.getCurrentResource().asDeleterItem();
		bgColor = deleterColor;
		addTableText("Deleter \"" + currentRes.getResourceName() + "\"");
		
		addBlankLine();
		indent();
		addTableText("Handler Method: " + currentRes.getCallbackMethod() + "(...)");
		addTableText("Handler Method Type: " + currentRes.getCallbackMethodType().toString());
		
		if (currentRes.getDescription() != null) {
			addBlankLine();
			addTableText("Description: " + currentRes.getDescription());
		}
		
		addBlankLine();
		printPrettySecurity(currentRes);
		
		outdent();
	}
	
	protected void printPrettySecurity(AbstractResourceItem currentRes) {
		addTableText("Drag Authorized Users here...");
		indent();
		if(currentRes.getInheritUsers()) {
			addTableText("Inherited users: ");
			indent();
			if(currentRes.getInheritedUsers().isEmpty()) {
				addTableText("<none>");
			} else {
				for(UserLoginData user: currentRes.getInheritedUsers()) {
					addTableText("User: \"" + user.getUsername() + "\" as defined in \"" + user.getResource().getResourceName() + "\"");
				}
			}
			outdent();
		} else {
			addTableText("This resource does not inherit users from parent");
		}
		
		addTableText("Users definded by this resource");
		indent();
		if(currentRes.getAuthorizedUsers().isEmpty()) {
			addTableText("<none>");
		} else {
			for(UserLoginData user: currentRes.getAuthorizedUsers()) {
				addTableText("User: \"" + user.getUsername() + "\" Password: \"*******\"");
			}
		}
		outdent();
	}
	
	private void printCurrentResourceInfoXML() {
		if (application.getCurrentResource() != null) {
			currentIndentationLevel = 0;
			removeAll();
			bgColor = whiteColor;
			
			if (application.getCurrentResource().getParentResource() != null) {
				addTableText("^");
				addBlankLine();
			}
			
			if (application.getCurrentResource().getIsResource()) {
				printXMLAsResource();
			} else if (application.getCurrentResource().getIsGetter()) {
				printXMLAsGetter();
			} else if (application.getCurrentResource().getIsPoster()) {
				printXMLAsPoster();
			} else if (application.getCurrentResource().getIsPutter()) {
				printXMLAsPutter();
			} else if (application.getCurrentResource().getIsDeleter()) {
				printXMLAsDeleter();
			}
		}
	}
	
	protected void printXMLAsResource() {
		ResourceItem currentRes = application.getCurrentResource().asResourceItem();
		
		addTableText("<" + currentRes.getClassName() + ">");
		indent();
		addTableText(Constants.RESOURCE_NAME_OPEN + currentRes.getResourceName() + Constants.RESOURCE_NAME_CLOSE);
		addTableText(Constants.RESOURCE_URI_OPEN + currentRes.getURI() + Constants.RESOURCE_URI_CLOSE);
		addTableText(Constants.RESOURCE_METHODS_OPEN + currentRes.getMethods() + Constants.RESOURCE_METHODS_CLOSE);
		
		List<String> restrictedMethods = new ArrayList<String>();
		List<String> publicMethods = new ArrayList<String>();
		if(currentRes.hasGetter()) {
			if(currentRes.getGetter().isRestricted()) {
				restrictedMethods.add("GET");
			} else {
				publicMethods.add("GET");
			}
		}
		if(currentRes.hasPoster()) {
			if(currentRes.getPoster().isRestricted()) {
				restrictedMethods.add("POST");
			} else {
				publicMethods.add("POST");
			}
		}
		if(currentRes.hasPutter()) {
			if(currentRes.getPutter().isRestricted()) {
				restrictedMethods.add("PUT");
			} else {
				publicMethods.add("PUT");
			}
		}
		if(currentRes.hasDeleter()) {
			if(currentRes.getDeleter().isRestricted()) {
				restrictedMethods.add("DELETE");
			} else {
				publicMethods.add("DELETE");
			}
		}
		if(!restrictedMethods.isEmpty()) {
			addTableText(Constants.RESOURCE_RESTRICTED_METHODS_OPEN + restrictedMethods.toString().substring(1, restrictedMethods.toString().length()-1) + Constants.RESOURCE_RESTRICTED_METHODS_CLOSE);
		}
		if(!publicMethods.isEmpty()) {
			addTableText(Constants.RESOURCE_PUBLIC_METHODS_OPEN + publicMethods.toString().substring(1, publicMethods.toString().length()-1) + Constants.RESOURCE_PUBLIC_METHODS_CLOSE);
		}
		
		
		if (!currentRes.isCollection()) {
			addTableText(Constants.RESOURCE_COLLECTION_METHOD_NONE);
		} else {
			addTableText(Constants.RESOURCE_COLLECTION_METHOD_OPEN + currentRes.getCollectionMethod() + Constants.RESOURCE_COLLECTION_METHOD_CLOSE);
		}
		
		if (currentRes.getDescription() == null) {
			addTableText(Constants.RESOURCE_DESCRIPTION_NONE);
		} else {
			addTableText(Constants.RESOURCE_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.RESOURCE_DESCRIPTION_CLOSE);
		}
		
		if (currentRes.getChildren().isEmpty()) {
			addTableText(Constants.RESOURCE_CHILDREN_NONE);
		} else {
			addTableText(Constants.RESOURCE_CHILDREN_OPEN);
			indent();
			
			bgColor = childColor;
			for (String childName : currentRes.getChildNames()) {
				if (currentRes.getChild(childName).isCollection())
					addTableText(Constants.RESOURCE_CHILD_OPEN.substring(0, Constants.RESOURCE_CHILD_OPEN.length() - 1) + " type=\"collection\">");
				else
					addTableText(Constants.RESOURCE_CHILD_OPEN);
				indent();
				addTableText("<" + childName + "/>");
				outdent();
				addTableText(Constants.RESOURCE_CHILD_CLOSE);
			}
			
			bgColor = whiteColor;
			
			outdent();
			addTableText(Constants.RESOURCE_CHILDREN_CLOSE);
		}
		
		if (!currentRes.hasGetter()) {
			addTableText(Constants.RESOURCE_GETTER_NONE);
		} else {
			bgColor = getterColor;
			addTableText(Constants.RESOURCE_GETTER_OPEN);
			indent();
			addTableText("<" + currentRes.getGetter().getResourceName() + "/>");
			outdent();
			addTableText(Constants.RESOURCE_GETTER_CLOSE);
			bgColor = whiteColor;
		}
		
		if (!currentRes.hasPoster()) {
			addTableText(Constants.RESOURCE_POSTER_NONE);
		} else {
			bgColor = posterColor;
			addTableText(Constants.RESOURCE_POSTER_OPEN);
			indent();
			addTableText("<" + currentRes.getPoster().getResourceName() + "/>");
			outdent();
			addTableText(Constants.RESOURCE_POSTER_CLOSE);
			bgColor = whiteColor;
		}
		
		if (!currentRes.hasPutter()) {
			addTableText(Constants.RESOURCE_PUTTER_NONE);
		} else {
			bgColor = putterColor;
			addTableText(Constants.RESOURCE_PUTTER_OPEN);
			indent();
			addTableText("<" + currentRes.getPutter().getResourceName() + "/>");
			outdent();
			addTableText(Constants.RESOURCE_PUTTER_CLOSE);
			bgColor = whiteColor;
		}
		
		if (!currentRes.hasDeleter()) {
			addTableText(Constants.RESOURCE_DELETER_NONE);
		} else {
			bgColor = deleterColor;
			addTableText(Constants.RESOURCE_DELETER_OPEN);
			indent();
			addTableText("<" + currentRes.getDeleter().getResourceName() + "/>");
			outdent();
			addTableText(Constants.RESOURCE_DELETER_CLOSE);
			bgColor = whiteColor;
		}
		
		printXMLSecurity(currentRes);
		
		outdent();
		addTableText("</" + currentRes.getClassName() + ">");
	}
	
	protected void printXMLAsGetter() {
		GetterItem currentRes = application.getCurrentResource().asGetterItem();
		
		bgColor = getterColor;
		indent();
		
		addTableText(Constants.GETTER_NAME_OPEN + currentRes.getResourceName() + Constants.GETTER_NAME_CLOSE);
		addTableText(Constants.GETTER_METHOD_OPEN + currentRes.getCallbackMethod() + Constants.GETTER_METHOD_CLOSE);
		if (currentRes.getOnChangeMethod() == null) {
			addTableText(Constants.GETTER_ONCHANGE_METHOD_NONE);
		} else {
			addTableText(Constants.GETTER_ONCHANGE_METHOD_OPEN + currentRes.getOnChangeMethod() + Constants.GETTER_ONCHANGE_METHOD_CLOSE);
		}
		
		if (currentRes.getDescription() == null) {
			addTableText(Constants.GETTER_DESCRIPTION_NONE);
		} else {
			addTableText(Constants.GETTER_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.GETTER_DESCRIPTION_CLOSE);
		}

		printXMLSecurity(currentRes);
		
		bgColor = whiteColor;
		outdent();
	}

	protected void printXMLAsPoster() {
		PosterItem currentRes = application.getCurrentResource().asPosterItem();
		
		bgColor = posterColor;
		indent();
		
		addTableText(Constants.POSTER_NAME_OPEN + currentRes.getResourceName() + Constants.POSTER_NAME_CLOSE);
		addTableText(Constants.POSTER_METHOD_OPEN + currentRes.getCallbackMethod() + Constants.POSTER_METHOD_CLOSE);
		addTableText(Constants.POSTER_ARGTYPE_OPEN + currentRes.getPosterArgumentType() + Constants.POSTER_ARGTYPE_CLOSE);
		addTableText(Constants.POSTER_SHOWTYPE_OPEN + currentRes.getPosterPresentationType() + Constants.POSTER_SHOWTYPE_CLOSE);
		
		if (currentRes.getDescription() == null) {
			addTableText(Constants.POSTER_DESCRIPTION_NONE);
		} else {
			addTableText(Constants.POSTER_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.POSTER_DESCRIPTION_CLOSE);
		}
		
		printXMLSecurity(currentRes);
		
		bgColor = whiteColor;
		outdent();
	}

	protected void printXMLAsPutter() {
		PutterItem currentRes = application.getCurrentResource().asPutterItem();
		
		bgColor = putterColor;
		indent();
		
		addTableText(Constants.PUTTER_NAME_OPEN + currentRes.getResourceName() + Constants.PUTTER_NAME_CLOSE);
		addTableText(Constants.PUTTER_METHOD_OPEN + currentRes.getCallbackMethod() + Constants.PUTTER_METHOD_CLOSE);
		addTableText(Constants.PUTTER_ARGTYPE_OPEN + currentRes.getPutterArgumentType() + Constants.PUTTER_ARGTYPE_CLOSE);
		addTableText(Constants.PUTTER_SHOWTYPE_OPEN + currentRes.getPutterPresentationType() + Constants.PUTTER_SHOWTYPE_CLOSE);
		
		if (currentRes.getDescription() == null) {
			addTableText(Constants.PUTTER_DESCRIPTION_NONE);
		} else {
			addTableText(Constants.PUTTER_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.PUTTER_DESCRIPTION_CLOSE);
		}
		
		printXMLSecurity(currentRes);
		
		bgColor = whiteColor;
		outdent();
	}

	protected void printXMLAsDeleter() {
		DeleterItem currentRes = application.getCurrentResource().asDeleterItem();
		
		bgColor = deleterColor;
		indent();
		
		addTableText(Constants.DELETER_NAME_OPEN + currentRes.getResourceName() + Constants.DELETER_NAME_CLOSE);
		addTableText(Constants.DELETER_METHOD_OPEN + currentRes.getCallbackMethod() + Constants.DELETER_METHOD_CLOSE);
		
		if (currentRes.getDescription() == null) {
			addTableText(Constants.DELETER_DESCRIPTION_NONE);
		} else {
			addTableText(Constants.DELETER_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.DELETER_DESCRIPTION_CLOSE);
		}
		
		printXMLSecurity(currentRes);
		
		bgColor = whiteColor;
		outdent();
	}
	
	protected void printXMLSecurity(AbstractResourceItem currentRes) {
		addTableText(Constants.SECURITY_OPEN);
		indent();
		addTableText(Constants.SECURITY_INHERIT_OPEN + currentRes.getInheritUsers() + Constants.SECURITY_INHERIT_OPEN);
		
		if (currentRes.getInheritUsers() && !currentRes.getInheritedUsers().isEmpty()) {
			addTableText(Constants.SECURITY_INHERIT_USER_LIST_OPEN);
			indent();
			for (UserLoginData user : currentRes.getInheritedUsers()) {
				addTableText(Constants.SECURITY_USER_OPEN);
				indent();
				addTableText(Constants.SECURITY_USER_NAME_OPEN + user.getUsername() + Constants.SECURITY_USER_NAME_CLOSE);
				addTableText(Constants.SECURITY_USER_RESOURCE_OPEN + user.getResource().getResourceName() + Constants.SECURITY_USER_RESOURCE_CLOSE);
				outdent();
				addTableText(Constants.SECURITY_USER_CLOSE);
			}
			outdent();
			addTableText(Constants.SECURITY_INHERIT_USER_LIST_CLOSE);
		} else {
			addTableText(Constants.SECURITY_INHERIT_USER_LIST_CLOSE);
		}
		
		if (currentRes.isRestricted() && !currentRes.getAuthorizedUsers().isEmpty()) {
			addTableText(Constants.SECURITY_USER_LIST_OPEN);
			indent();
			for (UserLoginData user : currentRes.getAuthorizedUsers()) {
				addTableText(Constants.SECURITY_USER_OPEN + Constants.SECURITY_USER_NAME_OPEN + user.getUsername() + Constants.SECURITY_USER_NAME_CLOSE + Constants.SECURITY_USER_CLOSE);
			}
			outdent();
			addTableText(Constants.SECURITY_USER_LIST_CLOSE);
		} else {
			addTableText(Constants.SECURITY_USER_LIST_CLOSE);
		}
		outdent();
		addTableText(Constants.SECURITY_CLOSE);
	}
	
	/**
	 * Add a text as item to the table dropArea
	 * 
	 * @param text
	 */
	private void addTableText(String text) {
		for (int i = 0; i < currentIndentationLevel; i++)
			text = "   " + text;
		
		TableItem item = new TableItem(this, SWT.NONE);
		item.setText(text);
		item.setBackground(bgColor);
	}
	
	private void addBlankLine() {
		addTableText("");
	}
	
	private void indent() {
		currentIndentationLevel++;
	}
	
	private void outdent() {
		currentIndentationLevel--;
	}
	
	@Override
	protected void checkSubclass() {
		// Allow SWT subclassing
	}
}
