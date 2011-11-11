package org.fao.aoscs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MenuLabel extends Composite {

	private HTML l;
	public boolean active = false;
	public boolean disable = false;
	public String module;
	
	public MenuLabel(String label, String title, final String module, final MainApp mainAPP) 
	{
		this.module = module; 
		l = new HTML(label);			
		l.setTitle(title);
		l.setWordWrap(false);
		DOM.setStyleAttribute(l.getElement(), "fontSize", "11px");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHeight("100%");		
		hp.add(l);
		hp.setCellVerticalAlignment(l, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellHeight(l,"100%");
				
		l.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				if(!disable)
				{
					activateStyle();
				}
			}
		});
		l.addMouseOutHandler(new MouseOutHandler() {
			
			public void onMouseOut(MouseOutEvent event) {
				if(!disable && !active)
				{
					deactivateStyle();
				}
			}			
		});
		l.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(disable)
				{
					disableTextSelection(l.getElement(), true);
				}
				else
				{
			    //if(!MainApp.dataChanged){
					mainAPP.iconContainer.deactivate();
    				activate();				
    				mainAPP.goToModule(module);
			    /*}else{
			        FormDialogBox fdb = new FormDialogBox("Save my changes","Don't save");
			        fdb.setWidth("440px");			        			       
			        fdb.addWidget(new HTML("Do you want to save your changes?"));
			        fdb.setText("Save before exit?");
			        fdb.show();
			        fdb.submit.addClickHandler(new ClickHandler()
			        {   public void onClick(ClickEvent event)
                        {
                            mainAPP.saveCurrentModule();
                            MainApp.dataChanged = false;
                            MainApp.iconContainer.deactivate();
                            activate();             
                            mainAPP.goToModule(module);      
                        }
                    });			        
			        fdb.cancel.addClickHandler(new ClickHandler()
			        {
                        public void onClick(ClickEvent event)
                        {
                            MainApp.dataChanged = false;
                            MainApp.iconContainer.deactivate();
                            activate();             
                            mainAPP.goToModule(module);                      
                        }
                    });
			    }*/
				}
			}
		});		
		this.initWidget(hp);
		l.setStyleName("menu-label-deactive");
	}
	
	public void activate(){
		active = true;
		disable = false;
		activateStyle();		
	}
	
	public void deactivate(){
		active = false;
		disable = false;
		deactivateStyle();
	}
	
	public void disable(){
		disable = true;
		disableStyle();
	}
	
	private void activateStyle()
	{
		l.setStyleName("menu-label-active");
	}
	
	private void deactivateStyle()
	{
		l.setStyleName("menu-label-deactive");
	}
	
	private void disableStyle()
	{
		l.setStyleName("menu-label-disable");
	}

	
	/** 
	   * Enables or disables text selection for the element. A circular reference 
	   * will be created when disabling text selection. Disabling should be cleared 
	   * when the element is detached. See the <code>Component</code> source for 
	   * an example. 
	   * 
	   * @param elem the element 
	   * @param disable <code>true</code> to disable 
	   */ 
	
	  public static void disableTextSelection(Element elem, boolean disable) { 
	    setStyleName(elem, "my-no-selection", disable); 
	    disableTextSelectInternal(elem, disable); 
	  } 
	  
	  private native static void disableTextSelectInternal(Element e, boolean disable)/*-{ 
	      if (disable) { 
	        e.ondrag = function () { return false; }; 
	        e.onselectstart = function () { return false; }; 
	      } else { 
	        e.ondrag = null; 
	        e.onselectstart = null; 
	      } 
	    }-*/; 

}
