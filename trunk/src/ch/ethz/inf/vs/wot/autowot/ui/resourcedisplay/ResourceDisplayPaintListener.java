package ch.ethz.inf.vs.wot.autowot.ui.resourcedisplay;

/**
 * Listener reacting to paint events and drawing the graphical representation
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.project.resources.DeleterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.GetterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PosterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PutterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;
import ch.ethz.inf.vs.wot.autowot.ui.views.UserInterface;

public class ResourceDisplayPaintListener implements PaintListener {
	protected AutoWoT application = null;
	protected UserInterface mainUI = null;
	protected ResourceDisplayMouseListener mouseListener = null;
	
	public ResourceDisplayPaintListener (AutoWoT application, UserInterface mainUI, ResourceDisplayMouseListener mouseListener) {
		this.application = application;
		this.mainUI = mainUI;
		this.mouseListener = mouseListener;
	}
	
	public void paintControl(PaintEvent e) {
		mouseListener.clickAreas.clear();
        if(application.getCurrentResource().getIsResource()) {
        	printAsResource(e);
        } else if(application.getCurrentResource().getIsGetter()) {
        	printAsGetter(e);
        } else if(application.getCurrentResource().getIsPoster()) {
        	printAsPoster(e);
        } else if(application.getCurrentResource().getIsPutter()) {
        	printAsPutter(e);
        } else if(application.getCurrentResource().getIsDeleter()) {
        	printAsDeleter(e);
        } else {
        	e.gc.drawText("Welcome to the AutoWoT Prototyper for automatic WoTizing of Resources!", 10, 10);
        }
    }
	
	protected void printAsResource(PaintEvent e) {
		Rectangle clientArea = mainUI.resourceDisplay.getClientArea();
        int hcenter = clientArea.width/2;
        int vcenter = clientArea.height/2;
        
        // Get information about the resource
		ResourceItem resource = (ResourceItem) application.getCurrentResource();
    	String resourceName = "Resource: " + resource.getResourceName();
    	String resourceURL = "URL: " + resource.getURI();
    	String resourceMethods = "Methods: " + resource.getMethods(); 
        int resourceWidth = java.lang.Math.max(java.lang.Math.max(resourceName.length(), resourceURL.length()), resourceMethods.length()) * 7 + 70;
        int resourceHeight = 120;
        
        // Draw Getter (if present)
    	if(resource.hasGetter()) {
        	e.gc.setBackground(new Color(Display.getCurrent(), 0, 255, 127));
        	e.gc.setForeground(new Color(Display.getCurrent(), 0, 255, 127));
        	
        	String name = "Getter: " + resource.getGetter().getResourceName();
        	int getterWidth = name.length() * 8;
        	int getterHeight = 60;
        	int getterHBegin = 5;
        	int getterVBegin = vcenter - resourceHeight - 12;
        	
        	e.gc.fillOval(getterHBegin, getterVBegin, getterWidth, getterHeight);
        	e.gc.drawLine(getterHBegin + getterWidth/2, getterVBegin + getterHeight/2, hcenter, vcenter);
        	if(resource.getGetter().isRestricted()) {
	        	e.gc.drawImage(new Image(mainUI.display,"images/lock_small.png"), getterHBegin + getterWidth/2 - 4, getterVBegin+2);
	        }
        	
        	mouseListener.addArea(getterHBegin, getterVBegin, getterHBegin + getterWidth, getterVBegin + getterHeight, resource.getGetter());
        	
        	e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
        	e.gc.drawText(name, getterHBegin + 10, getterVBegin + 22);
    	}
        
    	// Draw Poster (if present)
        if(resource.hasPoster()) {
        	e.gc.setBackground(new Color(Display.getCurrent(), 0, 191, 255));
        	e.gc.setForeground(new Color(Display.getCurrent(), 0, 191, 255));
        	
        	String name = "Poster: " + resource.getPoster().getResourceName();
        	int posterWidth = name.length() * 8;
        	int posterHeight = 60;
        	int posterHBegin = hcenter - posterWidth - 20;
        	int posterVBegin = vcenter - resourceHeight - 60;
        	
        	e.gc.fillOval( posterHBegin, posterVBegin, posterWidth, posterHeight);
        	e.gc.drawLine(posterHBegin + posterWidth/2, posterVBegin + posterHeight/2, hcenter, vcenter);
        	if(resource.getPoster().isRestricted()) {
	        	e.gc.drawImage(new Image(mainUI.display,"images/lock_small.png"), posterHBegin + posterWidth/2 - 4, posterVBegin+2);
	        }
        	
        	mouseListener.addArea(posterHBegin, posterVBegin, posterHBegin + posterWidth, posterVBegin + posterHeight, resource.getPoster());
        	
        	e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
        	e.gc.drawText(name, posterHBegin + 10, posterVBegin + 20);
        }
        
        // Draw Putter (if present)
        if(resource.hasPutter()) {
        	e.gc.setBackground(new Color(Display.getCurrent(), 191, 255, 0));
        	e.gc.setForeground(new Color(Display.getCurrent(), 191, 255, 0));
        	
        	String name = "Putter: " + resource.getPutter().getResourceName();
        	int putterWidth = name.length() * 8;
        	int putterHeight = 60;
        	int putterHBegin = hcenter + 20;
        	int putterVBegin = vcenter - resourceHeight - 60;
        	
        	e.gc.fillOval(putterHBegin, putterVBegin, putterWidth, putterHeight);
        	e.gc.drawLine(putterHBegin + putterWidth/2, putterVBegin + putterHeight/2, hcenter, vcenter);
        	if(resource.getPutter().isRestricted()) {
	        	e.gc.drawImage(new Image(mainUI.display,"images/lock_small.png"), putterHBegin + putterWidth/2 - 4, putterVBegin+2);
	        }
        	
        	mouseListener.addArea(putterHBegin, putterVBegin, putterHBegin + putterWidth, putterVBegin + putterHeight, resource.getPutter());
        	
        	e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
        	e.gc.drawText(name, putterHBegin + 10, putterVBegin + 20);
        }
        
        // Draw Deleter (if present)
        if(resource.hasDeleter()) {
        	e.gc.setBackground(new Color(Display.getCurrent(), 255, 191, 0));
        	e.gc.setForeground(new Color(Display.getCurrent(), 255, 191, 0));
        	
        	String name = "Deleter: " + resource.getDeleter().getResourceName();
        	int deleterWidth =  name.length() * 8;
        	int deleterHeight = 60;
        	int deleterHBegin = clientArea.width - deleterWidth - 5;
        	int deleterVBegin = vcenter - resourceHeight - 12;
        	
        	e.gc.fillOval(deleterHBegin, deleterVBegin, deleterWidth, deleterHeight);
        	e.gc.drawLine(deleterHBegin + deleterWidth/2, deleterVBegin + deleterHeight/2, hcenter, vcenter);
        	if(resource.getDeleter().isRestricted()) {
	        	e.gc.drawImage(new Image(mainUI.display,"images/lock_small.png"), deleterHBegin + deleterWidth/2 - 4, deleterVBegin+2);
	        }
        	
        	mouseListener.addArea(deleterHBegin, deleterVBegin, deleterHBegin + deleterWidth, deleterVBegin + deleterHeight, resource.getDeleter());
        	
        	e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
        	e.gc.drawText(name, deleterHBegin + 10, deleterVBegin + 20);
        }
        
        // Draw Children (if present)
        if(resource.hasChildren()) {
        	int maxLength = 0;
        	
        	// Get maximum name length
        	for(ResourceItem child : resource.getChildren().values()) {
        		if(child.getResourceName().length() > maxLength) {
        			maxLength = child.getResourceName().length();
        		}
        		if(child.getURI().length() > maxLength) {
        			maxLength = child.getURI().length();
        		}
        	}
        	
        	// Compute maximum width
        	maxLength += 7;
        	int childWidth = maxLength * 8 + 10;
        	int childHeight = 60;
        	int childCount = resource.getChildNames().size();
        	
        	// Compute position of first child
        	int childCountPerRow = clientArea.width / (childWidth + 5);
        	int currentChildHPos = (clientArea.width - (java.lang.Math.min(childCount, childCountPerRow) * (childWidth + 5)))/2;
        	int currentChildVPos = vcenter + resourceHeight/2 + 30;
        	boolean firstRow = true;
        	
        	// Take each child
        	for(ResourceItem child : resource.getChildren().values()) {
        		
        		// If going "past the end" of the visible area, go to new line
        		if(currentChildHPos + childWidth > clientArea.width) {
        			currentChildHPos = (clientArea.width - (childCountPerRow * (childWidth + 5)))/2;
        			currentChildVPos += childHeight + 10;
        			firstRow = false;
        		}
        		
        		// Get information about the child
        		String name = "Child: " + child.getResourceName();
        		String url = "URL: " + child.getURI();
        		
        		// Draw child
        		e.gc.setBackground(new Color(Display.getCurrent(), 0, 255, 255));
	        	e.gc.setForeground(new Color(Display.getCurrent(), 0, 255, 255));
        		e.gc.fillRoundRectangle(currentChildHPos, currentChildVPos, childWidth, childHeight, 5, 5);
        		if(child.isRestricted()) {
    	        	e.gc.drawImage(new Image(mainUI.display,"images/lock_small.png"), currentChildHPos+2, currentChildVPos+2);
    	        }
        		if(firstRow) {
        			e.gc.drawLine(currentChildHPos + childWidth/2 + 1, currentChildVPos, hcenter, vcenter);
        		}
        		
        		mouseListener.addArea(currentChildHPos, currentChildVPos, currentChildHPos+childWidth, currentChildVPos + childHeight, child);
        		
        		e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
        		e.gc.drawText(name, (int) (currentChildHPos + childWidth/2 - maxLength * 4 + 10), currentChildVPos + 14);
        		e.gc.drawText(url, (int) (currentChildHPos + childWidth/2 - maxLength * 4 + 10), currentChildVPos + 34);
        		currentChildHPos += childWidth + 5;
        	}
        }
        
        // Draw parent
        e.gc.setBackground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        e.gc.setForeground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        if(resource.getParentResource() != null) {
        	e.gc.fillRoundRectangle(hcenter-50, 10, 100, 30, 10, 10);
        	e.gc.drawLine(hcenter, 40, hcenter, vcenter);
        	e.gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        	e.gc.drawText("Go to parent", hcenter - 40, 16);
        	mouseListener.addArea(hcenter-50, 10, hcenter+50, 40, resource.getParentResource());
        }
        
        // Draw resource itself
        e.gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        e.gc.fillRoundRectangle(hcenter-resourceWidth/2, vcenter-resourceHeight/2, resourceWidth, resourceHeight, 10, 10); 
        e.gc.drawText(resourceName, hcenter - resourceWidth/2 + 35, vcenter-25);
        e.gc.drawText(resourceURL, hcenter - resourceWidth/2 + 35, vcenter-5);
        e.gc.drawText(resourceMethods, hcenter - resourceWidth/2 + 35, vcenter+15);
        if(resource.isRestricted()) {
        	e.gc.drawImage(new Image(mainUI.display,"images/lock_white.png"), hcenter-8, vcenter-resourceHeight/2+2);
        }
        
        e.gc.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}
	
	protected void printAsGetter(PaintEvent e) {
		Rectangle clientArea = mainUI.resourceDisplay.getClientArea();
        int hcenter = clientArea.width/2;
        int vcenter = clientArea.height/2;
        
		GetterItem getter = application.getCurrentResource().asGetterItem();
		String resourceName = "Getter: " + getter.getResourceName();
    	String resourceMethod = "Method: " + getter.getCallbackMethod();
    	String resourceType = "Type: " + getter.getCallbackMethodType();
        int resourceWidth = java.lang.Math.max(java.lang.Math.max(resourceName.length(), resourceMethod.length()), resourceType.length()) * 7 + 70;
        int resourceHeight = 120;
        
        e.gc.setBackground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        e.gc.setForeground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        if(getter.getParentResource() != null) {
        	e.gc.fillRoundRectangle(hcenter-50, 10, 100, 30, 10, 10);
        	e.gc.drawLine(hcenter, 40, hcenter, vcenter);
        	e.gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        	e.gc.drawText("Go to parent", hcenter - 40, 16);
        	mouseListener.addArea(hcenter-50, 10, hcenter+50, 40, getter.getParentResource());
        }
        
		e.gc.setBackground(new Color(Display.getCurrent(), 0, 255, 127));
    	e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
    	e.gc.fillRoundRectangle(hcenter-resourceWidth/2, vcenter-resourceHeight/2, resourceWidth, resourceHeight, 10, 10); 
        e.gc.drawText(resourceName, hcenter - resourceWidth/2 + 35, vcenter-25);
        e.gc.drawText(resourceMethod, hcenter - resourceWidth/2 + 35, vcenter-5);
        e.gc.drawText(resourceType, hcenter - resourceWidth/2 + 35, vcenter+15);
        if(getter.isRestricted()) {
        	e.gc.drawImage(new Image(mainUI.display,"images/lock.png"), hcenter-8, vcenter-resourceHeight/2+2);
        }
        
        e.gc.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}
	
	protected void printAsPoster(PaintEvent e) {
		Rectangle clientArea = mainUI.resourceDisplay.getClientArea();
        int hcenter = clientArea.width/2;
        int vcenter = clientArea.height/2;
        
		PosterItem poster = application.getCurrentResource().asPosterItem();
		String resourceName = "Poster: " + poster.getResourceName();
    	String resourceMethod = "Method: " + poster.getCallbackMethod();
    	String resourceType = "Type: " + poster.getCallbackMethodType();
        int resourceWidth = java.lang.Math.max(java.lang.Math.max(resourceName.length(), resourceMethod.length()), resourceType.length()) * 7 + 70;
        int resourceHeight = 120;
        
        e.gc.setBackground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        e.gc.setForeground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        if(poster.getParentResource() != null) {
        	e.gc.fillRoundRectangle(hcenter-50, 10, 100, 30, 10, 10);
        	e.gc.drawLine(hcenter, 40, hcenter, vcenter);
        	e.gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        	e.gc.drawText("Go to parent", hcenter - 40, 16);
        	mouseListener.addArea(hcenter-50, 10, hcenter+50, 40, poster.getParentResource());
        }
        
		e.gc.setBackground(new Color(Display.getCurrent(), 0, 191, 255));
    	e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
    	e.gc.fillRoundRectangle(hcenter-resourceWidth/2, vcenter-resourceHeight/2, resourceWidth, resourceHeight, 10, 10); 
        e.gc.drawText(resourceName, hcenter - resourceWidth/2 + 35, vcenter-25);
        e.gc.drawText(resourceMethod, hcenter - resourceWidth/2 + 35, vcenter-5);
        e.gc.drawText(resourceType, hcenter - resourceWidth/2 + 35, vcenter+15);
        if(poster.isRestricted()) {
        	e.gc.drawImage(new Image(mainUI.display,"images/lock.png"), hcenter-8, vcenter-resourceHeight/2+2);
        }
        
        e.gc.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}
	
	protected void printAsPutter(PaintEvent e) {
		Rectangle clientArea = mainUI.resourceDisplay.getClientArea();
        int hcenter = clientArea.width/2;
        int vcenter = clientArea.height/2;
        
		PutterItem putter = application.getCurrentResource().asPutterItem();
		String resourceName = "Putter: " + putter.getResourceName();
    	String resourceMethod = "Method: " + putter.getCallbackMethod();
    	String resourceType = "Type: " + putter.getCallbackMethodType();
        int resourceWidth = java.lang.Math.max(java.lang.Math.max(resourceName.length(), resourceMethod.length()), resourceType.length()) * 7 + 70;
        int resourceHeight = 120;
        
        e.gc.setBackground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        e.gc.setForeground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        if(putter.getParentResource() != null) {
        	e.gc.fillRoundRectangle(hcenter-50, 10, 100, 30, 10, 10);
        	e.gc.drawLine(hcenter, 40, hcenter, vcenter);
        	e.gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        	e.gc.drawText("Go to parent", hcenter - 40, 16);
        	mouseListener.addArea(hcenter-50, 10, hcenter+50, 40, putter.getParentResource());
        }
		e.gc.setBackground(new Color(Display.getCurrent(), 191, 255, 0));
    	e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
    	e.gc.fillRoundRectangle(hcenter-resourceWidth/2, vcenter-resourceHeight/2, resourceWidth, resourceHeight, 10, 10); 
        e.gc.drawText(resourceName, hcenter - resourceWidth/2 + 35, vcenter-25);
        e.gc.drawText(resourceMethod, hcenter - resourceWidth/2 + 35, vcenter-5);
        e.gc.drawText(resourceType, hcenter - resourceWidth/2 + 35, vcenter+15);
        if(putter.isRestricted()) {
        	e.gc.drawImage(new Image(mainUI.display,"images/lock.png"), hcenter-8, vcenter-resourceHeight/2+2);
        }
        
        e.gc.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}
	
	protected void printAsDeleter(PaintEvent e) {
		Rectangle clientArea = mainUI.resourceDisplay.getClientArea();
        int hcenter = clientArea.width/2;
        int vcenter = clientArea.height/2;
        
		DeleterItem deleter = application.getCurrentResource().asDeleterItem();
		String resourceName = "Deleter: " + deleter.getResourceName();
    	String resourceMethod = "Method: " + deleter.getCallbackMethod();
    	String resourceType = "Type: " + deleter.getCallbackMethodType();
        int resourceWidth = java.lang.Math.max(java.lang.Math.max(resourceName.length(), resourceMethod.length()), resourceType.length()) * 7 + 70;
        int resourceHeight = 120;
        
        e.gc.setBackground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        e.gc.setForeground(mainUI.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        if(deleter.getParentResource() != null) {
        	e.gc.fillRoundRectangle(hcenter-50, 10, 100, 30, 10, 10);
        	e.gc.drawLine(hcenter, 40, hcenter, vcenter);
        	e.gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        	e.gc.drawText("Go to parent", hcenter - 40, 16);
        	mouseListener.addArea(hcenter-50, 10, hcenter+50, 40, deleter.getParentResource());
        }
        
		e.gc.setBackground(new Color(Display.getCurrent(), 255, 191, 0));
    	e.gc.setForeground(new Color(Display.getCurrent(), 0, 0, 0));
    	e.gc.fillRoundRectangle(hcenter-resourceWidth/2, vcenter-resourceHeight/2, resourceWidth, resourceHeight, 10, 10); 
        e.gc.drawText(resourceName, hcenter - resourceWidth/2 + 35, vcenter-25);
        e.gc.drawText(resourceMethod, hcenter - resourceWidth/2 + 35, vcenter-5);
        e.gc.drawText(resourceType, hcenter - resourceWidth/2 + 35, vcenter+15);
        if(deleter.isRestricted()) {
        	e.gc.drawImage(new Image(mainUI.display,"images/lock.png"), hcenter-8, vcenter-resourceHeight/2+2);
        }
        
        e.gc.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	}
}
