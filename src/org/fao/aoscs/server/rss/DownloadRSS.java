package org.fao.aoscs.server.rss;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadRSS extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String feedType = request.getParameter("type")==null?"rss_2.0":request.getParameter("type");
			String ontologyId = request.getParameter("ontologyId")==null?"0":request.getParameter("ontologyId");
			String rcid = request.getParameter("rcid")==null?"0":request.getParameter("rcid");
			String data = new GenerateRSS().getRSSText(feedType, ontologyId, rcid);
			String filename = "rss.xml"; 
			ServletOutputStream op = response.getOutputStream(); 
			response.setHeader("Content-Disposition", "inline; filename=" + filename);
			response.setContentType("application/rss+xml"); 
			
			op.write(data.getBytes("UTF-8"));
			op.flush(); 
			op.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
