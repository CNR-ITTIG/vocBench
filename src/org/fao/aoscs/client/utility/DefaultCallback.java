package org.fao.aoscs.client.utility;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class DefaultCallback implements AsyncCallback<Object>
{
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
	 */
	public void onFailure(Throwable caught)
	{
	//	Message to display
	//
		String message = caught.getMessage();
		if ((message == null) ||
			(message.length() == 0))
		{
			message = caught.toString();
		}
		
	//	Display from the top application
	//
		Window.alert(message);
	}
}
