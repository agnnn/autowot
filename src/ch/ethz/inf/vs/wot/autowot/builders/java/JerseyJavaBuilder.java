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
 * Builder class for a Jersey server
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */


public class JerseyJavaBuilder extends AbstractJavaBuilder {
	
	/**
	 * Constructor setting Jersey-specific folder and package names
	 */
	public JerseyJavaBuilder(Project project) {
		super(project);
		inputFolderName = Constants.SOURCE_FOLDER + "javaresources" + System.getProperty("file.separator") + "jersey" + System.getProperty("file.separator");
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
		// Copy BaseResource
		copyBaseResource();
		// Copy JerseyMain
		copyJerseyMain();
		// Copy JerseyConstants
		copyJerseyConstants();
		// Copy Resource Manager
		copyResourceManager();
		// SecurityFiles
		copyResourceProtector();
		copySecurityExceptions();
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
		String jerseyMainTemplate;
		try {
			jerseyMainTemplate = FileOperations.readFileAsString(inputFolderName + "BaseResourceTemplate");
			jerseyMainTemplate = jerseyMainTemplate.replace("{{AuthorName}}", authorString);
			jerseyMainTemplate = jerseyMainTemplate.replace("{{MetaAuthorName}}", metaAuthorString);
			jerseyMainTemplate = jerseyMainTemplate.replace("{{PackageName}}", resourcesPackageName);
			jerseyMainTemplate = jerseyMainTemplate.replace("{{JerseyCommonsPackage}}", commonsPackageName);
			myWriter.write(jerseyMainTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Create Jersey main file
	 */
	protected void copyJerseyMain() {
		BufferedWriter myWriter = FileOperations.createFile(outputSourceFolderName + "JerseyMain.java");
		String jerseyMainTemplate;
		try {
			jerseyMainTemplate = FileOperations.readFileAsString(inputFolderName + "JerseyMainTemplate");
			jerseyMainTemplate = jerseyMainTemplate.replace("{{AuthorName}}", authorString);
			jerseyMainTemplate = jerseyMainTemplate.replace("{{MetaAuthorName}}", metaAuthorString);
			jerseyMainTemplate = jerseyMainTemplate.replace("{{PackageName}}", basePackageName);
			jerseyMainTemplate = jerseyMainTemplate.replace("{{ResourcesPackageName}}", resourcesPackageName);
			jerseyMainTemplate = jerseyMainTemplate.replace("{{JerseyCommonsPackage}}", commonsPackageName);
			if (this.makeStandalone) {
				jerseyMainTemplate = jerseyMainTemplate.replace("{{MakeStandalone}}", "");
			} else {
				jerseyMainTemplate = jerseyMainTemplate.replace("{{MakeStandalone}}", "//");
			}
			myWriter.write(jerseyMainTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Create Jersey constants file
	 */
	protected void copyJerseyConstants() {
		File outFile = new File(inputFolderName + "JerseyConstantsTemplate");
		File outFile2 = new File(outputCommonsFolderName + "JerseyConstants.java");
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
	
	/**
	 * Create Jersey resource manager file
	 */
	protected void copyResourceManager() {
		BufferedWriter myWriter = FileOperations.createFile(outputResourcesFolderName + "ResourceManager.java");
		String resourceManagerTemplate;
		try {
			resourceManagerTemplate = FileOperations.readFileAsString(inputFolderName + "ResourceManagerTemplate");
			resourceManagerTemplate = resourceManagerTemplate.replace("{{AuthorName}}", authorString);
			resourceManagerTemplate = resourceManagerTemplate.replace("{{MetaAuthorName}}", metaAuthorString);
			resourceManagerTemplate = resourceManagerTemplate.replace("{{PackageName}}", resourcesPackageName);
			resourceManagerTemplate = resourceManagerTemplate.replace("{{WebresourcesPath}}", "/webresources");
			myWriter.write(resourceManagerTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Create Jersey resource protector file
	 */
	protected void copyResourceProtector() {
		BufferedWriter myWriter = FileOperations.createFile(outputSecurityFolderName + "ResourceProtector.java");
		String resourceProtectorTemplate;
		try {
			resourceProtectorTemplate = FileOperations.readFileAsString(inputFolderName + "ResourceProtectorTemplate");
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{AuthorName}}", authorString);
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{PackageName}}", securityPackageName);
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{JerseyCommonsPackage}}", commonsPackageName);
			myWriter.write(resourceProtectorTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Create Jersey security exception file
	 */
	protected void copySecurityExceptions() {
		BufferedWriter myWriter = FileOperations.createFile(outputSecurityFolderName + "UnauthorizedException.java");
		String resourceProtectorTemplate;
		try {
			resourceProtectorTemplate = FileOperations.readFileAsString(inputFolderName + "UnauthorizedExceptionTemplate");
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{AuthorName}}", authorString);
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{PackageName}}", securityPackageName);
			resourceProtectorTemplate = resourceProtectorTemplate.replace("{{JerseyCommonsPackage}}", commonsPackageName);
			myWriter.write(resourceProtectorTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Starting point to create Jersey Java classes
	 */
	protected void parseToJavaClasses() {
		try {
			this.documentationWriter = new BufferedWriter(new FileWriter(outputBaseFolderName + "ReadMe.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// Parse the file and create Java classes, start with the RootResource
		parseRecursive(rootResource);
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

	protected List<String> runnablesTracker = new ArrayList<String>();
	
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
			resourceTemplate = resourceTemplate.replace("{{JerseySecurityPackage}}", securityPackageName);
			
			// Replace author and meta author
			resourceTemplate = resourceTemplate.replace("{{AuthorName}}", authorString);
			resourceTemplate = resourceTemplate.replace("{{MetaAuthorName}}", metaAuthorString);

			// Replace resource and class names
			resourceTemplate = resourceTemplate.replace("{{ResourceName}}", currentClass.getResourceName());
			resourceTemplate = resourceTemplate.replace("{{ClassName}}", currentClass.getClassName());
			
			// Replace resource information
			resourceTemplate = resourceTemplate.replace("{{ResourceURI}}", currentClass.getURI());
			
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
					String getterRunnableTemplate = FileOperations.readFileAsString(Constants.SOURCE_FOLDER + "javaresources " + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "GetterRunnableTemplate");

					// Replace Package Declaration
					getterRunnableTemplate = getterRunnableTemplate.replace("{{PackageName}}", resourcesPackageName);

					// Replace Author and MetaAuthor
					getterRunnableTemplate = getterRunnableTemplate.replace("{{AuthorName}}", this.authorString);
					getterRunnableTemplate = getterRunnableTemplate.replace("{{MetaAuthorName}}", this.metaAuthorString);

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
			resourceTemplate = resourceTemplate.replace("{{GetterMethod}}", getterMethodString);
			resourceTemplate = resourceTemplate.replace("{{setAllowGet}}", "allowGet = true;");

			// Build and replace the PosterMethods
			if (currentClass.hasPoster()) {
				// Get information about the Poster
				PosterItem poster = currentClass.getPoster();
				String posterArgType = poster.getPosterArgumentType();
				String methodName = poster.getCallbackMethod();
				HandlerCallbackType methodType = poster.getCallbackMethodType();
				
				// Build strings for the Poster
				String posterMethodString = "";
				while (methodName.contains(".")) methodName = methodName.substring(methodName.indexOf(".") + 1);
				
				if (poster.getDescription() == null) posterMethodString += "// No Poster Description Given\n";
				else posterMethodString += "// " + poster.getDescription() + "\n"; 
				
				if (posterArgType.equalsIgnoreCase("None")) posterMethodString += indent(2) + this.handlerClassCanonicalName + "." + methodName + "(getSource(), null);\n";
				else posterMethodString += indent(2) + this.handlerClassCanonicalName + "." + methodName + "(getSource(), posterVar);\n";

				// Add callback to list for creating Handler stubs
				handlerCallbacks.add(new HandlerCallback(methodName, methodType, "POST"));
				
				// Insert the function handling the POST request
				resourceTemplate = resourceTemplate.replace("{{PosterMethodDeclarations}}",
						"@POST \n" +
						indent(1) + "@Produces(\"text/html\") \n" +
						indent(1) + "public String handlePostHTML(@Context HttpServletRequest request, @Context UriInfo uri, @FormParam(\"value\") String posterVar) { \n" +
						indent(1) + "	{{ResourceProtectorPost}}\n" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{PosterMethod}} \n" +
						indent(1) + "	return getRepresentationHTML();\n" +
						indent(1) + "} \n" +
						indent(1) + "\n" +
						indent(1) + "@POST \n" +
						indent(1) + "@Produces(\"application/json\") \n" +
						indent(1) + "public String handlePostJSON(@Context HttpServletRequest request, @Context UriInfo uri, @FormParam(\"value\") String posterVar) { \n" +
						indent(1) + "	{{ResourceProtectorPost}}" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{PosterMethod}} \n" +
						indent(1) + "	return getRepresentationJSON();\n" +
						indent(1) + "} \n" +
						indent(1) + "\n" +
						indent(1) + "@POST \n" +
						indent(1) + "@Produces(\"text/xml\") \n" +
						indent(1) + "public String handlePostXML(@Context HttpServletRequest request, @Context UriInfo uri, @FormParam(\"value\") String posterVar) { \n" +
						indent(1) + "	{{ResourceProtectorPost}}" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{PosterMethod}} \n" +
						indent(1) + "	return getRepresentationXML();\n" +
						indent(1) + "} \n" +
						indent(1) + "\n" +
						indent(1) + "\n");
			
				resourceTemplate = resourceTemplate.replace("{{PosterMethod}}", posterMethodString);
				resourceTemplate = resourceTemplate.replace("{{setAllowPost}}", "allowPost = true;");
			} else {
				resourceTemplate = resourceTemplate.replace("{{PosterMethodDeclarations}}", "");
				resourceTemplate = resourceTemplate.replace("{{setAllowPost}}", "allowPost = false;");
			}

			// Build and replace the PutterMethods
			if (currentClass.hasPutter()) {
				// Get information about the Putter
				PutterItem putter = currentClass.getPutter();
				String methodName = putter.getCallbackMethod();
				HandlerCallbackType methodType = putter.getCallbackMethodType();
				
				// Build strings for the Putter
				String putterMethodString = "";
				while (methodName.contains(".")) methodName = methodName.substring(methodName.indexOf(".") + 1);

				if (putter.getDescription() == null) putterMethodString += "// No Putter Description Given\n";
				else putterMethodString += "// " + putter.getDescription() + "\n"; 
					
				putterMethodString += indent(2) + this.handlerClassCanonicalName + "." + methodName + "(getSource(), content);\n";

				// Add callback to list for creating Handler stubs
				handlerCallbacks.add(new HandlerCallback(methodName, methodType, "PUT"));
				
				// Insert the function handling the PUT request
				resourceTemplate = resourceTemplate.replace("{{PutterMethodDeclarations}}",
						"@PUT \n" +
						indent(1) + "@Produces(\"text/html\") \n" +
						indent(1) + "public String handlePutHTML(String content, @Context HttpServletRequest request, @Context UriInfo uri) { \n" +
						indent(1) + "	{{ResourceProtectorPut}}" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{PutterMethod}} \n" +
						indent(1) + "	return getRepresentationHTML();\n" +
						indent(1) + "} \n" +
						indent(1) + "\n" +
						indent(1) + "@PUT \n" +
						indent(1) + "@Produces(\"application/json\") \n" +
						indent(1) + "public String handlePutJSON(String content, @Context HttpServletRequest request, @Context UriInfo uri) { \n" +
						indent(1) + "	{{ResourceProtectorPut}}" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{PutterMethod}} \n" +
						indent(1) + "	return getRepresentationJSON();\n" +
						indent(1) + "} \n" +
						indent(1) + "\n" +
						indent(1) + "@PUT \n" +
						indent(1) + "@Produces(\"text/xml\") \n" +
						indent(1) + "public String handlePutXML(String content, @Context HttpServletRequest request, @Context UriInfo uri) { \n" +
						indent(1) + "	{{ResourceProtectorPut}}" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{PutterMethod}} \n" +
						indent(1) + "	return getRepresentationXML();\n" +
						indent(1) + "} \n" +
						indent(1) + "\n" +
						indent(1) + "\n");
				
				resourceTemplate = resourceTemplate.replace("{{PutterMethod}}", putterMethodString);
				resourceTemplate = resourceTemplate.replace("{{setAllowPut}}", "allowPut = true;");
			} else {
				resourceTemplate = resourceTemplate.replace("{{PutterMethodDeclarations}}", "");
				resourceTemplate = resourceTemplate.replace("{{setAllowPut}}", "allowPut = false;");
			}
			
			// Build and replace the DeleterMethods
			if (currentClass.hasDeleter()) {
				// Get information about the Deleter
				DeleterItem deleter = currentClass.getDeleter();
				String methodName = deleter.getCallbackMethod();
				HandlerCallbackType methodType = deleter.getCallbackMethodType();
				
				// Build strings for the Deleter
				String deleterMethodString = "";
				while (methodName.contains(".")) methodName = methodName.substring(methodName.indexOf(".") + 1);

				if (deleter.getDescription() == null) deleterMethodString += "// No Deleter Description Given\n";
				else deleterMethodString += "// " + deleter.getDescription() + "\n"; 

				deleterMethodString += indent(2) + this.handlerClassCanonicalName + "." + methodName + "(getSource());\n";

				// Add callback to list for creating Handler stubs
				handlerCallbacks.add(new HandlerCallback(methodName, methodType, "DELETE"));
				
				// Insert the function handling the DELETE request
				resourceTemplate = resourceTemplate.replace("{{DeleterMethodDeclarations}}",
						"@DELETE \n" +
						indent(1) + "@Produces(\"text/html\") \n" +
						indent(1) + "public String handleDeleteHTML(@Context HttpServletRequest request, @Context UriInfo uri) { \n" +
						indent(1) + "	{{ResourceProtectorDelete}}" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{DeleterMethod}} \n" +
						indent(1) + "	return getRepresentationHTML();\n" +
						indent(1) + "} \n" +
						indent(1) + "\n" +
						indent(1) + "@DELETE \n" +
						indent(1) + "@Produces(\"application/json\") \n" +
						indent(1) + "public String handleDeleteJSON(@Context HttpServletRequest request, @Context UriInfo uri) { \n" +
						indent(1) + "	{{ResourceProtectorDelete}}" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{DeleterMethod}} \n" +
						indent(1) + "	return getRepresentationJSON();\n" +
						indent(1) + "} \n" +
						indent(1) + "@DELETE \n" +
						indent(1) + "@Produces(\"text/xml\") \n" +
						indent(1) + "public String handleDeleteXML(@Context HttpServletRequest request, @Context UriInfo uri) { \n" +
						indent(1) + "	{{ResourceProtectorDelete}}" +
						indent(1) + "	parseResourceInformation(request, uri);\n" +
						indent(1) + "	{{DeleterMethod}} \n" +
						indent(1) + "	return getRepresentationXML();\n" +
						indent(1) + "} \n" +
						indent(1) + "\n" +
						indent(1) + "\n");
				
				resourceTemplate = resourceTemplate.replace("{{DeleterMethod}}", deleterMethodString);
				resourceTemplate = resourceTemplate.replace("{{setAllowDelete}}", "allowDelete = true;");
			} else {
				resourceTemplate = resourceTemplate.replace("{{DeleterMethodDeclarations}}", "");	
				resourceTemplate = resourceTemplate.replace("{{setAllowDelete}}", "allowDelete = false;");
			}
			
			// Add user base64 hashes for users having access to the getter or the resource
			if(currentClass.hasGetter() && currentClass.getGetter().isRestricted()) {
				String hashList = "{";
				List<UserLoginData> users = currentClass.getGetter().getAuthorizedUsers();
				users.addAll(currentClass.getGetter().getInheritedUsers());
				for(int i = 0; i<users.size()-1; ++i) {
					hashList += "\"" + users.get(i).getBase64Hash() + "\"" + ", ";
				}
				hashList += "\"" + users.get(users.size()-1).getBase64Hash() + "\"";
				hashList += "}";
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesGet}}", hashList);
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorGet}}", "ResourceProtector.protect(request, authorizedHashesGet);");
			} else if(currentClass.isRestricted()) {
				String hashList = "{";
				List<UserLoginData> users = currentClass.getAuthorizedUsers();
				users.addAll(currentClass.getInheritedUsers());
				for(int i = 0; i<users.size()-1; ++i) {
					hashList += "\"" + users.get(i).getBase64Hash() + "\"" + ", ";
				}
				hashList += "\"" + users.get(users.size()-1).getBase64Hash() + "\"";
				hashList += "}";
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesGet}}", hashList);
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorGet}}", "ResourceProtector.protect(request, authorizedHashesGet);");
			} else {
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesGet}}", "null");
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorGet}}", "");
			}
			
			// Add user base64 hashes for users having access to the poster
			if(currentClass.hasPoster() && currentClass.getPoster().isRestricted()) {
				String hashList = "{";
				List<UserLoginData> users = currentClass.getPoster().getAuthorizedUsers();
				users.addAll(currentClass.getPoster().getInheritedUsers());
				for(int i = 0; i<users.size()-1; ++i) {
					hashList += "\"" + users.get(i).getBase64Hash() + "\"" + ", ";
				}
				hashList += "\"" + users.get(users.size()-1).getBase64Hash() + "\"";
				hashList += "}";
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesPost}}", hashList);
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorPost}}", "ResourceProtector.protect(request, authorizedHashesPost);");
			} else {
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesPost}}", "null");
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorPost}}", "");
			}
			
			// Add user base64 hashes for users having access to the putter
			if(currentClass.hasPutter() && currentClass.getPutter().isRestricted()) {
				String hashList = "{";
				List<UserLoginData> users = currentClass.getPutter().getAuthorizedUsers();
				users.addAll(currentClass.getPutter().getInheritedUsers());
				for(int i = 0; i<users.size()-1; ++i) {
					hashList += "\"" + users.get(i).getBase64Hash() + "\"" + ", ";
				}
				hashList += "\"" + users.get(users.size()-1).getBase64Hash() + "\"";
				hashList += "}";
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesPut}}", hashList);
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorPut}}", "ResourceProtector.protect(request, authorizedHashesPut);");
			} else {
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesPut}}", "null");
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorPut}}", "");
			}
			
			// Add user base64 hashes for users having access to the deleter
			if(currentClass.hasDeleter() && currentClass.getDeleter().isRestricted()) {
				String hashList = "{";
				List<UserLoginData> users = currentClass.getDeleter().getAuthorizedUsers();
				users.addAll(currentClass.getDeleter().getInheritedUsers());
				for(int i = 0; i<users.size()-1; ++i) {
					hashList += "\"" + users.get(i).getBase64Hash() + "\"" + ", ";
				}
				hashList += "\"" + users.get(users.size()-1).getBase64Hash() + "\"";
				hashList += "}";
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesDelete}}", hashList);
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorDelete}}", "ResourceProtector.protect(request, authorizedHashesDelete);");
			} else {
				resourceTemplate = resourceTemplate.replace("{{AuthorizedHashesDelete}}", "null");
				resourceTemplate = resourceTemplate.replace("{{ResourceProtectorDelete}}", "");
			}
			
			myWriter.write(resourceTemplate);			
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		

		// Get child classes and return them
		List<ResourceItem> children = new ArrayList<ResourceItem>();
		for (ResourceItem item : currentClass.getChildren().values()) {
			children.add(item);			
		}

		return children;
	}
	
	/**
	 * Create Jersey project files for Eclipse IDE
	 */
	protected void createProjectFiles() {
		BufferedWriter myWriter = FileOperations.createFile(outputBaseFolderName + ".classpath");
		String classpath = 	"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
							"<classpath> \n" +
							"<classpathentry kind=\"src\" path=\"src\"/> \n" +
							"<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.6\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/jersey/asm.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/jersey/grizzly-servlet-webserver.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/jersey/jersey-bundle.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/jersey/jsr311.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/json/json.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/jdom/jdom.jar\"/> \n" +
							"<classpathentry kind=\"lib\" path=\"lib/jython/jython.jar\"/> \n" +
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
