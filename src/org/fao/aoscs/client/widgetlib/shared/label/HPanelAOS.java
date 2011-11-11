package org.fao.aoscs.client.widgetlib.shared.label;

import org.fao.aoscs.domain.TreeObject;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HPanelAOS extends VerticalPanel{
	
	private HorizontalPanel labelPanel = new HorizontalPanel();
	private int step = -1;
	private TreeObject treeObject = new TreeObject();
	private VerticalPanel childPanel = new VerticalPanel();
	
	public HPanelAOS(TreeObject treeObject, int step){
		this.setTreeObject(treeObject);
		this.step = step;
		this.add(childPanel);
		this.add(labelPanel);
	}

	public void setLabelPanel(HorizontalPanel labelPanel) {
		this.labelPanel.add(labelPanel);
	}

	public HorizontalPanel getLabelPanel() {
		return labelPanel;
	}

	public void setChildPanel(VerticalPanel childPanel) {
		this.childPanel.add(childPanel);
	}

	public VerticalPanel getChildPanel() {
		return childPanel;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getStep() {
		return step;
	}

	public void setTreeObject(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	public TreeObject getTreeObject() {
		return treeObject;
	}


	
}
