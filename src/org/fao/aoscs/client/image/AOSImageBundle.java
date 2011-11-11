package org.fao.aoscs.client.image;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface AOSImageBundle extends ImageBundle {

	@Resource("org/fao/aoscs/client/image/icons/label_not_found.gif")
	public AbstractImagePrototype labelNotFound();
	  
	@Resource("org/fao/aoscs/client/image/icons/concept_logo.gif")
	public AbstractImagePrototype conceptIcon();

	@Resource("org/fao/aoscs/client/image/icons/category_logo.gif")
	public AbstractImagePrototype categoryIcon();
	
	@Resource("org/fao/aoscs/client/image/icons/add-grey.gif")
	public AbstractImagePrototype addIcon();
	
	@Resource("org/fao/aoscs/client/image/icons/edit-grey.gif")
	public AbstractImagePrototype editIcon();
	
	@Resource("org/fao/aoscs/client/image/icons/delete-grey.gif")
	public AbstractImagePrototype deleteIcon();
	
	@Resource("org/fao/aoscs/client/image/icons/wiki.gif")
	public AbstractImagePrototype wikiIcon();
}
