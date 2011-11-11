package org.fao.aoscs.client.module.search.widgetlib;

import com.google.gwt.user.client.ui.SuggestOracle.Response;

public class AOSSuggestOracleResponse extends Response{

	public long timestamp;
	
	public AOSSuggestOracleResponse() {
		super();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
