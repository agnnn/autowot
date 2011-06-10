package ch.ethz.inf.vs.wot.autowot.ui.navigationdisplay;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;

/**
 * Class providing the content of the navigation display
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class NavigationDisplayContentProvider implements ITreeContentProvider {
	
	protected AbstractResourceItem invisibleRoot;
	protected AutoWoT application;
	
	public NavigationDisplayContentProvider(AutoWoT application, AbstractResourceItem invisibleRoot) {
		this.invisibleRoot = invisibleRoot;
		this.application = application;
	}
	
	@Override
	public Object[] getElements(Object arg0) {
		return getChildren(arg0);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		((TreeViewer) arg0).refresh();
	}
	
	@Override
	public Object[] getChildren(Object arg0) {
		if(arg0 == invisibleRoot && application.getCurrentProject().getRootResource() != null) {
			return new Object[] {application.getCurrentProject().getRootResource()};
		} else if (arg0 instanceof ResourceItem) {
			ResourceItem res = (ResourceItem) arg0;
			ArrayList<Object> result = new ArrayList<Object>();
			if(res.hasGetter()) result.add(res.getGetter());
			if(res.hasPoster()) result.add(res.getPoster());
			if(res.hasPutter()) result.add(res.getPutter());
			if(res.hasDeleter()) result.add(res.getDeleter());
			result.addAll(res.asResourceItem().getChildren().values());
			return result.toArray();
		}
		
		return new Object[0];
	}

	@Override
	public Object getParent(Object arg0) {
		if (arg0 instanceof AbstractResourceItem) {
			AbstractResourceItem res = (AbstractResourceItem) arg0;
			return res.getParentResource();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		if (arg0 instanceof ResourceItem) {
			ResourceItem res = (ResourceItem) arg0;
			return !res.getChildren().isEmpty() | res.hasGetter() | res.hasPoster() | res.hasPutter() | res.hasDeleter();
		}
		
		return false;
	}
	
}
