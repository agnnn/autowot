package ch.ethz.inf.vs.wot.autowot.core;

/**
 * 
 * Class for parsing an XML project file
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.charset.MalformedInputException;

import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.DeleterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.GetterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PosterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PutterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;

public class FileHandler {

	int indentation = 0;

	/**
	 * Read the resource structure from an XML file given its name
	 * @param fileName - The name of the XML file to be read
	 * @return The root resource of the parsed resource structure
	 */
	public AbstractResourceItem setupResourceStructure(String fileName) {
		File inFile = null;
		PushbackReader reader = null;
		indentation = 0;

		// Open the file
		try {
			inFile = new File(fileName);
			reader = new PushbackReader(new FileReader(inFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Read header of the file
		if (!readNextTag(reader).equalsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
			System.err.println("Error: Wrong XML header");
		} else {
			// Parse the XML file
			try {
				return readDataRecursive(reader, null);
			} catch (MalformedInputException e) {
				e.printStackTrace();
			}			
		}

		return null;
	}
	
	/**
	 * Read the next lines, knowing it is a Getter
	 */
	protected AbstractResourceItem readGetterData(PushbackReader reader, ResourceItem parent) throws MalformedInputException {
		AbstractResourceItem thisItem = null;
		indentation++;
		
		// Parsing a Getter
		String name = readNextString(reader);
		String top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.GETTER_NAME_CLOSE)) throw new MalformedInputException(0);

		// Read method tag
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.GETTER_METHOD_OPEN)) throw new MalformedInputException(0);
		String method = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.GETTER_METHOD_CLOSE)) throw new MalformedInputException(0);
		
		// Read method type tage
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.GETTER_METHOD_TYPE_OPEN)) throw new MalformedInputException(0);
		String methodTypeString = readNextString(reader);
		HandlerCallbackType methodType = HandlerCallbackType.NONE;
		if(methodTypeString.equalsIgnoreCase("JAVA")) methodType = HandlerCallbackType.JAVA;
		if(methodTypeString.equalsIgnoreCase("SHELL")) methodType = HandlerCallbackType.SHELL;
		if(methodTypeString.equalsIgnoreCase("PYTHON")) methodType = HandlerCallbackType.PYTHON;
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.GETTER_METHOD_TYPE_CLOSE)) throw new MalformedInputException(0);
		
		String onChangeMethodName = null;

		top = readNextTag(reader);
		
		// Read OnChange method
		if (top.equalsIgnoreCase(Constants.GETTER_ONCHANGE_METHOD_OPEN)) {
			onChangeMethodName = readNextString(reader);
			top = readNextTag(reader);
			if (!top.equalsIgnoreCase(Constants.GETTER_ONCHANGE_METHOD_CLOSE)) throw new MalformedInputException(0);
		}
		else if (top.equalsIgnoreCase(Constants.GETTER_ONCHANGE_METHOD_NONE)) {
			System.out.println("No OnChange-Method Specified");
		} else {
			throw new MalformedInputException(0);
		}
		
		// Read OnChange method type
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.GETTER_ONCHANGE_METHOD_TYPE_OPEN)) throw new MalformedInputException(0);
		String onChnageMethodTypeString = readNextString(reader);
		HandlerCallbackType onChangeMethodType = HandlerCallbackType.NONE;
		if(onChnageMethodTypeString.equalsIgnoreCase("JAVA")) onChangeMethodType = HandlerCallbackType.JAVA;
		if(onChnageMethodTypeString.equalsIgnoreCase("SHELL")) onChangeMethodType = HandlerCallbackType.SHELL;
		if(onChnageMethodTypeString.equalsIgnoreCase("PYTHON")) onChangeMethodType = HandlerCallbackType.PYTHON;
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.GETTER_ONCHANGE_METHOD_TYPE_CLOSE)) throw new MalformedInputException(0);
		
		if (onChangeMethodName == null) thisItem = new GetterItem(name, method, methodType, parent);
		else thisItem = new GetterItem(name, method, methodType, onChangeMethodName, onChangeMethodType, parent);

		// Read description
		top = readNextTag(reader);
		if (top.equalsIgnoreCase(Constants.GETTER_DESCRIPTION_OPEN)) {
			thisItem.setDescription(readNextString(reader));
			top = readNextTag(reader);
			if (!top.equalsIgnoreCase(Constants.GETTER_DESCRIPTION_CLOSE)) throw new MalformedInputException(0);
		} else if(!top.equalsIgnoreCase(Constants.GETTER_DESCRIPTION_NONE)) {
			throw new MalformedInputException(0);
		}
		
		// Read security section
		top = readNextTag(reader);
		if(top.equalsIgnoreCase(Constants.SECURITY_OPEN)) {
			readSecurityData(reader, thisItem);
		} else {
			 throw new MalformedInputException(0);
		}
		
		indentation--;
		
		return thisItem;
	}
	
	/**
	 * Read the next lines, knowing it is a Poster
	 */
	protected AbstractResourceItem readPosterData(PushbackReader reader, ResourceItem parent) throws MalformedInputException {
		indentation++;
		
		// Read poster Name
		String name = readNextString(reader);
		String top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_NAME_CLOSE)) throw new MalformedInputException(0);
		
		// Read poster Method
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_METHOD_OPEN)) throw new MalformedInputException(0);
		String method = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_METHOD_CLOSE)) throw new MalformedInputException(0);
		
		// Read poster Method Type
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_METHOD_TYPE_OPEN)) throw new MalformedInputException(0);
		String methodTypeString = readNextString(reader);
		HandlerCallbackType methodType = HandlerCallbackType.NONE;
		if(methodTypeString.equalsIgnoreCase("JAVA")) methodType = HandlerCallbackType.JAVA;
		if(methodTypeString.equalsIgnoreCase("SHELL")) methodType = HandlerCallbackType.SHELL;
		if(methodTypeString.equalsIgnoreCase("PYTHON")) methodType = HandlerCallbackType.PYTHON;
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_METHOD_TYPE_CLOSE)) throw new MalformedInputException(0);
		
		// Read poster Argument Type
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_ARGTYPE_OPEN)) throw new MalformedInputException(0);
		String argType = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_ARGTYPE_CLOSE)) throw new MalformedInputException(0);

		// Read poster Argument Representation Type
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_SHOWTYPE_OPEN)) throw new MalformedInputException(0);
		String showType = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.POSTER_SHOWTYPE_CLOSE)) throw new MalformedInputException(0);
		
		// Create a new poster object with the data collected above
		AbstractResourceItem thisItem = new PosterItem(name, method, methodType, argType, showType, parent);

		// Read the poster description
		top = readNextTag(reader);
		if (top.equalsIgnoreCase(Constants.POSTER_DESCRIPTION_OPEN)) {
			thisItem.setDescription(readNextString(reader));
			top = readNextTag(reader);
			if (!top.equalsIgnoreCase(Constants.POSTER_DESCRIPTION_CLOSE)) throw new MalformedInputException(0);
		} else if(!top.equalsIgnoreCase(Constants.POSTER_DESCRIPTION_NONE)) {
			throw new MalformedInputException(0);
		}
		
		// Read security section
		top = readNextTag(reader);
		if(top.equalsIgnoreCase(Constants.SECURITY_OPEN)) {
			readSecurityData(reader, thisItem);
		} else {
			 throw new MalformedInputException(0);
		}
		
		indentation--;
		
		// Return Poster object
		return thisItem;
	}
	
	/**
	 * Read the next lines, knowing it is a Putter
	 */
	protected AbstractResourceItem readPutterData(PushbackReader reader, ResourceItem parent) throws MalformedInputException {
		indentation++;
		
		// Read putter name
		String name = readNextString(reader);
		String top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_NAME_CLOSE)) throw new MalformedInputException(0);

		// Read putter method
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_METHOD_OPEN)) throw new MalformedInputException(0);
		String method = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_METHOD_CLOSE)) throw new MalformedInputException(0);
		
		// Read putter method type
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_METHOD_TYPE_OPEN)) throw new MalformedInputException(0);
		String methodTypeString = readNextString(reader);
		HandlerCallbackType methodType = HandlerCallbackType.NONE;
		if(methodTypeString.equalsIgnoreCase("JAVA")) methodType = HandlerCallbackType.JAVA;
		if(methodTypeString.equalsIgnoreCase("SHELL")) methodType = HandlerCallbackType.SHELL;
		if(methodTypeString.equalsIgnoreCase("PYTHON")) methodType = HandlerCallbackType.PYTHON;
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_METHOD_TYPE_CLOSE)) throw new MalformedInputException(0);
		
		// Read putter argument type
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_ARGTYPE_OPEN)) throw new MalformedInputException(0);
		String argType = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_ARGTYPE_CLOSE)) throw new MalformedInputException(0);
		
		// Read putter representation type
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_SHOWTYPE_OPEN)) throw new MalformedInputException(0);
		String showType = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.PUTTER_SHOWTYPE_CLOSE)) throw new MalformedInputException(0);

		AbstractResourceItem thisItem = new PutterItem(name, method, methodType, argType, showType, parent);

		// Read description
		top = readNextTag(reader);
		if (top.equalsIgnoreCase(Constants.PUTTER_DESCRIPTION_OPEN)) {
			thisItem.setDescription(readNextString(reader));
			top = readNextTag(reader);
			System.out.println("TOP: " + top);
			if (!top.equalsIgnoreCase(Constants.PUTTER_DESCRIPTION_CLOSE)) throw new MalformedInputException(0);
		}  else if(!top.equalsIgnoreCase(Constants.PUTTER_DESCRIPTION_NONE)) {
			throw new MalformedInputException(0);
		}
		
		// Read security section
		top = readNextTag(reader);
		if(top.equalsIgnoreCase(Constants.SECURITY_OPEN)) {
			readSecurityData(reader, thisItem);
		} else {
			 throw new MalformedInputException(0);
		}

		indentation--;
		
		return thisItem;
	}
	
	/**
	 * Read the next lines, knowing it is a Deleter
	 */
	protected AbstractResourceItem readDeleterData(PushbackReader reader, ResourceItem parent) throws MalformedInputException {
		indentation++;
		
		// Read deleter name
		String name = readNextString(reader);
		String top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.DELETER_NAME_CLOSE)) throw new MalformedInputException(0);

		// Read deleter method
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.DELETER_METHOD_OPEN)) throw new MalformedInputException(0);
		String method = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.DELETER_METHOD_CLOSE)) throw new MalformedInputException(0);
		
		// Read deleter method type
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.DELETER_METHOD_TYPE_OPEN)) throw new MalformedInputException(0);
		String methodTypeString = readNextString(reader);
		HandlerCallbackType methodType = HandlerCallbackType.NONE;
		if(methodTypeString.equalsIgnoreCase("JAVA")) methodType = HandlerCallbackType.JAVA;
		if(methodTypeString.equalsIgnoreCase("SHELL")) methodType = HandlerCallbackType.SHELL;
		if(methodTypeString.equalsIgnoreCase("PYTHON")) methodType = HandlerCallbackType.PYTHON;
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.DELETER_METHOD_TYPE_CLOSE)) throw new MalformedInputException(0);
		
		AbstractResourceItem thisItem = new DeleterItem(name, method, methodType, parent);

		// Read description
		top = readNextTag(reader);			
		System.out.println("Top before description: " + top);
		if (top.equalsIgnoreCase(Constants.DELETER_DESCRIPTION_OPEN)) {
			thisItem.setDescription(readNextString(reader));
			top = readNextTag(reader);
			if (!top.equalsIgnoreCase(Constants.DELETER_DESCRIPTION_CLOSE)) throw new MalformedInputException(0);
		}  else if(!top.equalsIgnoreCase(Constants.DELETER_DESCRIPTION_NONE)) {
			throw new MalformedInputException(0);
		}
		
		// Read security section
		top = readNextTag(reader);
		if(top.equalsIgnoreCase(Constants.SECURITY_OPEN)) {
			readSecurityData(reader, thisItem);
		} else {
			 throw new MalformedInputException(0);
		}

		indentation--;
		
		return thisItem;
	}
	
	/**
	 * Read the next lines, knowing it is a security section
	 */
	protected void readSecurityData(PushbackReader reader, AbstractResourceItem thisItem) throws MalformedInputException {
		indentation++;
		
		// Read the Inherit-User-Flag
		String top = readNextTag(reader);
		if(!top.equalsIgnoreCase(Constants.SECURITY_INHERIT_OPEN)) throw new MalformedInputException(0);
		thisItem.setInheritUsers(new Boolean(readNextString(reader)));
		top = readNextTag(reader);
		if(!top.equalsIgnoreCase(Constants.SECURITY_INHERIT_CLOSE)) throw new MalformedInputException(0);
		indentation--;

		// Read the Beginning of the user list (if any)
		top = readNextTag(reader);
		if(top.equalsIgnoreCase(Constants.SECURITY_USER_LIST_OPEN)) {
			indentation++;
			while(true) {
				top = readNextTag(reader);
				if(top.equalsIgnoreCase(Constants.SECURITY_USER_LIST_CLOSE)) {
					// Read until List Closing Tag
					indentation--;
					top = readNextTag(reader);
					break;
				} else if(top.equalsIgnoreCase(Constants.SECURITY_USER_OPEN)) {
					// Read the Hash of the user and add it to the object
					String userHash = readNextString(reader);
					thisItem.addUser(userHash);
					top = readNextTag(reader);
					if(!top.equalsIgnoreCase(Constants.SECURITY_USER_CLOSE)) throw new MalformedInputException(0);
				} else {
					throw new MalformedInputException(0);
				}
			}
			indentation--;
		}
		
		// Ensure user list tag has indeed been closed
		if (!top.equalsIgnoreCase(Constants.SECURITY_CLOSE)) throw new MalformedInputException(0);
	}
	
	/**
	 * Read the next lines, knowing it is a Resource
	 */
	protected AbstractResourceItem readResourceData(PushbackReader reader, ResourceItem parent) throws MalformedInputException {
		indentation++;
		// It's a resource

		// Get resource name
		String top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_NAME_OPEN)) throw new MalformedInputException(0);
		String name = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_NAME_CLOSE)) throw new MalformedInputException(0);

		// Get resource uri
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_URI_OPEN)) throw new MalformedInputException(0);
		String uri = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_URI_CLOSE)) throw new MalformedInputException(0);

		// Get resource methods
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_METHODS_OPEN)) throw new MalformedInputException(0);
		String methods = readNextString(reader);
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_METHODS_CLOSE)) throw new MalformedInputException(0);

		// Get collection method
		Boolean isCollection = false;
		String collectionMethod = "";
		top = readNextTag(reader);
		System.out.println("top: " + top);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_COLLECTION_METHOD_NONE)) {
			collectionMethod = readNextString(reader); 
			top = readNextTag(reader);
			if (!top.equalsIgnoreCase(Constants.RESOURCE_COLLECTION_METHOD_CLOSE)) throw new MalformedInputException(0);

			Integer collectionLength = collectionMethod.split("\\.").length;
			System.out.println("Original: " + collectionMethod);

			System.out.println("Length: " + collectionLength);
			// String className = collectionMethod.split("\\.")[collectionLength - 2];
			String methodName = collectionMethod.split("\\.")[collectionLength - 1];
			// collectionMethod = className + "." + methodName;
			collectionMethod = methodName;
			System.out.println("COLLECTION: " + collectionMethod);
			isCollection = true;				
		}
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_COLLECTION_METHOD_TYPE_OPEN)) throw new MalformedInputException(0);
		String collectionMethodTypeString = readNextString(reader);
		HandlerCallbackType collectionMethodType = HandlerCallbackType.NONE;
		if(collectionMethodTypeString.equalsIgnoreCase("JAVA")) collectionMethodType = HandlerCallbackType.JAVA;
		if(collectionMethodTypeString.equalsIgnoreCase("SHELL")) collectionMethodType = HandlerCallbackType.SHELL;
		if(collectionMethodTypeString.equalsIgnoreCase("PYTHON")) collectionMethodType = HandlerCallbackType.PYTHON;
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_COLLECTION_METHOD_TYPE_CLOSE)) throw new MalformedInputException(0);

		String className = Constants.makeClassName(name);
		AbstractResourceItem thisItem = new ResourceItem(className, name, uri, methods, isCollection, collectionMethod, collectionMethodType, parent);

		// Get resource description
		top = readNextTag(reader);
		if (!top.equalsIgnoreCase(Constants.RESOURCE_DESCRIPTION_NONE)) {
			thisItem.setDescription(readNextString(reader));
			top = readNextTag(reader);
			if (!top.equalsIgnoreCase(Constants.RESOURCE_DESCRIPTION_CLOSE)) throw new MalformedInputException(0);
		}

		top = readNextTag(reader);
		if (top.equalsIgnoreCase(Constants.RESOURCE_CHILDREN_OPEN)) {
			indentation++;

			top = readNextTag(reader);
			// System.out.println("\tAre there children...");
			// Second option to take the types of children into account
			while (top.equalsIgnoreCase(Constants.RESOURCE_CHILD_OPEN) || top.startsWith(Constants.RESOURCE_CHILD_OPEN.substring(0, Constants.RESOURCE_CHILD_OPEN.length() - 1))) {
				// System.out.println("\t\t!");
				thisItem.asResourceItem().addChild((ResourceItem) readDataRecursive(reader, thisItem.asResourceItem()));
				top = readNextTag(reader);		// Burn this one - it's the close-tag of the child's name [if the XML is well-formed]
				if (!top.startsWith("</")) throw new MalformedInputException(0);
				top = readNextTag(reader);
				if (!top.equalsIgnoreCase(Constants.RESOURCE_CHILD_CLOSE)) throw new MalformedInputException(0);
				top = readNextTag(reader);		// This is the close-tag "</children>" or another "<child>" [if the XML is well-formed]
			}

			if (!top.equalsIgnoreCase(Constants.RESOURCE_CHILDREN_CLOSE)) throw new MalformedInputException(0);

			indentation--;
		}

		top = readNextTag(reader);
		if (top.equalsIgnoreCase(Constants.RESOURCE_GETTER_OPEN)) {
			indentation++;
			
			thisItem.asResourceItem().setGetter((GetterItem) readDataRecursive(reader, thisItem.asResourceItem()));
			top = readNextTag(reader);		// This is the close-tag "</getter>" [if the XML is well-formed]
			if (!top.equalsIgnoreCase(Constants.RESOURCE_GETTER_CLOSE)) throw new MalformedInputException(0);
			
			indentation--;
		}

		top = readNextTag(reader);
		if (top.equalsIgnoreCase(Constants.RESOURCE_POSTER_OPEN)) {
			indentation++;
			
			System.out.println("\t\t!");
			thisItem.asResourceItem().setPoster((PosterItem) readDataRecursive(reader, thisItem.asResourceItem()));
			top = readNextTag(reader);		// This is the close-tag "</poster>" [if the XML is well-formed]
			if (!top.equalsIgnoreCase(Constants.RESOURCE_POSTER_CLOSE)) throw new MalformedInputException(0);
				
			indentation--;
		}

		top = readNextTag(reader);
		if (top.equalsIgnoreCase(Constants.RESOURCE_PUTTER_OPEN)) {
			indentation++;
			
			thisItem.asResourceItem().setPutter((PutterItem) readDataRecursive(reader, thisItem.asResourceItem()));
			top = readNextTag(reader);		// This is the close-tag "</poster>" [if the XML is well-formed]
			if (!top.equalsIgnoreCase(Constants.RESOURCE_PUTTER_CLOSE)) throw new MalformedInputException(0);
			
			indentation--;
		}

		top = readNextTag(reader);
		if (top.equalsIgnoreCase(Constants.RESOURCE_DELETER_OPEN)) {
			indentation++;
			
			thisItem.asResourceItem().setDeleter((DeleterItem) readDataRecursive(reader, thisItem.asResourceItem()));
			top = readNextTag(reader);		// This is the close-tag "</getter>" [if the XML is well-formed]
			if (!top.equalsIgnoreCase(Constants.RESOURCE_DELETER_CLOSE)) throw new MalformedInputException(0);
			
			indentation--;
		}

		// Read security section
		top = readNextTag(reader);
		if(top.equalsIgnoreCase(Constants.SECURITY_OPEN)) {
			readSecurityData(reader, thisItem);
		} else {
			 throw new MalformedInputException(0);
		}
		
		return thisItem;
	}
	
	/**
	 * Method for recursively reading resources
	 */
	protected AbstractResourceItem readDataRecursive(PushbackReader reader, ResourceItem parent) throws MalformedInputException {
		AbstractResourceItem thisItem = null;

		String top = readNextTag(reader);

		if (top.startsWith(Constants.POSTER_NAME_OPEN)) {
			thisItem = readPosterData(reader, parent);
		} else if (top.startsWith(Constants.GETTER_NAME_OPEN)) {
			thisItem = readGetterData(reader, parent);
		} else if (top.startsWith(Constants.PUTTER_NAME_OPEN)) {
			thisItem = readPutterData(reader, parent);
		} else if (top.startsWith(Constants.DELETER_NAME_OPEN)) {
			thisItem = readDeleterData(reader, parent);
		} else {
			thisItem = readResourceData(reader, parent);
		}
		return thisItem;
	}
	
	/**
	 * Read the name of the next tag
	 */
	private String readNextTag(PushbackReader reader) {
		String returnString = "";
		char currentChar = ' ';

		do {
			try {
				currentChar = (char) reader.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (currentChar != '<');

		returnString += currentChar;

		while (currentChar != '>') {
			try {
				currentChar = (char) reader.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			returnString += currentChar;
		}

		for (int i = 0; i < indentation; i++) System.out.print("\t");
		System.out.println(returnString);
		return returnString;
	}
	
	/**
	 * Read the content of the next tag
	 */
	private String readNextString(PushbackReader reader) {
		String returnString = "";
		char currentChar = ' ';

		try {
			currentChar = (char) reader.read();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (currentChar != '<') {
			returnString += currentChar;
			try {
				currentChar = (char) reader.read();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}

		try {
			reader.unread('<');
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < indentation; i++) System.out.print("\t");
		System.out.println(returnString);
		return returnString;
	}
}