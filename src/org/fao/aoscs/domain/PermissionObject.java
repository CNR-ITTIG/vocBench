package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Iterator;

public class PermissionObject extends ArrayList<PermissionFunctionalityMap>{

	private static final long serialVersionUID = -8016679812084695183L;

	public PermissionObject() {
		super();
	}
	
	/**
	 * Check whether permission with given action and status exists
	 * @param actionId	Action identifier
	 * @param status	Status identifier. If -1, function does not consider status value
	 * @return
	 */
	public boolean contains(int actionId, int status){
		boolean ret = false;
		Iterator<PermissionFunctionalityMap> itr = iterator();
		while(itr.hasNext()){
			PermissionFunctionalityMap map = itr.next();
			if((status == -1 && map.getFunctionId() == actionId) || (map.getFunctionId() == actionId && map.getStatus() == status)){
				ret = true;
				break;
			}
		}
		return ret;
	}
	
}
