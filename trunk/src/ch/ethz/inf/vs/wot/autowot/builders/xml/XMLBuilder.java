package ch.ethz.inf.vs.wot.autowot.builders.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.project.Project;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.DeleterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.GetterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PosterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PutterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.security.UserLoginData;

/**
 * Builds an XML configuration file when given an AutoWOT project
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class XMLBuilder {
	
	private static Integer indentation = 0;
	private static String currentHandlerCanonicalName = "";
	public static String resourceConfigTempFolder = "resources_obfuscated";
	public static String resourceConfigTempLocation = "resources_obfuscated/configuration.xml";
	
	/**
	 * Creates XML file of a project
	 */
	public static void createXMLStructure(Project project) {
		if (project.getRootResource() != null) {
			File outFile = null;
			BufferedWriter writer = null;
			
			outFile = new File(resourceConfigTempFolder);
			System.out.println("Creating " + outFile.getAbsolutePath());
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			
			// Open file/create file
			try {
				// TODO wrong file load
				outFile = new File(resourceConfigTempLocation);
				
				// Create file if it does not exist
				boolean success = outFile.createNewFile();
				if (success) {
					System.out.println(resourceConfigTempLocation + " created!");
					// File did not exist and was created
				} else {
					// File already existed
				}
				
				writer = new BufferedWriter(new FileWriter(outFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			writeLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer);
			writeLine("", writer);
			
			System.out.print("Creating XML...");
			indentation = 0;
			currentHandlerCanonicalName = project.getHandlerCanonical();
			createXMLrecursive(project.getRootResource(), writer);
			
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("File created!");
		}
	}
	
	/**
	 * Parse created resource structure recursively into this file
	 */
	protected static void createXMLrecursive(AbstractResourceItem currentItem, BufferedWriter writer) {
		// Write output to file for a subresource
		if (currentItem.getIsResource()) {
			createXMLResource(currentItem, writer);
		}

		// Write output to file for a getter
		else if (currentItem.getIsGetter()) {
			createXMLGetter(currentItem, writer);
		}

		// Write output to file for a poster
		else if (currentItem.getIsPoster()) {
			createXMLPoster(currentItem, writer);
		}

		// Write output to file for a putter
		else if (currentItem.getIsPutter()) {
			createXMLPutter(currentItem, writer);
		}

		// Write output to file for a deleter
		else if (currentItem.getIsDeleter()) {
			createXMLDeleter(currentItem, writer);
		}
	}
	
	protected static void createXMLGetter(AbstractResourceItem currentItem, BufferedWriter writer) {
		GetterItem currentRes = currentItem.asGetterItem();
		
		writeLine(Constants.RESOURCE_GETTER_OPEN, writer);
		indentation++;
		
		writeLine(Constants.GETTER_NAME_OPEN + currentRes.getResourceName() + Constants.GETTER_NAME_CLOSE, writer);
		writeLine(Constants.GETTER_METHOD_OPEN + currentRes.getCallbackMethod() + Constants.GETTER_METHOD_CLOSE, writer);
		writeLine(Constants.GETTER_METHOD_TYPE_OPEN + currentRes.getCallbackMethodType().toString() + Constants.GETTER_METHOD_TYPE_CLOSE, writer);
		
		if (currentRes.getOnChangeMethod() == null) {
			writeLine(Constants.GETTER_ONCHANGE_METHOD_NONE, writer);
			writeLine(Constants.GETTER_ONCHANGE_METHOD_TYPE_OPEN + HandlerCallbackType.NONE + Constants.GETTER_ONCHANGE_METHOD_TYPE_CLOSE, writer);
		} else {
			writeLine(Constants.GETTER_ONCHANGE_METHOD_OPEN + currentRes.getOnChangeMethod() + Constants.GETTER_ONCHANGE_METHOD_CLOSE, writer);
			writeLine(Constants.GETTER_ONCHANGE_METHOD_TYPE_OPEN + currentRes.getOnChangeMethodType().toString() + Constants.GETTER_ONCHANGE_METHOD_TYPE_CLOSE, writer);
		}
		
		if (currentRes.getDescription() == null)
			writeLine(Constants.GETTER_DESCRIPTION_NONE, writer);
		else
			writeLine(Constants.GETTER_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.GETTER_DESCRIPTION_CLOSE, writer);
		
		createXMLSecurity(currentRes, writer);
		
		indentation--;
		writeLine(Constants.RESOURCE_GETTER_CLOSE, writer);
	}
	
	protected static void createXMLPoster(AbstractResourceItem currentItem, BufferedWriter writer) {
		PosterItem currentRes = currentItem.asPosterItem();
		
		writeLine(Constants.RESOURCE_POSTER_OPEN, writer);
		indentation++;
		
		writeLine(Constants.POSTER_NAME_OPEN + currentRes.getResourceName() + Constants.POSTER_NAME_CLOSE, writer);
		writeLine(Constants.POSTER_METHOD_OPEN + currentRes.getCallbackMethod() + Constants.POSTER_METHOD_CLOSE, writer);
		writeLine(Constants.POSTER_METHOD_TYPE_OPEN + currentRes.getCallbackMethodType().toString() + Constants.POSTER_METHOD_TYPE_CLOSE, writer);
		writeLine(Constants.POSTER_ARGTYPE_OPEN + currentRes.getPosterArgumentType() + Constants.POSTER_ARGTYPE_CLOSE, writer);
		writeLine(Constants.POSTER_SHOWTYPE_OPEN + currentRes.getPosterPresentationType() + Constants.POSTER_SHOWTYPE_CLOSE, writer);
		
		if (currentRes.getDescription() == null)
			writeLine(Constants.POSTER_DESCRIPTION_NONE, writer);
		else
			writeLine(Constants.POSTER_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.POSTER_DESCRIPTION_CLOSE, writer);
		
		createXMLSecurity(currentRes, writer);
		
		indentation--;
		writeLine(Constants.RESOURCE_POSTER_CLOSE, writer);
	}
	
	protected static void createXMLPutter(AbstractResourceItem currentItem, BufferedWriter writer) {
		PutterItem currentRes = currentItem.asPutterItem();
		
		writeLine(Constants.RESOURCE_PUTTER_OPEN, writer);
		indentation++;
		
		writeLine(Constants.PUTTER_NAME_OPEN + currentRes.getResourceName() + Constants.PUTTER_NAME_CLOSE, writer);
		writeLine(Constants.PUTTER_METHOD_OPEN + currentRes.getCallbackMethod() + Constants.PUTTER_METHOD_CLOSE, writer);
		writeLine(Constants.PUTTER_METHOD_TYPE_OPEN + currentRes.getCallbackMethodType().toString() + Constants.PUTTER_METHOD_TYPE_CLOSE, writer);
		writeLine(Constants.PUTTER_ARGTYPE_OPEN + currentRes.getPutterArgumentType() + Constants.PUTTER_ARGTYPE_CLOSE, writer);
		writeLine(Constants.PUTTER_SHOWTYPE_OPEN + currentRes.getPutterPresentationType() + Constants.PUTTER_SHOWTYPE_CLOSE, writer);
		
		if (currentRes.getDescription() == null)
			writeLine(Constants.PUTTER_DESCRIPTION_NONE, writer);
		else
			writeLine(Constants.PUTTER_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.PUTTER_DESCRIPTION_CLOSE, writer);
		
		createXMLSecurity(currentRes, writer);
		
		indentation--;
		writeLine(Constants.RESOURCE_PUTTER_CLOSE, writer);
	}
	
	protected static void createXMLDeleter(AbstractResourceItem currentItem, BufferedWriter writer) {
		DeleterItem currentRes = currentItem.asDeleterItem();
		
		writeLine(Constants.RESOURCE_DELETER_OPEN, writer);
		indentation++;
		
		writeLine(Constants.DELETER_NAME_OPEN + currentRes.getResourceName() + Constants.DELETER_NAME_CLOSE, writer);
		writeLine(Constants.DELETER_METHOD_OPEN + currentRes.getCallbackMethod() + Constants.DELETER_METHOD_CLOSE, writer);
		writeLine(Constants.DELETER_METHOD_TYPE_OPEN + currentRes.getCallbackMethodType().toString() + Constants.DELETER_METHOD_TYPE_CLOSE, writer);
		
		if (currentRes.getDescription() == null)
			writeLine(Constants.DELETER_DESCRIPTION_NONE, writer);
		else
			writeLine(Constants.DELETER_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.DELETER_DESCRIPTION_CLOSE, writer);
		
		createXMLSecurity(currentRes, writer);
		
		indentation--;
		writeLine(Constants.RESOURCE_DELETER_CLOSE, writer);
	}
	
	protected static void createXMLResource(AbstractResourceItem currentItem, BufferedWriter writer) {
		ResourceItem currentRes = currentItem.asResourceItem();
		
		// Classname
		writeLine("<" + currentRes.getClassName() + ">", writer);
		indentation++;
		
		// Information about the class
		writeLine(Constants.RESOURCE_NAME_OPEN + currentRes.getResourceName() + Constants.RESOURCE_NAME_CLOSE, writer);
		writeLine(Constants.RESOURCE_URI_OPEN + currentRes.getURI() + Constants.RESOURCE_URI_CLOSE, writer);
		writeLine(Constants.RESOURCE_METHODS_OPEN + currentRes.getMethods() + Constants.RESOURCE_METHODS_CLOSE, writer);
		
		if (currentRes.getCollectionMethod() == null) {
			writeLine(Constants.RESOURCE_COLLECTION_METHOD_NONE, writer);
			writeLine(Constants.RESOURCE_COLLECTION_METHOD_TYPE_OPEN + HandlerCallbackType.NONE + Constants.RESOURCE_COLLECTION_METHOD_TYPE_CLOSE, writer);
		} else {
			writeLine(Constants.RESOURCE_COLLECTION_METHOD_OPEN + currentHandlerCanonicalName + "." + currentRes.getCollectionMethod() + Constants.RESOURCE_COLLECTION_METHOD_CLOSE, writer);
			writeLine(Constants.RESOURCE_COLLECTION_METHOD_TYPE_OPEN + currentRes.getCollectionMethodType().toString() + Constants.RESOURCE_COLLECTION_METHOD_TYPE_CLOSE, writer);
		}
		
		System.out.println("Description: " + currentRes.getDescription());
		if (currentRes.getDescription() == null) {
			
			writeLine(Constants.RESOURCE_DESCRIPTION_NONE, writer);
		} else
			writeLine(Constants.RESOURCE_DESCRIPTION_OPEN + currentRes.getDescription() + Constants.RESOURCE_DESCRIPTION_CLOSE, writer);
		
		// Children
		if (currentRes.getChildren().isEmpty()) {
			writeLine(Constants.RESOURCE_CHILDREN_NONE, writer);
		} else {
			writeLine(Constants.RESOURCE_CHILDREN_OPEN, writer);
			indentation++;
			
			for (String childName : currentRes.getChildNames()) {
				ResourceItem child = currentRes.getChildren().get(childName);
				
				if (child.isCollection())
					writeLine(Constants.RESOURCE_CHILD_OPEN.substring(0, Constants.RESOURCE_CHILD_OPEN.length() - 1) + " type=\"collection\">", writer);
				else
					writeLine(Constants.RESOURCE_CHILD_OPEN, writer);
				
				indentation++;
				createXMLrecursive(child, writer);
				indentation--;
				writeLine(Constants.RESOURCE_CHILD_CLOSE, writer);
			}
			
			indentation--;
			writeLine(Constants.RESOURCE_CHILDREN_CLOSE, writer);
		}
		
		// Getters
		if (!currentRes.hasGetter()) {
			writeLine(Constants.RESOURCE_GETTER_NONE, writer);
		} else {
			GetterItem child = currentRes.getGetter();
			createXMLrecursive(child, writer);
		}
		
		// Handle Posters
		if (!currentRes.hasPoster()) {
			writeLine(Constants.RESOURCE_POSTER_NONE, writer);
		} else {
			PosterItem child = currentRes.getPoster();
			createXMLrecursive(child, writer);
		}
		
		// Handle Putters
		if (!currentRes.hasPutter()) {
			writeLine(Constants.RESOURCE_PUTTER_NONE, writer);
		} else {
			PutterItem child = currentRes.getPutter();
			createXMLrecursive(child, writer);
		}
		
		// Handle Deleters
		if (!currentRes.hasDeleter()) {
			writeLine(Constants.RESOURCE_DELETER_NONE, writer);
		} else {
			DeleterItem child = currentRes.getDeleter();
			createXMLrecursive(child, writer);
		}
		
		createXMLSecurity(currentRes, writer);
		
		indentation--;
		writeLine("</" + currentRes.getClassName() + ">", writer);
	}
	
	protected static void createXMLSecurity(AbstractResourceItem currentRes, BufferedWriter writer) {
		writeLine(Constants.SECURITY_OPEN, writer);
		indentation++;
		writeLine(Constants.SECURITY_INHERIT_OPEN + currentRes.getInheritUsers().toString() + Constants.SECURITY_INHERIT_CLOSE, writer);
		if (!currentRes.getAuthorizedUsers().isEmpty()) {
			writeLine(Constants.SECURITY_USER_LIST_OPEN, writer);
			indentation++;
			for (UserLoginData user : currentRes.getAuthorizedUsers()) {
				writeLine(Constants.SECURITY_USER_OPEN + user.getBase64Hash() + Constants.SECURITY_USER_CLOSE, writer);
			}
			indentation--;
			writeLine(Constants.SECURITY_USER_LIST_CLOSE, writer);
		}
		indentation--;
		writeLine(Constants.SECURITY_CLOSE, writer);
	}
	
	/**
	 * Handles writing a String into a file
	 */
	protected static void writeLine(String content, BufferedWriter writer) {
		try {
			for (int i = 0; i < indentation; i++)
				writer.write("\t");
			writer.write(content);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
