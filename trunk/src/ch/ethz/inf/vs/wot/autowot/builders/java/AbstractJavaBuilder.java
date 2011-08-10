package ch.ethz.inf.vs.wot.autowot.builders.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.binary.StringUtils;

import ch.ethz.inf.vs.wot.autowot.builders.utils.FileOperations;
import ch.ethz.inf.vs.wot.autowot.builders.utils.FileUtils;
import ch.ethz.inf.vs.wot.autowot.builders.xml.XMLBuilder;
import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.project.Project;
import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallback;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;

/**
 * 
 * Super class for all Java Builders
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public abstract class AbstractJavaBuilder {

	// Class names
	protected List<HandlerCallback> handlerCallbacks = null;
	protected String handlerClassCanonicalName = null;
	protected String handlerClassPackageName = null;
	protected String handlerClassFolderName = null;

	// Package names
	protected String basePackageName = null;
	protected String commonsPackageName = null;
	protected String resourcesPackageName = null;
	protected String securityPackageName = null;

	// Output folder locations
	protected String outputBaseFolderName = null;
	protected String outputLibrariesFolderName = null;
	protected String outputWebresourcesFolderName = null;
	protected String outputSourceFolderName = null;
	protected String outputCommonsFolderName = null;
	protected String outputResourcesFolderName = null;
	protected String outputSecurityFolderName = null;

	// Input Folder locations
	protected String inputFolderName = null;
	protected String inputCommonsFolderName = null;

	// Template locations
	protected String templateFolderName = null;
	
	// Package and folder suffixes
	protected String commonsSuffix = "commons";
	protected String resourcesSuffix = "resources";
	protected String securitySuffix = "security";

	// Flag for creating a standalone server
	protected Boolean makeStandalone = null;

	// Root resource from which the parsing starts
	protected ResourceItem rootResource = null;

	// Author and Meta-Authors
	protected String authorString = null;
	protected String metaAuthorString = null;

	protected BufferedWriter documentationWriter = null;


	/**
	 * Constructor setting all required folder and package names
	 */
	public AbstractJavaBuilder(Project project) {

		// Set package names
		basePackageName = project.getPackageCanonical();
		commonsPackageName = basePackageName + "." + commonsSuffix;
		resourcesPackageName = basePackageName + "." + resourcesSuffix;
		securityPackageName = basePackageName + "." + securitySuffix;
		handlerClassCanonicalName = project.getHandlerCanonical();

		// Set output folder names
		outputBaseFolderName = project.getFileSystemPath(); // Was FileOperations.getCurrentSystemPath() + File.separator + Constants.BASE_FOLDER_NAME + "JavaProject" + File.separator;
		outputLibrariesFolderName = outputBaseFolderName + "lib" + File.separator;
		outputWebresourcesFolderName = outputBaseFolderName + "webresources" + File.separator;
		outputSourceFolderName = outputBaseFolderName + "src" + File.separator + basePackageName.replace(".", File.separator) + File.separator;
		outputCommonsFolderName = outputSourceFolderName + commonsSuffix + File.separator;
		outputResourcesFolderName = outputSourceFolderName + resourcesSuffix + File.separator;
		outputSecurityFolderName = outputSourceFolderName + securitySuffix + File.separator;

		// Set Handler folder and package names
		handlerClassCanonicalName = project.getHandlerCanonical();
		handlerClassPackageName = handlerClassCanonicalName.substring(0, handlerClassCanonicalName.lastIndexOf("."));
		handlerClassFolderName = outputBaseFolderName + "src" + File.separator + this.handlerClassPackageName.replace(".", File.separator) + File.separator; 
		handlerCallbacks = new ArrayList<HandlerCallback>();

		// Set input folder names for common elements
		inputCommonsFolderName = null; // Constants.SOURCE_FOLDER + "javaresources" + File.separator + "commons" + File.separator;

		// Set all flags accordingly
		makeStandalone = project.getMakeStandalone();
		rootResource = project.getRootResource();
		authorString = this.getClass().getCanonicalName();
		metaAuthorString = "Simon Mayer, ETH Zurich; Claude Barthels, ETH Zurich";
	}


	/**
	 * Create the web server
	 * 
	 */
	public abstract void build();

	/**
	 * Copy the structure configuration file
	 */
	protected void copyStructureConfiguration() {
		File inFile = new File(XMLBuilder.resourceConfigTempLocation);
		FileOperations.makeDirectory(outputBaseFolderName + System.getProperty("file.separator") + "config");
		File outFile2 = new File(outputBaseFolderName + System.getProperty("file.separator") + "config" + System.getProperty("file.separator") + "configuration.xml");
		try {
			FileOperations.copyFile(inFile, outFile2, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the directory structure
	 */
	protected void createAndCopyResourceDirectories() {
		File outFile = null;
		File outFile2 = null;

		// Delete current project directory
		FileOperations.deleteDir(outputBaseFolderName);

		// Create new directory structure
		FileOperations.makeDirectory(outputBaseFolderName);
		FileOperations.makeDirectory(outputCommonsFolderName);
		FileOperations.makeDirectory(outputResourcesFolderName);
		FileOperations.makeDirectory(outputWebresourcesFolderName);
		FileOperations.makeDirectory(outputLibrariesFolderName);
		FileOperations.makeDirectory(outputSecurityFolderName);		

		// Copy required files
		if (this.getClass().getResource("/webresources").toString().startsWith("file:")) outFile2 = new File(outputBaseFolderName);
		else outFile2 = new File(outputWebresourcesFolderName);
		FileUtils.copyResourcesRecursively(this.getClass().getResource("/webresources"), outFile2);
		System.out.println("Copying required webresources at " + this.getClass().getResource("/webresources") + " to " + outFile2.getAbsolutePath());	

		if (this.getClass().getResource("/lib").toString().startsWith("file:")) outFile2 = new File(outputBaseFolderName);
		else outFile2 = new File(outputLibrariesFolderName);
		FileUtils.copyResourcesRecursively(this.getClass().getResource("/lib"), outFile2);
		System.out.println("Copying required libraries at " + this.getClass().getResource("/lib") + " to " + outFile2.getAbsolutePath());

		/* Old code... */
//		outFile = new File(Constants.SOURCE_FOLDER + "webresources" + System.getProperty("file.separator"));
//
//		try {
//			outFile = new File(this.getClass().getResource("/webresources").toURI());
//			try {
//				System.out.println(outFile.getCanonicalPath());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (URISyntaxException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		outFile = new File(Constants.SOURCE_FOLDER + "commons" + System.getProperty("file.separator"));
//		try {
//			outFile = new File(this.getClass().getResource("/commons").toURI());
//		} catch (URISyntaxException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		outFile2 = new File(outputLibrariesFolderName);
//		System.out.println("Copying required libraries to " + outputLibrariesFolderName);
//		try {
//			FileOperations.copyDirectory(outFile, outFile2, false);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}


	/**
	 * Create Handler file
	 */
	protected void copyHandlerClass() {
		BufferedWriter myWriter = FileOperations.createFile(handlerClassFolderName + "Handler.java");
		String handlerTemplate;
		try {
			handlerTemplate = FileOperations.readFileAsString(inputCommonsFolderName + "HandlerTemplate");
			handlerTemplate = handlerTemplate.replace("{{AuthorName}}", this.authorString);
			handlerTemplate = handlerTemplate.replace("{{MetaAuthorName}}", this.metaAuthorString);
			handlerTemplate = handlerTemplate.replace("{{PackageName}}", this.handlerClassPackageName);

			// Output all callback functions
			for (HandlerCallback callback : handlerCallbacks) {
				String callbackStub = callback.toString();
				callbackStub += "\n\n\t{{HandlerMethods}}";
				handlerTemplate = handlerTemplate.replace("{{HandlerMethods}}", callbackStub);
			}

			handlerTemplate = handlerTemplate.replace("{{HandlerMethods}}", "");
			myWriter.write(handlerTemplate);
			myWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}	

	/**
	 * Output an indentation of n tabs
	 * 
	 */
	protected static String indent(int n) {
		String returnString = "";
		for (int i = 0; i < n; i++) returnString += "\t";
		return returnString;
	}
}

