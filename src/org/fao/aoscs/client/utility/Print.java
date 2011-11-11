package org.fao.aoscs.client.utility;

import org.fao.aoscs.client.locale.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.UIObject;


public class Print { 

	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
    public static native void it() /*-{ 
        $wnd.print(); 
    }-*/; 


    public static native void buildFrame(String html) /*-{ 
        var frame = $doc.getElementById('__gwt_historyFrame'); 
        if (!frame) { 
            $wnd.alert("Error: Can't find printing frame."); 
            return; 
        } 
        var doc = frame.contentWindow.document; 
        doc.open(); 
        doc.write(html); 
        doc.close(); 


    }-*/; 


    public static native void printFrame() /*-{ 
        var frame = $doc.getElementById('__gwt_historyFrame'); 
        frame = frame.contentWindow; 
        frame.focus(); 
        frame.print(); 
    }-*/; 


    public static class PrintFrame implements Command { 
        public void execute() { 
        	try
        	{
        		printFrame();
        	}
        	catch(Exception e)
        	{
        		
        	}
        } 
    } 
    public static PrintFrame printFrameCommmand = new PrintFrame(); 


    public static void it(String html) { 
        try { 
            buildFrame(html); 
            Scheduler.get().scheduleDeferred(printFrameCommmand); 
        } catch (Throwable exc) { 
            Window.alert(constants.printFailed()); 
        } 
    } 
    public static void it(UIObject obj) { 
        it("", obj.getElement().toString()); 
    } 


    public static void it(Element element) { 
        it("", element.toString()); 
    } 


    public static void it(String style, String it) { 
        it("<html><head>"+style+"</head>\n<body>"+it+"</body></html>"); 
    } 


    public static void it(String style, UIObject obj) { 
        it(style, obj.getElement().toString()); 
    } 


    public static void it(String style, Element element) { 
        it(style, element.toString()); 
    } 
    
    public static void dump(String msg){
        GWT.log(msg, null);        
    }
}

 



