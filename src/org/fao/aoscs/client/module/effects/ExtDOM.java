package org.fao.aoscs.client.module.effects;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public abstract class ExtDOM {
	private ExtDOM() {
		//Don't extend;
	}
	
	public static native boolean exceedsBoundingBox(Element element) /*-{
		return element.scrollWidth > element.clientWidth || element.scrollHeight > element.clientHeight;
	}-*/;
	
	public static void setOpacity(UIObject e, double factor) {
		if ( factor > .995 ) factor = 1;
		if ( factor < .05 ) factor = 0;
		
		String percentage = String.valueOf((int)(factor * 100));
		int idx = percentage.indexOf('.');
		if ( idx > -1 ) percentage = percentage.substring(0, idx);
		
		String fct;
		if ( percentage == "100" ) fct = "1";
		else if ( percentage.length() == 1 ) fct = ".0" + percentage;
		else fct = "." + percentage;
		
		Element h = e.getElement();
		DOM.setStyleAttribute(h, "filter", "alpha(opacity=" + percentage + ")");
		DOM.setStyleAttribute(h, "opacity", fct);
		//DOM.setStyleAttribute(h, "-moz-opacity", fct);
	}
	
	public static void setClickPointer(UIObject e, boolean set) {
		DOM.setStyleAttribute(e.getElement(), "cursor", set ? "pointer" : "default");
	}
	
	public static void setClickPointer(Element e, boolean set) {
		DOM.setStyleAttribute(e, "cursor", set ? "pointer" : "default");
	}
	
	public static native int bodyScrollTop() /*-{
		return $doc.body.scrollTop;
	}-*/;
	
	public static native int bodyScrollLeft() /*-{
		return $doc.body.scrollLeft;
	}-*/;
	
	public static String[] getStyleSheetURLs() {
		List<Object> urls = new ArrayList<Object>();
		loadStyleSheetURLs0(urls);
		
		String[] ret = new String[urls.size()];
		
		for ( int i = 0 ; i < ret.length ; i++ ) ret[i] = urls.get(i).toString();
		
		return ret;
	}
	
	private static native void loadStyleSheetURLs0(List<Object> list) /*-{
		var ssa = $doc.styleSheets;
		
		var i = 0;
		while ( i < ssa.length ) {
			list.@java.util.List::add(Ljava/lang/Object;)(ssa[i].href);
			i++;
		}
	}-*/;
	
	public static String getUrlBase() {
		String url = getUrlBase0();
		int idx = url.lastIndexOf('/');
		return url.substring(0, idx +1);
	}
	
	private static native String getUrlBase0() /*-{
		return $doc.location;
	}-*/;
	
	public static native String urlDecode(String toDecode) /*-{
		return decodeURI(toDecode);
	}-*/;
	
	public static native String urlEncode(String toEncode) /*-{
		return encodeURI(toEncode);
	}-*/;
	
	public static native int getTabIndex(Element elem) /*-{
		return elem.tabIndex;
	}-*/;
	
	public static native String getUserAgent() /*-{
		var ua = navigator.userAgent.toLowerCase();
		if ( ua.indexOf('opera') != -1 ) return 'opera';
		if ( ua.indexOf('webkit' ) != -1 ) return 'safari';
		if ( ua.indexOf('msie 6.0') != -1 ) return 'ie6';
		if ( ua.indexOf('msie 7.0') != -1 ) return 'ie7';
		if ( ua.indexOf('gecko') != -1 ) return 'gecko';
		return 'unknown';
	}-*/;
	
	public static String REPEAT = "repeat";
	public static String REPEAT_X = "repeat-x";
	public static String REPEAT_Y = "repeat-y";
	public static String NO_REPEAT = "no-repeat";
	
	public static void setBackgroundImage(UIObject element, String url, String repeat) {
		DOM.setStyleAttribute(element.getElement(), "backgroundImage", "url(" + url + ")");
		DOM.setStyleAttribute(element.getElement(), "backgroundRepeat", repeat);
	}
}
