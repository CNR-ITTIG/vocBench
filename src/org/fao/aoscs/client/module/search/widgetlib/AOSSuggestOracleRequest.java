package org.fao.aoscs.client.module.search.widgetlib;

import com.google.gwt.user.client.ui.SuggestOracle.Request;

public class AOSSuggestOracleRequest extends Request{

	public long timestamp;
	
	public AOSSuggestOracleRequest() {
		super();
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
