package org.fao.aoscs.client.module.effects;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Modality {
	private static List<Widget> blinders = new ArrayList<Widget>();
	
	private static final double DEFAULT_OPACITY = 0.70;
	private static final String DEFAULT_BACKGROUND_COLOR = "black";
	
	private static double opacity = DEFAULT_OPACITY;
	private static String backgroundColor = DEFAULT_BACKGROUND_COLOR;
	
	public static void errorBox(String message) {
		Window.alert(message);
	}
	
	public static void push() {
		push(null);
	}
	
	public static void push(ClickHandler handler) {
		Widget w;
		
		if ( !blinders.isEmpty() ) {
			w = (Widget) blinders.get(blinders.size() -1);
			DOM.setStyleAttribute(w.getElement(), "display", "none");
		}
		
		w = makeNew(handler);
		blinders.add(w);
		RootPanel.get().add(w, Window.getScrollLeft(), Window.getScrollTop());
	}
	
	public static void pop() {
		if ( blinders.isEmpty() ) return;
		Widget w = (Widget)blinders.remove(blinders.size() -1);
		RootPanel.get().remove(w);
		if ( !blinders.isEmpty() ) {
			w = (Widget)blinders.get(blinders.size() -1);
			DOM.setStyleAttribute(w.getElement(), "display", "block");
		}
	}
	
	public static boolean modalClear() {
		return blinders.isEmpty();
	}
	
	public static void setOpacity(double opacity) {
		if ( opacity < 0 || opacity > 1 ) throw new IllegalArgumentException(
				"percentage must be between 0 and 1, inclusive");
		Modality.opacity = opacity;
	}
	
	public static void setBackground(String backgroundColor) {
		Modality.backgroundColor = backgroundColor;
	}
	
	private static Widget makeNew(ClickHandler handler) {
		Label panel = new Label();
		panel.setPixelSize(RootPanel.get().getOffsetWidth(),RootPanel.get().getOffsetHeight());
		//panel.setPixelSize(Window.getScrollLeft()+Window.getClientWidth(), Window.getScrollTop()+Window.getClientHeight());
		//panel.setPixelSize(Window.getClientWidth(), Window.getClientHeight());
		ExtDOM.setOpacity(panel, opacity);
		DOM.setStyleAttribute(panel.getElement(), "backgroundColor", backgroundColor);
		if ( handler != null ) {
			panel.addClickHandler(handler);
			ExtDOM.setClickPointer(panel, true);
		}
		return panel;
	}
	
	private static ResizeHandler resizeHandler;
	private static boolean listening;
	private static List<Widget> widgets = new ArrayList<Widget>();
	
	public static void center(Widget p, int w, int h) {
		center0(false, null, p, w, h);
	}
	
	public static void animateAndCenter(Widget animateFrom, Widget p, int w, int h) {
		center0(true, animateFrom, p, w, h);
	}
	
	private static void center0(boolean animate, Widget animSource, final Widget p, final int w, final int h) {
		if ( !listening ) {
			listening = true;
			if ( resizeHandler == null ) resizeHandler = new ResizeHandler() 
			{ 
				public void onResize(ResizeEvent event) {
					if ( widgets.isEmpty() ) {
						listening = false;
						return;
					}
					
					for ( int i = 0 ; i < widgets.size() ; i++ ) {
						Widget p = (Widget) widgets.get(i);
						int h = p.getOffsetHeight();
						int w = p.getOffsetWidth();
						RootPanel.get().setWidgetPosition(p, (event.getWidth() - w)/2, (event.getHeight() - h)/2);
					}
				}};
			Window.addResizeHandler(resizeHandler);
		}
		
		p.setPixelSize(w, h);
		final int width = Window.getClientWidth();
		final int height = Window.getClientHeight();
		
		final int left = (width - w)/2;
		final int top = (height -h)/2;
		
		Command display = new Command() { public void execute() {
			Modality.push();
			RootPanel.get().add(p, left, top);
		}};
		
		if ( animate ) new MorphEffect().start(animSource).end(left, top, w, h).hook(display).go();
		else display.execute();
	}
	
	public static void centerDone(Widget p) {
		widgets.remove(p);
		RootPanel.get().remove(p);
		Modality.pop();
	}
	
	private static int ignoreEventsCounter = 0;
	
	public static void pushIgnoreEvents() {
		ignoreEventsCounter++;
		if ( ignoreEventsCounter != 1 ) return;
		//DOM.addEventPreview(ignoringPreview);
		Event.addNativePreviewHandler(ignoringPreview);
	}
	
	public static void popIgnoreEvents() {
		ignoreEventsCounter--;
		
		if ( ignoreEventsCounter < 0 ) {
			ignoreEventsCounter = 0;
			return;
		} else if ( ignoreEventsCounter > 0 ) return;
		
		//DOM.removeEventPreview(ignoringPreview);
		Event.addNativePreviewHandler(ignoringPreview);
	}
	
	private static NativePreviewHandler ignoringPreview = new NativePreviewHandler() {
		public void onPreviewNativeEvent(NativePreviewEvent event) {
			
		}
	};
}

