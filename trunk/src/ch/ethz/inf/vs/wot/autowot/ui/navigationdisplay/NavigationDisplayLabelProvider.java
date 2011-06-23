package ch.ethz.inf.vs.wot.autowot.ui.navigationdisplay;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import ch.ethz.inf.vs.wot.autowot.project.resources.DeleterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.GetterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PosterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.PutterItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;

/**
 * Class providing the icons of the navigation display
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class NavigationDisplayLabelProvider implements ILabelProvider{

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getImage(Object arg0) {
		Image result = null;
		if(arg0 instanceof ResourceItem) {
			result = new Image(null, this.getClass().getResourceAsStream("/resource.png"));
		} else if(arg0 instanceof GetterItem) {
			result = new Image(null, this.getClass().getResourceAsStream("/getter.png"));
		} else if(arg0 instanceof PosterItem) {
			result = new Image(null, this.getClass().getResourceAsStream("/poster.png"));
		} else if(arg0 instanceof PutterItem) {
			result = new Image(null, this.getClass().getResourceAsStream("/putter.png"));
		} else if(arg0 instanceof DeleterItem) {
			result = new Image(null, this.getClass().getResourceAsStream("/deleter.png"));
		}
		return result;
	}

	@Override
	public String getText(Object arg0) {
		if(arg0 instanceof ResourceItem) {
			return ((ResourceItem) arg0).getResourceName() + " (" + ((ResourceItem) arg0).getURI() + ")";
		} else if(arg0 instanceof GetterItem) {
			return ((GetterItem) arg0).getResourceName();
		} else if(arg0 instanceof PosterItem) {
			return ((PosterItem) arg0).getResourceName();
		} else if(arg0 instanceof PutterItem) {
			return ((PutterItem) arg0).getResourceName();
		} else if(arg0 instanceof DeleterItem) {
			return ((DeleterItem) arg0).getResourceName();
		}
		return "Unkown Resource Type";
	}
	
}
