package ch.ethz.inf.vs.wot.autowot.builders.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.ethz.inf.vs.wot.autowot.builders.utils.FileOperations;
import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.project.Project;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallback;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;
import ch.ethz.inf.vs.wot.autowot.project.resources.DeleterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.GetterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PosterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PutterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.security.UserLoginData;

/**
 * Builder class for a Restlet server
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class RestletJavaBuilder extends AbstractJavaBuilder {
	
	/**
	 * Constructor setting Restlet-specific folder and package names
	 */
	public RestletJavaBuilder(Project project) {
		super(project);
		inputFolderName = Constants.SOURCE_FOLDER + "javaresources" + System.getProperty("file.separator") + "restlet" + System.getProperty("file.separator");
	}

	/**
	 * Create the web server
	 */
	public void build() {
		// Delete Old Data
		FileOperations.deleteDir(outputBaseFolderName);
		// Create Directories
		createAndCopyResourceDirectories();
		// Copy Structure Configuration File
		copyStructureConfiguration();
		// Parse the Java source, simultaneously create the root application
		parseToJavaClasses();
		// Copy RestletConstants
		copyRestletConstants();
		// Copy BaseResource
		copyBaseResource();
		// Security Tools
		copyResourceProtector(); 
		// Copy RestletMain
		copyRestletMain();
		// Copy RESTSOperation
		copyRestsOperation();
		copyRestsService();
		// Copy Handler
		copyHandlerClass();
		// Create project files
		if(makeStandalone) {
			createProjectFiles();
		}
	}

	/**
	 * Create BaseResource file
	 */
	protected void copyBaseResource() {
		BufferedWriter myWriter = FileOperations.createFile(outputResourcesFolderName + "BaseResource.java");
		String restletMainTemplate;
		try {
			restletMainTemplate = FileOperations.readFileAsString(inputFolderName + "BaseResourceTemplate");
			restletMainTemplate = restletMainTemplate.replace("{{AuthorName}}", authorString);
			restletMainTemplate = restletMainTemplate.replace("{{MetaAuthorName}}", metaAuthorString);
			restletMainTemplate = restletMainTemplate.replace("{{PackageName}}", resourcesPackageName);
			restletMainTemplate = restletMainTemplate.replace("{{RestletCommonsPackage}}", commonsPackageName);
			myWriter.write(restletMainTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Create RestsOperation file
	 */
	protected void copyRestsOperation() {
		BufferedWriter myWriter = FileOperations.createFile(outputResourcesFolderName + "RESTSOperation.java");
		String restletMainTemplate;
		try {
			restletMainTemplate = FileOperations.readFileAsString(inputFolderName + "RESTSOperationTemplate");
			restletMainTemplate = restletMainTemplate.replace("{{AuthorName}}", authorString);
			restletMainTemplate = restletMainTemplate.replace("{{MetaAuthorName}}", metaAuthorString);
			restletMainTemplate = restletMainTemplate.replace("{{PackageName}}", resourcesPackageName);
			myWriter.write(restletMainTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Create RestsService file
	 */
	protected void copyRestsService() {
		BufferedWriter myWriter = FileOperations.createFile(outputResourcesFolderName + "RESTSService.java");
		String restletMainTemplate;
		try {
			restletMainTemplate = FileOperations.readFileAsString(inputFolderName + "RESTSServiceTemplate");
			restletMainTemplate = restletMainTemplate.replace("{{AuthorName}}", authorString);
			restletMainTemplate = restletMainTemplate.replace("{{MetaAuthorName}}", metaAuthorString);
			restletMainTemplate = restletMainTemplate.replace("{{PackageName}}", resourcesPackageName);
			myWriter.write(restletMainTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Create Restlet resource protector file
	 */
	private void copyResourceProtector() {
		BufferedWriter myWriter = FileOperations.createFile(outputSecurityFolderName + "ResourceProtector.java");
		String resourceProtectorTemplate;
		try {
			resourceProtectorTemplate = FileOperations.readFileAsString(inputFolderName + "ResourceProtectorTemplate");
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{AuthorName}}", authorString);
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{PackageName}}", securityPackageName);
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{RestletCommonsPackage}}", commonsPackageName);
			myWriter.write(resourceProtectorTemplate);			
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	List<String> runnablesTracker = new ArrayList<String>();

	/**
	 * Create Restlet main file
	 */
	protected void copyRestletMain() {
		BufferedWriter myWriter = FileOperations.createFile(outputResourcesFolderName + "RestletMain.java");
		String restletMainTemplate;
		try {
			restletMainTemplate = FileOperations.readFileAsString(inputFolderName + "RestletMainTemplate");
			restletMainTemplate = restletMainTemplate.replace("{{AuthorName}}", authorString);
			restletMainTemplate = restletMainTemplate.replace("{{MetaAuthorName}}", metaAuthorString);
			restletMainTemplate = restletMainTemplate.replace("{{PackageName}}", resourcesPackageName);
			restletMainTemplate = restletMainTemplate.replace("{{RestletCommonsPackage}}", commonsPackageName);
			if (this.makeStandalone) {
				restletMainTemplate = restletMainTemplate.replace("{{MakeStandalone}}", "");
			} else {
				restletMainTemplate = restletMainTemplate.replace("{{MakeStandalone}}", "//");
			}
			myWriter.write(restletMainTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Create Restlet constants file
	 */
	protected void copyRestletConstants() {
		File outFile = new File(inputFolderName + "RestletConstantsTemplate");
		File outFile2 = new File(outputCommonsFolderName + "RestletConstants.java");
		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outFile2));
			fileWriter.write("package " + commonsPackageName + ";");
			fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			FileOperations.copyFile(outFile, outFile2, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String rootApplicationAttachments;

	/**
	 * Starting point to create Jersey Java classes
	 */
	protected void parseToJavaClasses() {
		try {
			this.documentationWriter = new BufferedWriter(new FileWriter(outputBaseFolderName + "ReadMe.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		rootApplicationAttachments = "";
		// Parse the file and create Java classes, start with the RootResource
		parseRecursive(rootResource);
		// Create the Root Application
		BufferedWriter myWriter = FileOperations.createFile(outputResourcesFolderName + "RootApplication.java");
		String rootApplicationTemplate;
		try {
			rootApplicationTemplate = FileOperations.readFileAsString(inputFolderName + "RootApplicationTemplate");
			rootApplicationTemplate = rootApplicationTemplate.replace("{{ResourceAttachment}}", rootApplicationAttachments);
			rootApplicationTemplate = rootApplicationTemplate.replace("{{AuthorName}}", authorString);
			rootApplicationTemplate = rootApplicationTemplate.replace("{{MetaAuthorName}}", metaAuthorString);
			rootApplicationTemplate = rootApplicationTemplate.replace("{{PackageName}}", resourcesPackageName);
			rootApplicationTemplate = rootApplicationTemplate.replace("{{RestletSecurityPackage}}", securityPackageName);
			myWriter.write(rootApplicationTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			this.documentationWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recursive function for creating Jersey Java classes
	 */
	protected void parseRecursive(ResourceItem currentClass) {
		System.out.println("Writing " + currentClass.getClassName() + "...");
		
		// List of authorized users and their base64 hashes
		List<UserLoginData> userDataGet = null;
		List<UserLoginData> userDataPost = null;
		List<UserLoginData> userDataPut = null;
		List<UserLoginData> userDataDelete = null;
		List<String> userHashGet = new ArrayList<String> ();
		List<String> userHashPost = new ArrayList<String> ();
		List<String> userHashPut = new ArrayList<String> ();
		List<String> userHashDelete = new ArrayList<String> ();
		
		// Set lists for each restricted resource, Getter, Poster, Putter and Deleter
		if(currentClass.isRestricted() || (currentClass.hasGetter() && currentClass.getGetter().isRestricted()) || (currentClass.hasPoster() && currentClass.getPoster().isRestricted()) || (currentClass.hasPutter() && currentClass.getPutter().isRestricted()) || (currentClass.hasDeleter() && currentClass.getDeleter().isRestricted())) {
			if(currentClass.hasGetter()) {
				userDataGet = currentClass.getGetter().getAuthorizedUsers();
				userDataGet.addAll(currentClass.getGetter().getInheritedUsers());
				for(UserLoginData user : userDataGet) {
					userHashGet.add(user.getBase64Hash());
				}
			} else if (currentClass.isRestricted()) {
				userDataGet = currentClass.getAuthorizedUsers();
				userDataGet.addAll(currentClass.getInheritedUsers());
				for(UserLoginData user : userDataGet) {
					userHashGet.add(user.getBase64Hash());
				}
			}
			if(currentClass.hasPoster()) {
				userDataPost = currentClass.getPoster().getAuthorizedUsers();
				userDataPost.addAll(currentClass.getPoster().getInheritedUsers());
				for(UserLoginData user : userDataPost) {
					userHashPost.add(user.getBase64Hash());
				}
			}
			if(currentClass.hasPutter()) {
				userDataPut = currentClass.getPutter().getAuthorizedUsers();
				userDataPut.addAll(currentClass.getPutter().getInheritedUsers());
				for(UserLoginData user : userDataPut) {
					userHashPut.add(user.getBase64Hash());
				}
			}
			if(currentClass.hasDeleter()) {
				userDataDelete = currentClass.getDeleter().getAuthorizedUsers();
				userDataDelete.addAll(currentClass.getDeleter().getInheritedUsers());
				for(UserLoginData user : userDataDelete) {
					userHashDelete.add(user.getBase64Hash());
				}
			}
			// Set up resource protector with credentials computed above
			String buildUp = "router.attach(\"" + currentClass.getURI() + "\", new ResourceProtector(getContext(), ";
					if(userHashGet.isEmpty()){buildUp += "null, ";} else { buildUp += "new String[] "+ userHashGet.toString().replace("[", "{\"").replace(", ", "\", \"").replace("]", "\"}") + ", ";} 
					if(userHashPost.isEmpty()){buildUp += "null, ";} else { buildUp += "new String[] "+ userHashPost.toString().replace("[", "{\"").replace(", ", "\", \"").replace("]", "\"}") + ", ";}
					if(userHashPut.isEmpty()){buildUp += "null, ";} else { buildUp += "new String[] "+ userHashPut.toString().replace("[", "{\"").replace(", ", "\", \"").replace("]", "\"}") + ", ";}
					if(userHashDelete.isEmpty()){buildUp += "null, ";} else { buildUp += "new String[] "+ userHashDelete.toString().replace("[", "{\"").replace(", ", "\", \"").replace("]", "\"}") + ", ";}
					buildUp += currentClass.getClassName() + ".class));\n\t\t";
					rootApplicationAttachments += buildUp;
		} else {
			// Unrestricted resources can be added directy
			rootApplicationAttachments += "router.attach(\"" + currentClass.getURI() + "\", " + currentClass.getClassName() + ".class);\n\t\t";
		}
		
		// Write additional information about the current class
		try {
			documentationWriter.newLine();
			documentationWriter.newLine();
			documentationWriter.newLine();
			documentationWriter.write("/************* INFORMATION ON CLASS " + currentClass + " ****************/");
			documentationWriter.newLine();
			documentationWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Parse the resource
		List<ResourceItem> toBeParsed = writeSingleSource(currentClass);

		System.out.println(currentClass + " parsed successfully");
		System.out.println();
		System.out.println();

		if (toBeParsed != null) {
			for (ResourceItem nextClass : toBeParsed) {
				parseRecursive(nextClass);
			}
		}
	}

	/**
	 * Create Jersey Java class
	 */
	protected List<ResourceItem> writeSingleSource(ResourceItem currentClass) {
		try {
			FileOperations.copyFile(new File(Constants.SOURCE_FOLDER + "webresources" + System.getProperty("file.separator") + "html" + System.getProperty("file.separator") + "ResourceHTML"), new File(outputWebresourcesFolderName + "html" + System.getProperty("file.separator") + currentClass.getClassName() + ".html" ), false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedWriter myWriter = FileOperations.createFile(outputResourcesFolderName + System.getProperty("file.separator") + currentClass.getClassName() + ".java");

		try {
			String resourceTemplate = FileOperations.readFileAsString(inputFolderName + "ResourceTemplate");

			// Replace package declaration
			resourceTemplate = resourceTemplate.replace("{{PackageName}}", resourcesPackageName);

			// Replace author and meta author
			resourceTemplate = resourceTemplate.replace("{{AuthorName}}", authorString);
			resourceTemplate = resourceTemplate.replace("{{MetaAuthorName}}", metaAuthorString);

			// Replace resource and class names
			resourceTemplate = resourceTemplate.replace("{{ResourceName}}", currentClass.getResourceName());
			resourceTemplate = resourceTemplate.replace("{{ClassName}}", currentClass.getClassName());

			boolean allowGet = true;
			boolean allowPost = false;
			boolean allowPut = false;
			boolean allowDelete = false;
			
			// Add children callback functions to list for creating Handler stubs
			Map<String, ResourceItem> children = currentClass.getChildren();
			if (children != null) {
				for (String childName : children.keySet()) {
					ResourceItem child = children.get(childName);
					if (child.isCollection()) {
						handlerCallbacks.add(new HandlerCallback(child.getCollectionMethod(), child.getCollectionMethodType(), "COLLECT"));
					}
				}
			}
			
			// Build and replace the GetterMethods
			String getterMethodString = "";
			if (currentClass.hasGetter()) {
				// Get information about the Getter
				GetterItem getter = currentClass.getGetter();
				String getterName = getter.getResourceName();
				String methodName = getter.getCallbackMethod();
				HandlerCallbackType methodType = getter.getCallbackMethodType();
				String onChangeMethodName = getter.getOnChangeMethod();
				//HandlerCallbackType onChangeMethodType = getter.getOnChangeMethodType();

				// Handle OnChange Getters
				if (onChangeMethodName != null) {
					// Create new GetterRunnable
					String getterRunnableTemplate = FileOperations.readFileAsString(inputFolderName + "GetterRunnableTemplate");

					// Replace Package Declaration
					getterRunnableTemplate = getterRunnableTemplate.replace("{{PackageName}}", resourcesPackageName);

					// Replace Author and MetaAuthor
					getterRunnableTemplate = getterRunnableTemplate.replace("{{AuthorName}}", authorString);
					getterRunnableTemplate = getterRunnableTemplate.replace("{{MetaAuthorName}}", metaAuthorString);

					// Replace Resource and Class Names
					getterRunnableTemplate = getterRunnableTemplate.replace("{{ClassName}}", "Runnable_" + getterName);

					// Replace GetterMethodName and OnChangeMethodName
					getterRunnableTemplate = getterRunnableTemplate.replace("{{GetterMethodName}}", methodName);
					getterRunnableTemplate = getterRunnableTemplate.replace("{{OnChangeMethodName}}", onChangeMethodName);

					// Remember to start new GetterRunnable
					runnablesTracker.add("Runnable_" + getterName);

					BufferedWriter myWriter2 = FileOperations.createFile(outputResourcesFolderName + "/" + "Runnable_" + getterName + ".java");
					myWriter2.write(getterRunnableTemplate);
					myWriter2.close();
				}
				while (methodName.contains(".")) methodName = methodName.substring(methodName.indexOf(".") + 1);
				
				// Method to be called for getting the Getter value 
				getterMethodString += "getterValue = " + this.handlerClassCanonicalName + "." + methodName + "(getSource()); // " + getter.getDescription() + "\n";

				// Add callback to list for creating Handler stubs
				handlerCallbacks.add(new HandlerCallback(methodName, methodType, "GET"));
			}
			resourceTemplate = resourceTemplate.replace("{{GetterMethods}}", getterMethodString);

			// Build and replace the PosterMethods
			if (currentClass.hasPoster()) {
				String posterMethodString = "";
				allowPost = true;
				
				// Get information about the Poster
				PosterItem poster = currentClass.getPoster();
				String posterArgType = poster.getPosterArgumentType();
				String methodName = poster.getCallbackMethod();
				HandlerCallbackType methodType = poster.getCallbackMethodType();
				
				// Build strings for the Poster
				while (methodName.contains(".")) methodName = methodName.substring(methodName.indexOf(".") + 1);

				if (poster.getDescription() == null) posterMethodString += "// No Poster Description Given\n";
				else posterMethodString += "// " + poster.getDescription() + "\n";

				if (posterArgType.equalsIgnoreCase("Integer")) posterMethodString += indent(2) + "Integer posterVar = null;\n";
				else if (posterArgType.equalsIgnoreCase("Double")) posterMethodString += indent(2) + "Double posterVar = null;\n";
				else posterMethodString += indent(2) + "String posterVar = null;\n";

				// Incoming form encoded variable
				posterMethodString += indent(2) + "if (entity.getMediaType().equals(MediaType.APPLICATION_WWW_FORM)) {\n";
				posterMethodString += indent(3) + "try { posterVar = ";

				if (posterArgType.equalsIgnoreCase("Integer"))
					posterMethodString += "new Integer";
				else if (posterArgType.equalsIgnoreCase("Double"))
					posterMethodString += "new Double";
				else if (posterArgType.equalsIgnoreCase("String"))
					posterMethodString += "";

				posterMethodString += "(entity.getText().split(\"=\")[1]); }\n";

				posterMethodString += indent(3) + "catch (Exception e) { e.printStackTrace(); }\n";
				posterMethodString += indent(2) + "} else if (entity.getMediaType().equals(MediaType.TEXT_PLAIN)) {\n";
				posterMethodString += indent(3) + "try { posterVar = ";

				if (posterArgType.equalsIgnoreCase("Integer"))
					posterMethodString += "new Integer";
				else if (posterArgType.equalsIgnoreCase("Double"))
					posterMethodString += "new Double";
				else if (posterArgType.equalsIgnoreCase("String"))
					posterMethodString += "";

				posterMethodString += "(entity.getText()); }\n";

				posterMethodString += indent(3) + "catch (Exception e) { e.printStackTrace(); }\n";
				posterMethodString += indent(2) + "}\n\n";

				if (posterArgType.equalsIgnoreCase("None"))
					posterMethodString += indent(2) + this.handlerClassCanonicalName + "." + methodName + "(getSource(), null);\n";
				else
					posterMethodString += indent(2) + this.handlerClassCanonicalName + "." + methodName + "(getSource(), posterVar);\n";

				// Add callback to list for creating Handler stubs
				handlerCallbacks.add(new HandlerCallback(methodName, methodType, "POST"));

				resourceTemplate = resourceTemplate.replace("{{PosterMethods}}", posterMethodString);
			} else {
				resourceTemplate = resourceTemplate.replace("{{PosterMethods}}", "");
			}
			
			// Build and replace the PutterMethods
			if (currentClass.hasPutter()) {
				String putterMethodString = "";
				allowPut = true;
				
				// Get information about the Putter
				PutterItem putter = currentClass.getPutter();
				String methodName = putter.getCallbackMethod();
				HandlerCallbackType methodType = putter.getCallbackMethodType();
				
				// Build strings for the Putter
				while (methodName.contains("."))
					methodName = methodName.substring(methodName.indexOf(".") + 1);

				if (putter.getDescription() == null)
					putterMethodString += "// No Putter Description Given\n";
				else
					putterMethodString += "// " + putter.getDescription() + "\n";
				
				putterMethodString += indent(2) + "try {\n";
				putterMethodString += indent(3) + this.handlerClassCanonicalName + "." + methodName + "(getSource(), getRequest().getEntity().getText());\n";
				putterMethodString += indent(2) + "} catch (IOException e) { e.printStackTrace(); }";

				// Add callback to list for creating Handler stubs
				handlerCallbacks.add(new HandlerCallback(methodName, methodType, "PUT"));
				
				resourceTemplate = resourceTemplate.replace("{{PutterMethods}}", putterMethodString);
			} else {
				resourceTemplate = resourceTemplate.replace("{{PutterMethods}}", "");
			}
			
			// Build and replace the DeleterMethods
			if (currentClass.hasDeleter()) {
				String deleterMethodString = "";
				allowDelete = true;
				
				// Get information about the Deleter
				DeleterItem deleter = currentClass.getDeleter();
				String methodName = deleter.getCallbackMethod();
				HandlerCallbackType methodType = deleter.getCallbackMethodType();
				
				// Build strings for the Deleter
				while (methodName.contains("."))
					methodName = methodName.substring(methodName.indexOf(".") + 1);

				if (deleter.getDescription() == null)
					deleterMethodString += "// No Deleter Description Given\n";
				else
					deleterMethodString += "// " + deleter.getDescription() + "\n";

				deleterMethodString += indent(2) + this.handlerClassCanonicalName + "." + methodName + "(getSource());\n";

				// Add callback to list for creating Handler stubs
				handlerCallbacks.add(new HandlerCallback(methodName, methodType, "DELETE"));

				resourceTemplate = resourceTemplate.replace("{{DeleterMethods}}", deleterMethodString);
			} else {
				resourceTemplate = resourceTemplate.replace("{{DeleterMethods}}", "");
			}

			// Replace allows
			resourceTemplate = resourceTemplate.replace("{{AllowGet}}", (allowGet ? "allowedMethods.add(org.restlet.data.Method.GET);" : "//allowedMethods.add(org.restlet.data.Method.GET);"));
			resourceTemplate = resourceTemplate.replace("{{AllowPost}}", (allowPost ? "allowedMethods.add(org.restlet.data.Method.POST);" : "//allowedMethods.add(org.restlet.data.Method.POST);"));
			resourceTemplate = resourceTemplate.replace("{{AllowPut}}", (allowPut ? "allowedMethods.add(org.restlet.data.Method.PUT);" : "//allowedMethods.add(org.restlet.data.Method.PUT);"));
			resourceTemplate = resourceTemplate.replace("{{AllowDelete}}", (allowDelete ? "allowedMethods.add(org.restlet.data.Method.DELETE);" : "//allowedMethods.add(org.restlet.data.Method.DELETE);"));

			myWriter.write(resourceTemplate);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get child classes and return them
		List<ResourceItem> children = new ArrayList<ResourceItem>();
		for (ResourceItem item : currentClass.getNonDynamicChildren().values()) {
			children.add(item);
		}
		for (ResourceItem item : currentClass.getDynamicChildren().values()) {
			children.add(item);
		}

		return children;
	}
	
	/**
	 * Create Restlet project files for Eclipse IDE
	 */
	protected void createProjectFiles() {
		BufferedWriter myWriter = FileOperations.createFile(outputBaseFolderName + ".classpath");
		String classpath = 	"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
							"<classpath> \n" +
							"<classpathentry kind=\"src\" path=\"src\"/> \n" +
							"<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.6\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/json/json.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/restlet/org.restlet.ext.xml.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/restlet/org.restlet.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/restlet/org.simpleframework.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/apache/commons-codec-1.5.jar\"/> \n" +
							"<classpathentry kind=\"output\" path=\"bin\"/> \n" + 
							"</classpath>";
		try {
			myWriter.write(classpath);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		myWriter = FileOperations.createFile(outputBaseFolderName + ".project");
		String project = 	"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
							"<projectDescription> \n" +
							"<name>GeneratedWebServer</name> \n" +
							"<comment></comment> \n" +
							"<projects> \n" +
							"</projects> \n" +
							"<buildSpec> \n" +
							"<buildCommand> \n" +
							"<name>org.eclipse.jdt.core.javabuilder</name> \n" +
							"<arguments> \n" +
							"</arguments> \n" +
							"</buildCommand> \n" +
							"</buildSpec> \n" +
							"<natures> \n" +
							"<nature>org.eclipse.jdt.core.javanature</nature> \n" +
							"</natures> \n" +
							"</projectDescription> \n";
		try {
			myWriter.write(project);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
