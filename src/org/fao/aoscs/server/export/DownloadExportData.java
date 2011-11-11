package org.fao.aoscs.server.export;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadExportData extends HttpServlet{

	private static final long serialVersionUID = -5989419573348960057L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String data = "No Export Data";
			String filename = request.getParameter("filename")==null?"export.txt":request.getParameter("filename");
			String key = request.getParameter("key")==null?"":request.getParameter("key");
			if(!key.equals(""))
			{
				try
				{
					data = (String) request.getSession().getAttribute(key);
					//request.getSession().removeAttribute(key);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			ServletOutputStream op = response.getOutputStream(); 
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");  
            op.write(data.getBytes("UTF-8"));
            op.flush(); 
            op.close(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			doGet(request, response); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
