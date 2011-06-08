package ch.ethz.inf.vs.wot.autowot.commons;

/**
 * Tool tips
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class ToolTips {

	public static final String CREATE_JAVA_BUTTON_TEXT = "Write Java Code!";
	public static final String CREATE_STRUCTURE_BUTTON_TEXT = "Create XML Structure File!";
	public static final String INSTRUCTIONS_BUTTON_TEXT = "Show Instructions!";

	public static final String GET_CHILD_DATA_TOOLTIP = "Create a new Child.";
	public static final String GET_CHILD_DATA_RESOURCE_NAME_TOOLTIP = "Enter the name of the Child to be created.";
	public static final String GET_CHILD_DATA_RESOURCE_URI_TOOLTIP = "Enter the relative URI of the Child to be created.";
	public static final String GET_CHILD_DATA_RESOURCE_TYPE_TOOLTIP = "Specify the type of the Child to be created.\n  Auto = Single Child\n  Collection = Collection of Children retrieved at runtime";
	public static final String GET_CHILD_COLLECTION_METHOD_TOOLTIP = "If the Child is of type \"Collection\", specify the Method (+ Classname) to retrieve its instances at runtime. This method will have to be implemented!";
	
	public static final String GET_GETTER_DATA_TOOLTIP = "Create a new Getter.";
	public static final String GET_GETTER_DATA_RESOURCE_NAME_TOOLTIP = "Enter the name of the Getter to be created.";
	public static final String GET_GETTER_COLLECTION_METHOD_TOOLTIP = "Specify the name of the method to be called for acquiring the Getter's value. This method will have to be implemented!";

	public static final String GET_POSTER_DATA_TOOLTIP = "Create a new Poster.";
	public static final String GET_POSTER_DATA_RESOURCE_NAME_TOOLTIP = "Enter the name of the Poster to be created.";
	public static final String GET_POSTER_DATA_RESOURCE_METHOD_TOOLTIP = "Specify the name of the method to be called. This method will have to be implemented!";
	public static final String GET_POSTER_DATA_RESOURCE_ARG_TYPE_TOOLTIP = "Specify the Type of the Argument [Integer, Double, String].";
	public static final String GET_POSTER_COLLECTION_SHOW_TYPE_TOOLTIP = "Specify in which way the Poster is to be presented to the user [Binary, Text].";
	
	public static final String GET_PUTTER_DATA_TOOLTIP = "Create a new Putter.";
	public static final String GET_PUTTER_DATA_RESOURCE_NAME_TOOLTIP = "Enter the name of the Putter to be created.";
	public static final String GET_PUTTER_DATA_RESOURCE_METHOD_TOOLTIP = "Specify the name of the method to be called. This method will have to be implemented!";
	public static final String GET_PUTTER_DATA_RESOURCE_ARG_TYPE_TOOLTIP = "Specify the Type of the Argument [Integer, Double, String].";
	public static final String GET_PUTTER_COLLECTION_SHOW_TYPE_TOOLTIP = "Specify in which way the Putter is to be presented to the user [Binary, Text].";
	
	public static final String GET_DELETER_DATA_TOOLTIP = "Create a new Deleter.";
	public static final String GET_DELETER_DATA_RESOURCE_NAME_TOOLTIP = "Enter the name of the Deleter to be created.";
	public static final String GET_DELETER_METHOD_TOOLTIP = "Specify the name of the method to be called. This method will have to be implemented!";

	public static final String GET_JAVA_CREATION_DATA_TOOLTIP = "Create the Java Classes.";
	public static final String GET_JAVA_CREATION_DATA_PROTOTYPES_PACKAGE = "Specify the name of the Java package the AutoWOTized Prototypes are to be contained in.";
	public static final String GET_JAVA_CREATION_DATA_MANAGEMENT_PACKAGE = "Specify the name of the Java package the callback classes are to be contained in.";
	
	public static final String GET_OSGI_CREATION_DATA_TOOLTIP = "Create the OSGi Bundles corresponding to the defined Resource Structure.";
	public static final String GET_OSGI_CREATION_DATA_DRIVERBUNDLENAME_TOOLTIP = "Specify the OGSi bundle name of the bundle implementing the device driver.";
	public static final String GET_OSGI_CREATION_DATA_HANDLERNAME_TOOLTOP = "Specify the Java package and class name of the handler-class that contains the callback-methods.";
	public static final String GET_OSGI_CREATION_DATA_INTERFACES_TOOLTIP = "Specify the Java package name for the web-server access interface.";
	public static final String GET_OSGI_CREATION_DATA_IMPLEMENTATIONS_TOOLTIP = "Specify the Java package name for the web-server access implementation.";
	public static final String GET_OSGI_CREATION_DATA_RESTLETPACKAGENAME_TOOLTIP = "Specify the Java package name for the base package of the web-server component.";
	
	public static final String USER_MANUAL_TEXT = "\n\nUse the AutoWOT Prototyper for automatic prototyping of new Things " +
				"for the WOT and for run-time changes to the\n" +
				"resource structure of a specific Thing.\n" +
				"The prototyper automagically generates proper XML structure files and Java Classes that implement the\n" +
				"Restlet-Webinterface of a Thing on the WOT.\n\n" +
				"In order to create a new thing, start by dragging the \"RootResource\"-Icon into the buildup-area on the left.\n" +
				"Add Children (i.e. subresources), Getters (i.e. returned values of the current resource), Posters, Putters and Deleters to the\n" +
				"structure until it reflects your desired Resource. Also add Descriptions to your Resources and don't forget to define\n" +
				"the various Callback-Methods that are necessary to interact with the low-level, non-web-enabled part of your driver.\n\n" +
				"Click \"" + CREATE_JAVA_BUTTON_TEXT + "\" to have Java classes created that implement the Restlet interface for your new Thing. The Java\n" +
				"classes will contain references to functions in your WOT-Driver that you now have to implement. In order to support you\n" +
				"with this task, a \"ReadMe\" file is generated that summarizes all steps you have to take in order to get the new Thing\n" +
				"out on the Web.\n\n" +
				"In order to change/redesign an existing Resource, simply drop its XML structure definition file into the buildup-area of\n" +
				"the AutoWOT Prototyper. The Prototyper will create an internal mapping of your resource and let you modify it using its\n" +
				"drag/drop interface.\n\n" +
				"Please do always remember that this is an ALPHA version of the AutoWOT Prototyper!\n\n\n";
}