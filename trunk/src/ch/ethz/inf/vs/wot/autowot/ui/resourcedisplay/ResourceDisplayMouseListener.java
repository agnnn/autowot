package ch.ethz.inf.vs.wot.autowot.ui.resourcedisplay;

/**
 * Listener reacting to click events on the display area in the
 * graphical mode
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.ui.views.UserInterface;

public class ResourceDisplayMouseListener implements MouseListener{
	
	protected AutoWoT application = null;
	protected UserInterface mainUI = null;
	protected List<ClickArea> clickAreas = new ArrayList<ClickArea>();
	
	public ResourceDisplayMouseListener(AutoWoT application, UserInterface mainUI) {
		this.application = application;
		this.mainUI = mainUI;
	}
	
	public void addArea(int leftX, int topY, int rightX, int bottomY, AbstractResourceItem resource) {
		clickAreas.add(new ClickArea(leftX, topY, rightX, bottomY, resource));
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseUp(MouseEvent arg0) {
		for(ClickArea area : clickAreas) {
			if(area.isIn(arg0.x, arg0.y)) {
				application.setCurrentResource(area.resource);
				mainUI.refresh();
				break;
			}
		}
	}
	
	private class ClickArea {
		
		public int leftX;
		public int topY;
		public int rightX;
		public int bottomY;
		public AbstractResourceItem resource;
		
		public ClickArea(int leftX, int topY, int rightX, int bottomY, AbstractResourceItem resource) {
			this.leftX = leftX;
			this.topY = topY;
			this.rightX = rightX;
			this.bottomY = bottomY;
			this.resource = resource;
		}
		
		public boolean isIn(int x, int y) {
			if(leftX<=x && x<=rightX) {
				if(topY<=y && y<=bottomY) {
					return true;
				}
			}
			return false;
		}
	}
	
}


