package org.fao.aoscs.client.module.comment.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.UserComments;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("comment")
public interface CommentService extends RemoteService{
		
	public ArrayList<UserComments> getComments(String module); 
	public String sendComment(UserComments uc);
		
	public static class CommentServiceUtil{
		private static CommentServiceAsync instance;
		public static CommentServiceAsync getInstance()
		{
			if (instance == null) {
				instance = (CommentServiceAsync) GWT.create(CommentService.class);
			}
			return instance;
		}
	}
}
