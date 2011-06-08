package ch.ethz.inf.vs.wot.autowot.commons;

/**
 * Constants and utility methods
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class Constants {
	public static String MANAGEMENT_PACKAGE = "";
	public static String RESOURCE_PACKAGE = "";
	
	public final static String BASE_FOLDER_NAME = "AutomaticPrototypes/";
	public final static String PROTOTYPES_FOLDER_NAME = BASE_FOLDER_NAME + "PlainJava/prototypes/";
	public final static String WEBRESOURCES_FOLDER_NAME = BASE_FOLDER_NAME + "PlainJava/webresources/";
	public final static String IMAGES_FOLDER_NAME = WEBRESOURCES_FOLDER_NAME + "images/";
	public final static String SCRIPTS_FOLDER_NAME = WEBRESOURCES_FOLDER_NAME + "scripts/";
	
	public static final String CONFIG_FILE_NAME = "structure_latest.xml";
	public static final String DOCUMENTATION_FILE_NAME = "ReadMe.txt";
	public static final String RESTLET_MAIN_NAME = "RestletMain.java";
	public static final String BASE_RESOURCE_NAME = "BaseResource.java";
	public static final String CSS_FILE_NAME = "main.css";
	
	public static final String SOURCE_FOLDER = "resources/";	// This was formerly "building_blocks/", but that directory was added to the source folders
	public static final String RESTLET_MAIN_SOURCE_NAME = SOURCE_FOLDER + "RestletMain";
	public static final String BASE_RESOURCE_SOURCE_NAME = SOURCE_FOLDER + "BaseResource";
	public static final String CSS_FILE_SOURCE_NAME = SOURCE_FOLDER + "main.css";
		
	public final static String RESOURCE_STRING = "   <RootResource>";
	public final static String CHILD_STRING = "   <Child>";
	public final static String GETTER_STRING = "   <Getter>";
	public final static String POSTER_STRING = "   <Poster>";
	public static final String PUTTER_STRING = "   <Putter>";
	public static final String DELETER_STRING = "   <Deleter>";
	public static final String DELETION_STRING = "   <Delete>";
	public final static String RESOURCENAME_STRING = "   <ResourceName>";
	public final static String DESCRIPTION_STRING = "   <Description>";
	public final static String METHOD_STRING = "   <Method>";
	public static final String ADD_USER = "   <Add User>";
	public static final String EDIT_USER = "   <Edit User>";
	public static final String DELETE_USER = "   <Delete User>";
	public static final String INHERIT_USERS = "   <Toggle User Inheritance>";
	
	public final static String RESOURCE_NAME_OPEN = "<resourceName>";
	public final static String RESOURCE_NAME_CLOSE = "</resourceName>";
	public final static String RESOURCE_URI_OPEN = "<resourceURI>";
	public final static String RESOURCE_URI_CLOSE = "</resourceURI>";
	public final static String RESOURCE_DESCRIPTION_OPEN = "<resourceDescription>";
	public final static String RESOURCE_DESCRIPTION_CLOSE = "</resourceDescription>";
	public final static String RESOURCE_DESCRIPTION_NONE = "<resourceDescription/>";
	public final static String RESOURCE_METHODS_OPEN = "<methods>";
	public final static String RESOURCE_METHODS_CLOSE = "</methods>";
	public final static String RESOURCE_PUBLIC_METHODS_OPEN = "<publicMethods>";
	public final static String RESOURCE_PUBLIC_METHODS_CLOSE = "</publicMethods>";
	public final static String RESOURCE_RESTRICTED_METHODS_OPEN = "<restrictedMethods>";
	public final static String RESOURCE_RESTRICTED_METHODS_CLOSE = "</restrictedMethods>";
	public static final String RESOURCE_COLLECTION_METHOD_OPEN = "<collectionMethod>";
	public static final String RESOURCE_COLLECTION_METHOD_CLOSE = "</collectionMethod>";
	public static final String RESOURCE_COLLECTION_METHOD_NONE = "<collectionMethod/>";
	public static final String RESOURCE_COLLECTION_METHOD_TYPE_OPEN = "<collectionMethodType>";
	public static final String RESOURCE_COLLECTION_METHOD_TYPE_CLOSE = "</collectionMethodType>";
	
	public final static String RESOURCE_CHILDREN_OPEN = "<children>";
	public final static String RESOURCE_CHILDREN_CLOSE = "</children>";
	public final static String RESOURCE_CHILDREN_NONE = "<children/>";
	public final static String RESOURCE_CHILD_OPEN = "<child>";
	public final static String RESOURCE_CHILD_CLOSE = "</child>";
	public final static String RESOURCE_GETTER_NONE = "<getter/>";
	public final static String RESOURCE_GETTER_OPEN = "<getter>";
	public final static String RESOURCE_GETTER_CLOSE = "</getter>";
	public final static String RESOURCE_POSTER_NONE = "<poster/>";
	public final static String RESOURCE_POSTER_OPEN = "<poster>";
	public final static String RESOURCE_POSTER_CLOSE = "</poster>";
	public final static String RESOURCE_PUTTER_NONE = "<putter/>";
	public final static String RESOURCE_PUTTER_OPEN = "<putter>";
	public final static String RESOURCE_PUTTER_CLOSE = "</putter>";
	public final static String RESOURCE_DELETER_NONE = "<deleter/>";
	public final static String RESOURCE_DELETER_OPEN = "<deleter>";
	public final static String RESOURCE_DELETER_CLOSE = "</deleter>";
	
	public static final String GETTER_NAME_OPEN = "<getterName>";
	public static final String GETTER_NAME_CLOSE = "</getterName>";
	public static final String GETTER_METHOD_OPEN = "<getterMethod>";
	public static final String GETTER_METHOD_CLOSE = "</getterMethod>";
	public static final String GETTER_METHOD_TYPE_OPEN = "<getterMethodType>";
	public static final String GETTER_METHOD_TYPE_CLOSE = "</getterMethodType>";
	public static final String GETTER_DESCRIPTION_OPEN = "<getterDescription>";
	public static final String GETTER_DESCRIPTION_CLOSE = "</getterDescription>";
	public static final String GETTER_DESCRIPTION_NONE = "<getterDescription/>";
	public static final String GETTER_ONCHANGE_METHOD_OPEN = "<getterOnChangeMethod>";
	public static final String GETTER_ONCHANGE_METHOD_CLOSE = "</getterOnChangeMethod>";
	public static final String GETTER_ONCHANGE_METHOD_NONE = "<getterOnChangeMethod/>";
	public static final String GETTER_ONCHANGE_METHOD_TYPE_OPEN = "<getterOnChangeMethodType>";
	public static final String GETTER_ONCHANGE_METHOD_TYPE_CLOSE = "</getterOnChangeMethodType>";
	
	public static final String POSTER_NAME_OPEN = "<posterName>";
	public static final String POSTER_NAME_CLOSE = "</posterName>";
	public static final String POSTER_METHOD_OPEN = "<posterMethod>";
	public static final String POSTER_METHOD_CLOSE = "</posterMethod>";
	public static final String POSTER_METHOD_TYPE_OPEN = "<posterMethodType>";
	public static final String POSTER_METHOD_TYPE_CLOSE = "</posterMethodType>";
	public static final String POSTER_ARGTYPE_OPEN = "<posterArgType>";
	public static final String POSTER_ARGTYPE_CLOSE = "</posterArgType>";
	public static final String POSTER_DESCRIPTION_OPEN = "<posterDescription>";
	public static final String POSTER_DESCRIPTION_CLOSE = "</posterDescription>";
	public static final String POSTER_DESCRIPTION_NONE = "<posterDescription/>";
	public static final String POSTER_SHOWTYPE_OPEN = "<posterShowType>";
	public static final String POSTER_SHOWTYPE_CLOSE = "</posterShowType>";
	
	public static final String PUTTER_NAME_OPEN = "<putterName>";
	public static final String PUTTER_NAME_CLOSE = "</putterName>";
	public static final String PUTTER_METHOD_OPEN = "<putterMethod>";
	public static final String PUTTER_METHOD_CLOSE = "</putterMethod>";
	public static final String PUTTER_METHOD_TYPE_OPEN = "<putterMethodType>";
	public static final String PUTTER_METHOD_TYPE_CLOSE = "</putterMethodType>";
	public static final String PUTTER_ARGTYPE_OPEN = "<putterArgType>";
	public static final String PUTTER_ARGTYPE_CLOSE = "</putterArgType>";
	public static final String PUTTER_DESCRIPTION_OPEN = "<putterDescription>";
	public static final String PUTTER_DESCRIPTION_CLOSE = "</putterDescription>";
	public static final String PUTTER_DESCRIPTION_NONE = "<putterDescription/>";
	public static final String PUTTER_SHOWTYPE_OPEN = "<putterShowType>";
	public static final String PUTTER_SHOWTYPE_CLOSE = "</putterShowType>";
	
	public static final String DELETER_NAME_OPEN = "<deleterName>";
	public static final String DELETER_NAME_CLOSE = "</deleterName>";
	public static final String DELETER_METHOD_OPEN = "<deleterMethod>";
	public static final String DELETER_METHOD_CLOSE = "</deleterMethod>";
	public static final String DELETER_METHOD_TYPE_OPEN = "<deleterMethodType>";
	public static final String DELETER_METHOD_TYPE_CLOSE = "</deleterMethodType>";
	public static final String DELETER_DESCRIPTION_OPEN = "<deleterDescription>";
	public static final String DELETER_DESCRIPTION_CLOSE = "</deleterDescription>";
	public static final String DELETER_DESCRIPTION_NONE = "<deleterDescription/>";
	
	public static final String SECURITY_OPEN = "<security>";
	public static final String SECURITY_CLOSE = "</security>";
	public static final String SECURITY_INHERIT_OPEN = "<inheritUsers>";
	public static final String SECURITY_INHERIT_CLOSE = "</inheritUsers>";
	public static final String SECURITY_USER_LIST_OPEN = "<definedUsers>";
	public static final String SECURITY_USER_LIST_CLOSE = "</definedUsers>";
	public static final String SECURITY_INHERIT_USER_LIST_OPEN = "<inheritedUsers>";
	public static final String SECURITY_INHERIT_USER_LIST_CLOSE = "</inheritedUsers>";
	public static final String SECURITY_USER_OPEN = "<user>";
	public static final String SECURITY_USER_CLOSE = "</user>";
	public static final String SECURITY_USER_NAME_OPEN = "<name>";
	public static final String SECURITY_USER_NAME_CLOSE = "</name>";
	public static final String SECURITY_USER_PASSWORD_OPEN = "<password>";
	public static final String SECURITY_USER_PASSWORD_CLOSE = "</password>";
	public static final String SECURITY_USER_RESOURCE_OPEN = "<resource>";
	public static final String SECURITY_USER_RESOURCE_CLOSE = "</resource>";
	
	/**
	 * Transform a string to a valid Java class name
	 * @param input - The string to be transformed
	 * @return A valid Java class name
	 */
	public static String makeClassName(String input) {
		String output = input.replace(" ", "").replace("\t", " ").replace("{", "_").replace("}", "_");
		return output.substring(0, 1).toUpperCase() + output.substring(1);
	}
	
	/**
	 * Trim method names for internal use
	 * @param input - The method name to be trimmed
	 * @return The trimmed method name
	 */
	public static String trimMethod(String input) {
		if (input.contains("(")) {
			return input.substring(0, input.indexOf("("));
		}
		else return input;
	}
	
	public static String startOfXMLElement(String input) {
		return input.substring(0, input.length() - 1);
	}
}