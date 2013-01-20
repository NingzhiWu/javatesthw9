/*Name: Ningzhi Wu*/
/*Course No.: 08-600 Java&J2EE Programming*/
/*Homework #8*/
/*Date: 11/28/2012*/
package nwu.webapp.hw8.formbean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class FavoriteForm {
	private String url;
	private String commentText;
	private String favoriteId;

	private String button;

	
	public FavoriteForm(HttpServletRequest request) {
		url = request.getParameter("url");
		commentText = request.getParameter("commentText");
		button = request.getParameter("favoritebutton");
		favoriteId = request.getParameter("favoriteId");
	}
	
	public String getUrl()      { return url; }
	public String getCommentText() { return commentText;}
	public String getFavoriteId() {
		return favoriteId;
	}
	public String getButton() {
		return button;
	}

	public boolean isPresent()  { return button != null;}
	
	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if(button != null && button.equals("Add Favorite")) {
			if (url == null || url.length() == 0) {
				errors.add("Url is required");
			}
		}
		
		return errors;
	}

}
