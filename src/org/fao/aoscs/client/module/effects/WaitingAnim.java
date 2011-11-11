package org.fao.aoscs.client.module.effects;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class WaitingAnim extends Widget {
	private final Element img;
	private boolean showWaitCursor;
	
	private WaitingAnim(String url) {
		Image.prefetch(url);
		img = DOM.createImg();
		DOM.setElementProperty(img, "src", url);
		DOM.setStyleAttribute(img, "visibility", "hidden");
		
		setElement(img);
	}
	
	/**
	 * 13 by 13 animated waiting image.
	 */
	public static WaitingAnim small() {
		return new WaitingAnim("images/loading.gif");
	}
	
	/**
	 * 32 by 32 animated waiting image.
	 */
	public static WaitingAnim large() {
		return new WaitingAnim("images/loading.gif");
	}
	
	/**
	 * If 'showWaitCursor' is true, the body is marked to have a 'wait' type cursor,
	 * until you hide() this animation. Do hide it if you use this!
	 */
	public WaitingAnim show(boolean showWaitCursor) {
		DOM.setStyleAttribute(img, "visibility", "visible");
		this.showWaitCursor = showWaitCursor;
		if ( showWaitCursor ) DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
		return this;
	}
	
	public WaitingAnim hide() {
		DOM.setStyleAttribute(img, "visibility", "hidden");
		if ( showWaitCursor ) DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
		showWaitCursor = false;
		return this;
	}
}
