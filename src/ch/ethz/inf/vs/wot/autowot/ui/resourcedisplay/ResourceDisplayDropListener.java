package ch.ethz.inf.vs.wot.autowot.ui.resourcedisplay;

/**
 * Listener reacting to dropping an action string on the display area
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.TableItem;

import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.core.FileHandler;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.DeleterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.GetterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PosterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PutterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.security.UserLoginData;
import ch.ethz.inf.vs.wot.autowot.ui.ViewStyle;
import ch.ethz.inf.vs.wot.autowot.ui.dialogs.DialogBoxFactory;
import ch.ethz.inf.vs.wot.autowot.ui.views.UserInterface;

public class ResourceDisplayDropListener implements DropTargetListener {
	
	protected AutoWoT application = null;
	protected UserInterface mainUI = null;
	
	protected TextTransfer textTransfer = TextTransfer.getInstance();
	protected FileTransfer fileTransfer = FileTransfer.getInstance();
	
	protected List<String> bannedNames = null;
	
	public ResourceDisplayDropListener(AutoWoT application, UserInterface mainUI) {
		this.application = application;
		this.mainUI = mainUI;
		bannedNames = new ArrayList<String>();
	}
	
	public void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
		// will accept text but prefer to have files dropped
		for (int i = 0; i < event.dataTypes.length; i++) {
			if (fileTransfer.isSupportedType(event.dataTypes[i])){
				event.currentDataType = event.dataTypes[i];
				// files should only be copied
				if (event.detail != DND.DROP_COPY) {
					event.detail = DND.DROP_NONE;
				}
				break;
			}
		}
	}
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
		if (textTransfer.isSupportedType(event.currentDataType)) {
			// NOTE: on unsupported platforms this will return null
			Object o = textTransfer.nativeToJava(event.currentDataType);
			String t = (String)o;
			if (t != null) System.out.println(t);
		}
	}
	public void dragOperationChanged(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}

		// allow text to be moved but files should only be copied
		if (fileTransfer.isSupportedType(event.currentDataType)){
			if (event.detail != DND.DROP_COPY) {
				event.detail = DND.DROP_NONE;
			}
		}
	}

	public void dragLeave(DropTargetEvent event) {
	}

	public void dropAccept(DropTargetEvent event) {
	}

	public void drop(DropTargetEvent event) {
		if (textTransfer.isSupportedType(event.currentDataType)) {
			String text = (String) event.data;
			
			// ******************************************************
			// Handle adding a root resource
			// ******************************************************
			if (text.equalsIgnoreCase(Constants.RESOURCE_STRING)) {
				DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
				db.getChildData();

				if (!db.cancelled) {
					String className = Constants.makeClassName(db.returnName);
					bannedNames.clear();
					bannedNames.add(className);
					
					application.getCurrentProject().setRootResource(new ResourceItem(className, db.returnName, db.returnURI, db.returnMethod, db.returnIsCollection, db.returnCollectionMethod, db.returnCollectionMethodType, null));
					application.setCurrentResource(application.getCurrentProject().getRootResource());
				}
			}
			
			// ******************************************************
			// Handle manipulating the current resource
			// ******************************************************
			else {
				if (application.getCurrentResource() != null) {
					// ******************************************************
					// Handle adding a child
					// ******************************************************
					if (text.equalsIgnoreCase(Constants.CHILD_STRING)) {
						DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
						db.getChildData();
						ResourceItem currentRes = application.getCurrentResource().asResourceItem();

						if (!db.cancelled) {
							String className = Constants.makeClassName(db.returnName);
							if (!bannedNames.contains(className)) {
								bannedNames.add(className);

								// Build the URI
								String rootRelativeURI;
								if(db.returnIsCollection) {
									java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("/(.*?)/\\{(.*?)\\}");
									java.util.regex.Matcher matcher = pattern.matcher(db.returnURI);
									matcher.find();
									
									rootRelativeURI = (currentRes.getURI() + "/" + matcher.group(1)).replace("//", "/");
									ResourceItem intermediateRes = new ResourceItem(className, db.returnName, rootRelativeURI, db.returnMethod, false, "", HandlerCallbackType.NONE, currentRes);
									
									rootRelativeURI = (currentRes.getURI() + "/" + matcher.group(1) + "/{" + matcher.group(2) + "}").replace("//", "/");
									intermediateRes.addChild(new ResourceItem(className + "Variable", db.returnName + "Variable", rootRelativeURI, db.returnMethod, db.returnIsCollection, db.returnCollectionMethod, db.returnCollectionMethodType, currentRes));
									
									currentRes.addChild(intermediateRes);
									
								} else if(db.returnIsDynamicChild) {
									if(!currentRes.hasDynamicChild()) {
										rootRelativeURI = (currentRes.getURI() + db.returnURI).replace("//", "/");
										currentRes.addChild(new ResourceItem(className, db.returnName, rootRelativeURI, db.returnMethod, db.returnIsDynamicChild, db.returnCollectionMethod, db.returnCollectionMethodType, currentRes));
									}
								} else {
									rootRelativeURI = (currentRes.getURI() + db.returnURI).replace("//", "/");
									currentRes.addChild(new ResourceItem(className, db.returnName, rootRelativeURI, db.returnMethod, db.returnIsCollection || db.returnIsDynamicChild, db.returnCollectionMethod, db.returnCollectionMethodType, currentRes));
								}
								
							}
						}
					}
					
					// ******************************************************
					// Handle adding a Getter
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.GETTER_STRING)) {
						if (!application.getCurrentResource().asResourceItem().hasGetter()) {
							DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
							db.getGetterData();
							ResourceItem currentRes = application.getCurrentResource().asResourceItem();

							if (!db.cancelled) {
								String className = Constants.makeClassName(db.returnName);
								if (!bannedNames.contains(className)) {
									bannedNames.add(className);

									if (db.returnCollectionMethod == null || db.returnCollectionMethod.isEmpty()) {
										currentRes.setGetter(new GetterItem(db.returnName, db.returnMethod, db.returnMethodType, currentRes));
									}
									else {
										currentRes.setGetter(new GetterItem(db.returnName, db.returnMethod, db.returnMethodType, db.returnOnChangeMethod, db.returnOnChangeMethodType, currentRes));
									}
								}
							}
						}
					}
					
					// ******************************************************
					// Handle adding a Poster
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.POSTER_STRING)) {
						if (!application.getCurrentResource().asResourceItem().hasPoster()) {
							DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
							db.getPosterData();
							ResourceItem currentRes = application.getCurrentResource().asResourceItem();

							if (!db.cancelled){
								String className = Constants.makeClassName(db.returnName);
								if (!bannedNames.contains(className)) {
									bannedNames.add(className);

									currentRes.setPoster(new PosterItem(db.returnName, db.returnMethod, db.returnMethodType, db.returnArgumentType, db.returnShowType, currentRes));
								}
							}
						}
						else {
							// Only one Poster per Resource!
						}
					}
					
					// ******************************************************
					// Handle adding a Putter
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.PUTTER_STRING)) {
						if (!application.getCurrentResource().asResourceItem().hasPutter()) {
							DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
							db.getPutterData();
							ResourceItem currentRes = application.getCurrentResource().asResourceItem();

							if (!db.cancelled){
								String className = Constants.makeClassName(db.returnName);
								if (!bannedNames.contains(className)) {
									bannedNames.add(className);

									currentRes.setPutter(new PutterItem(db.returnName, db.returnMethod, db.returnMethodType, db.returnArgumentType, db.returnShowType, currentRes));
								}
							}
						}
						else {
							// Only one Putter per Resource!
						}
					}
					
					// ******************************************************
					// Handle adding a Deleter
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.DELETER_STRING)) {
						if (!application.getCurrentResource().asResourceItem().hasDeleter()) {
							DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
							db.getDeleterData();
							ResourceItem currentRes = application.getCurrentResource().asResourceItem();

							if (!db.cancelled) {
								String className = Constants.makeClassName(db.returnName);
								if (!bannedNames.contains(className)) {
									bannedNames.add(className);

									if (db.returnCollectionMethod == null || db.returnCollectionMethod.isEmpty()) {
										currentRes.setDeleter(new DeleterItem(db.returnName, db.returnMethod, db.returnMethodType, currentRes));
									}
								}
							}
						}
						else {
							// Only one Deleter per Resource!
						}
					}
					
					// ******************************************************
					// Handle editing the description
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.DESCRIPTION_STRING)) {
						DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
						db.modifyDescriptionData(application.getCurrentResource().getDescription());

						if (!db.cancelled) {
							application.getCurrentResource().setDescription(db.returnDescription);
						}
					}
					
					// ******************************************************
					// Handle renaning a resource
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.RESOURCENAME_STRING)) {
						DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
						db.modifyResourceNameData(application.getCurrentResource().getResourceName());

						if (!db.cancelled) {
							bannedNames.remove(application.getCurrentResource().getResourceName());

							ResourceItem parent = application.getCurrentResource().getParentResource();
							if (parent != null) {
								if (application.getCurrentResource().getIsGetter()) {
									parent.removeGetter();
									application.getCurrentResource().setResourceName(db.returnName);
									parent.setGetter(application.getCurrentResource().asGetterItem());
								}
								else if (application.getCurrentResource().getIsPoster()) {
									parent.removePoster();
									application.getCurrentResource().setResourceName(db.returnName);
									parent.setPoster(application.getCurrentResource().asPosterItem());
								}
								else if (application.getCurrentResource().getIsPutter()) {
									parent.removePutter();
									application.getCurrentResource().setResourceName(db.returnName);
									parent.setPutter(application.getCurrentResource().asPutterItem());
								}
								else if (application.getCurrentResource().getIsDeleter()) {
									parent.removeDeleter();
									application.getCurrentResource().setResourceName(db.returnName);
									parent.setDeleter(application.getCurrentResource().asDeleterItem());
								}
								else if (application.getCurrentResource().getIsResource()) {
									parent.removeChild(parent.getChild(application.getCurrentResource().getResourceName()));
									application.getCurrentResource().setResourceName(db.returnName);
									parent.addChild(application.getCurrentResource().asResourceItem());
								}
							}
							else {
								application.getCurrentResource().setResourceName(db.returnName);
							}

							bannedNames.add(db.returnName);
						}
					}
					
					// ******************************************************
					// Handle editing a method
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.METHOD_STRING)) {
						DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);

						if (application.getCurrentResource().getIsResource())	{
							db.modifyMethodData(application.getCurrentResource().asResourceItem().getCollectionMethod(), application.getCurrentResource().asResourceItem().getCollectionMethodType());
							if (!db.cancelled) {
								application.getCurrentResource().asResourceItem().setCollectionMethod(db.returnMethod);
								application.getCurrentResource().asResourceItem().setCollectionMethodType(db.returnMethodType);
							}
						}
						if (application.getCurrentResource().getIsGetter()) {
							db.modifyMethodData(application.getCurrentResource().asGetterItem().getCallbackMethod(), application.getCurrentResource().asGetterItem().getCallbackMethodType());
							if (!db.cancelled) {
								application.getCurrentResource().asGetterItem().setCallbackMethod(db.returnMethod);
								application.getCurrentResource().asGetterItem().setCallbackMethodType(db.returnMethodType);
							}
						}
						if (application.getCurrentResource().getIsPoster()) {
							db.modifyMethodData(application.getCurrentResource().asPosterItem().getCallbackMethod(), application.getCurrentResource().asPosterItem().getCallbackMethodType());
							if (!db.cancelled) {
								application.getCurrentResource().asPosterItem().setCallbackMethod(db.returnMethod);
								application.getCurrentResource().asPosterItem().setCallbackMethodType(db.returnMethodType);
							}
						}
						if (application.getCurrentResource().getIsPutter()) {
							db.modifyMethodData(application.getCurrentResource().asPutterItem().getCallbackMethod(), application.getCurrentResource().asPutterItem().getCallbackMethodType());
							if (!db.cancelled) {
								application.getCurrentResource().asPutterItem().setCallbackMethod(db.returnMethod);
								application.getCurrentResource().asPutterItem().setCallbackMethodType(db.returnMethodType);
							}
						}
						if (application.getCurrentResource().getIsDeleter()) {
							db.modifyMethodData(application.getCurrentResource().asDeleterItem().getCallbackMethod(), application.getCurrentResource().asDeleterItem().getCallbackMethodType());
							if (!db.cancelled) {
								application.getCurrentResource().asDeleterItem().setCallbackMethod(db.returnMethod);
								application.getCurrentResource().asDeleterItem().setCallbackMethodType(db.returnMethodType);
							}
						}

					}
					
					// ******************************************************
					// Handle deleting a resource
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.DELETION_STRING)) {
						System.out.println("Deleting resource " + application.getCurrentResource().getResourceName());

						if (application.getCurrentResource().getIsGetter()) application.getCurrentResource().getParentResource().removeGetter();
						else if (application.getCurrentResource().getIsPoster()) application.getCurrentResource().getParentResource().removePoster();
						else if (application.getCurrentResource().getIsPutter()) application.getCurrentResource().getParentResource().removePutter();
						else if (application.getCurrentResource().getIsDeleter()) application.getCurrentResource().getParentResource().removeDeleter();
						else if (application.getCurrentResource().getIsResource()) {
							if(application.getCurrentResource() == application.getCurrentProject().getRootResource()) {
								application.getCurrentProject().setRootResource(null);
								application.setCurrentResource(null);
							} else {
								application.getCurrentResource().getParentResource().removeChild(application.getCurrentResource().asResourceItem());
							}
						}
						if(application.getCurrentResource() != null) {
							application.setCurrentResource(application.getCurrentResource().getParentResource());
						}
						reconsiderBans(application.getCurrentResource());
					}
					
					// ******************************************************
					// Handle adding a user
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.ADD_USER)) {
						DialogBoxFactory db = new DialogBoxFactory(application,mainUI.shell);
						db.getUserData();
						if (!db.cancelled) application.getCurrentResource().addUser(db.returnUsername, db.returnPassword);
					}
					
					// ******************************************************
					// Handle editing a user
					// ******************************************************
					else if(text.equalsIgnoreCase(Constants.EDIT_USER)) {
						if(mainUI.resourceDisplay.getViewStyle() == ViewStyle.PRETTY) {
							DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
							String tableEntry = ((TableItem) event.item).getText().trim();
							if(tableEntry.startsWith("User: ")) {
								Pattern p = Pattern.compile("User: \"([a-zA-Z_0-9]+)\" Password: \"([*;]+)\"");
								Matcher m = p.matcher(tableEntry);
								if(m.find()) {
									String user = m.group(1);
									db.modifyUserData(application.getCurrentResource().getUser(user));
									if (!db.cancelled) application.getCurrentResource().addUser(db.returnUsername, db.returnPassword);
								}
							}
						} else if(mainUI.resourceDisplay.getViewStyle() == ViewStyle.XML) {
							DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
							String tableEntry = ((TableItem) event.item).getText().trim();
							if(tableEntry.startsWith("<user>")) {
								Pattern p = Pattern.compile("<user><name>([a-zA-Z_0-9]+)</name></user>");
								Matcher m = p.matcher(tableEntry);
								if(m.find()) {
									String user = m.group(1);
									UserLoginData userData = application.getCurrentResource().getUser(user);
									db.modifyUserData(userData);
									if (!db.cancelled) {
										userData.setUsername(db.returnUsername);
										userData.setPassword(db.returnPassword);
									}
								}
							}
						} else if(mainUI.resourceDisplay.getViewStyle() == ViewStyle.GRAPHICAL) {
							DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
							db.displayMessage("Editing a user", "Editing a user is currently only supported in Pretty and XML view.");
						}
					}
					
					// ******************************************************
					// Handle deleting a user
					// ******************************************************
					else if (text.equalsIgnoreCase(Constants.DELETE_USER)) {
						if(mainUI.resourceDisplay.getViewStyle() == ViewStyle.PRETTY) {
							String tableEntry = ((TableItem) event.item).getText().trim();
							if(tableEntry.startsWith("User: ")) {
								Pattern p = Pattern.compile("User: \"([a-zA-Z_0-9]+)\" Password: \"([*;]+)\"");
								Matcher m = p.matcher(tableEntry);
								if(m.find()) {
									String user = m.group(1);
									application.getCurrentResource().deleteUser(user);
								}
							}
						} else if(mainUI.resourceDisplay.getViewStyle() == ViewStyle.XML) {
							String tableEntry = ((TableItem) event.item).getText().trim();
							if(tableEntry.startsWith("<user>")) {
								Pattern p = Pattern.compile("<user><name>([a-zA-Z_0-9]+)</name></user>");
								Matcher m = p.matcher(tableEntry);
								if(m.find()) {
									String user = m.group(1);
									application.getCurrentResource().deleteUser(user);
								}
							}
						} else if(mainUI.resourceDisplay.getViewStyle() == ViewStyle.GRAPHICAL) {
							DialogBoxFactory db = new DialogBoxFactory(application, mainUI.shell);
							db.displayMessage("Deleting a user", "Deleting a user is currently only supported in Pretty and XML view.");
						}
					} else if (text.equalsIgnoreCase(Constants.INHERIT_USERS)) {
						application.getCurrentResource().setInheritUsers(!application.getCurrentResource().getInheritUsers());
					}
					
					// ******************************************************
					// Every other string will be ignored
					// ******************************************************
				}
			}
		}
		
		// ******************************************************
		// Handle loading a file
		// ******************************************************
		if (fileTransfer.isSupportedType(event.currentDataType)){
			String[] files = (String[])event.data;
			if (files.length > 1) {
				TableItem item = new TableItem(mainUI.resourceDisplay, SWT.NONE);
				item.setText("Please insert only one file at a time!");
			}
			else {
				if (!files[0].endsWith(".xml")) {
					TableItem item = new TableItem(mainUI.resourceDisplay, SWT.NONE);
					item.setText("Please insert an XML file!");
				}
				else {
					TableItem item = new TableItem(mainUI.resourceDisplay, SWT.NONE);
					item.setText(files[0]);

					reconsiderBans(application.getCurrentProject().getRootResource());
					FileHandler fh = new FileHandler();
					application.getCurrentProject().setRootResource(fh.setupResourceStructure(files[0]).asResourceItem());
					if (application.getCurrentProject().getRootResource() == null) {
						item = new TableItem(mainUI.resourceDisplay, SWT.NONE);
						item.setText("That didn't work...");
					}
					else {
						application.setCurrentResource(application.getCurrentProject().getRootResource());
					}
				}
			}
		}
		mainUI.refresh();
	}
	
	private void reconsiderBans(AbstractResourceItem currentClass) {
		if (currentClass != null) {
			if (currentClass.getIsResource()) {
				// Remove itself
				System.out.print("Current bans: ");
				for (String name : bannedNames) System.out.print(name + " ");
				System.out.println();

				System.out.println("Removing ban for " + currentClass.asResourceItem().getClassName());

				bannedNames.remove(currentClass.asResourceItem().getClassName());

				// Remove children recursively
				for (String child : currentClass.asResourceItem().getChildren().keySet()) {
					reconsiderBans(currentClass.asResourceItem().getChildren().get(child));
				}
				
				if(currentClass.asResourceItem().hasGetter()) bannedNames.remove(currentClass.asResourceItem().getGetter().getResourceName());
				if(currentClass.asResourceItem().hasPoster()) bannedNames.remove(currentClass.asResourceItem().getPoster().getResourceName());
				if(currentClass.asResourceItem().hasPutter()) bannedNames.remove(currentClass.asResourceItem().getPutter().getResourceName());
				if(currentClass.asResourceItem().hasDeleter()) bannedNames.remove(currentClass.asResourceItem().getDeleter().getResourceName());
			}
			else {
				System.out.println("Removing " + currentClass.getResourceName());
				bannedNames.remove(currentClass.getResourceName());
			}
		}
		else {
			// Done
		}
	}
}
