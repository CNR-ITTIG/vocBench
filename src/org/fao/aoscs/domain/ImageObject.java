package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ImageObject extends LightEntity{

	private static final long serialVersionUID = 8346833454953079017L;
	
	private HashMap<String, IDObject> imageList = new HashMap<String, IDObject>();
	
	public void setImageList(HashMap<String, IDObject> imageList) {
		this.imageList = imageList;
	}
	
	public HashMap<String, IDObject> getImageList() {
		return imageList;
	}
	 
	public ArrayList<IDObject> getImageListOnly() {
		ArrayList<IDObject> list = new ArrayList<IDObject>();
		Iterator<String> it = imageList.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			IDObject dObj = (IDObject) imageList.get(key);
			list.add(dObj);
		}
		return list;
	}
	
	public void addImageList(String ImageIns,IDObject dObj) {
		if(!imageList.containsKey(ImageIns)){
			this.imageList.put(ImageIns, dObj);
		}
	}
	
	public boolean hasImage(String defIns){
		if(this.imageList.containsKey(defIns)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isEmpty(){
		if(imageList==null) 
			return false;
		else
			return imageList.isEmpty();
	}
	
	public int getImageCount(){
		if(imageList==null) 
			return 0;
		else
			return imageList.size();
	}
	
	public IDObject getImage(String defIns){
		if(imageList.containsKey(defIns)){
			return (IDObject)imageList.get(defIns);
		}else{
			return null;
		}
	}
	

}
