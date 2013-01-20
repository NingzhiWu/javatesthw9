/*Name: Ningzhi Wu*/
/*Course No.: 08-600 Java&J2EE Programming*/
/*Homework #8*/
/*Date: 11/28/2012*/

package nwu.webapp.hw8;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import nwu.webapp.hw8.dao.*;
import nwu.webapp.hw8.databean.*;
import nwu.webapp.hw8.formbean.*;


public class hw8 extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
		
		private FavoriteDAO favoriteDAO;
		private UserDAO userDAO;
		
		

		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        HttpSession session = request.getSession();
	        if (session.getAttribute("user") == null) {
	        	login(request,response);
	        } else {
	        	addFavorite(request,response,(UserBean)session.getAttribute("user"));
	        }
	    }
	    
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	doGet(request,response);
	    }

	    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   		List<String> errors = new ArrayList<String>();
	   		
	   		LoginForm loginForm = new LoginForm(request);
	   		RegisterForm registerForm = new RegisterForm(request);
	    	
	    	if (!loginForm.isPresent()) {
	    		outputLoginPage(response,loginForm,null);
	    		return;
	    	}
	    	
	   		errors.addAll(loginForm.getValidationErrors());
	       	if (errors.size() != 0) {
	    		outputLoginPage(response,loginForm,errors);
	    		return;
	    	}

	        try {
	            UserBean user;

	       		if (loginForm.getButton().equals("Register")) {
	       			user = userDAO.lookup(loginForm.getEmail());
	       			if(user != null) {
	       				errors.add("Exsiting user");
	       				outputLoginPage(response,loginForm,errors);
	       				return;
	       			}
	       			else {
	       				outputRegisterPage(response,loginForm,registerForm,errors);
	       				return;
	       			}
	       		} else if(loginForm.getButton().equals("Login")){
			       	user = userDAO.lookup(loginForm.getEmail());
			       	if (user == null) {
			       		errors.add("No such user");
			    		outputLoginPage(response,loginForm,errors);
			    		return;
			       	}
			       	
			       	if (!loginForm.getPassword().equals(user.getPassword())) {
			       		errors.add("Incorrect password");
			    		outputLoginPage(response,loginForm,errors);
			    		return;
			       	}
	       		} else {
	       			errors.addAll(registerForm.getValidationErrors());
	       	       	if (errors.size() != 0) {
	       	    		outputRegisterPage(response,loginForm,registerForm,errors);
	       	    		return;
	       	    	}
	       			if(registerForm.getConfirmPassword().equals(loginForm.getPassword())) {
	       				user = new UserBean();
	       				user.setEmail(loginForm.getEmail());
	       				user.setPassword(loginForm.getPassword());
	       				user.setFirstName(registerForm.getFirstName());
	       				user.setLastName(registerForm.getLastName());
	       				userDAO.create(user);
	       				user = userDAO.lookup(user.getEmail());
	       			}
	       			else {
	       				errors.add("Incorrect password");
	       				outputRegisterPage(response,loginForm,registerForm,errors);
	       				return;
	       			}
	       		}
		    	
		       	HttpSession session = request.getSession();
		       	session.setAttribute("user",user);
		       	addFavorite(request,response,user);
	       	} catch (MyDAOException e) {
	       		errors.add(e.getMessage());
	       		outputLoginPage(response,loginForm,errors);
	       	}
		}


	    private void addFavorite(HttpServletRequest request, HttpServletResponse response,UserBean user) throws ServletException, IOException {
	   		List<String> errors = new ArrayList<String>();
	   		
	   		FavoriteForm favoriteForm = new FavoriteForm(request);

	    	if (!favoriteForm.isPresent() && favoriteForm.getFavoriteId() == null) {
	    		outputList(response,favoriteForm,null,user);
	    		return;
	    	}
	    	
	   		errors.addAll(favoriteForm.getValidationErrors());
	       	if (errors.size() != 0) {
	    		outputList(response,favoriteForm,errors,user);
	    		return;
	    	}

	        try {
	        	FavoriteBean favoriteBean;
	            if(favoriteForm.isPresent() && favoriteForm.getButton().equals("Add Favorite")) {
	            	favoriteBean = new FavoriteBean();
	            	favoriteBean.setUserId(user.getUserId());
	            	favoriteBean.setUrl(favoriteForm.getUrl());
	            	favoriteBean.setCommentText(favoriteForm.getCommentText());
	            	favoriteBean.setClickCount(0);
	            	favoriteDAO.create(favoriteBean);
	            	outputList(response,favoriteForm,errors,user);
	            } else if(favoriteForm.isPresent() && favoriteForm.getButton().equals("Logout")) {
	            	HttpSession session = request.getSession();
	    	       	session.removeAttribute("user");
	    	       	login(request,response);
	            }
	            else {
	            	favoriteDAO.count(Integer.parseInt(favoriteForm.getFavoriteId()));
	            	outputList(response,favoriteForm,errors,user);
	            }

	       	} catch (MyDAOException e) {
	       		errors.add(e.getMessage());
	       		outputList(response,favoriteForm,errors,user);
	       	}
		}
	    
	    // Methods that generate & output HTML
	    
	    private void generateHead(PrintWriter out) {
		    out.println("  <head>");
		    out.println("    <meta http-equiv=\"cache-control\" content=\"no-cache\">");
		    out.println("    <meta http-equiv=\"pragma\" content=\"no-cache\">");
		    out.println("    <meta http-equiv=\"expires\" content=\"0\">");
		    out.println("    <title>HW8</title>");
		    out.println("  </head>");
	    }
	    
	    private void outputLoginPage(HttpServletResponse response, LoginForm form, List<String> errors) throws IOException {
			response.setContentType("text/html");
		    PrintWriter out = response.getWriter();
		
		    out.println("<html>");
		
		    generateHead(out);
		
		    out.println("<body>");
		    out.println("<h2>HW8</h2>");
		    
		    if (errors != null && errors.size() > 0) {
		    	for (String error : errors) {
		        	out.println("<p style=\"font-size: large; color: red\">");
		        	out.println(error);
		        	out.println("</p>");
		    	}
		    }
		
		    // Generate an HTML <form> to get data from the user
	        out.println("<form method=\"POST\">");
	        out.println("    <table/>");
	        out.println("        <tr>");
	        out.println("            <td style=\"font-size: large\">E-mail Address:</td>");
	        out.println("            <td>");
	        out.println("                <input type=\"text\" name=\"email\"");
	        if (form != null && form.getEmail() != null) {
	        	out.println("                    value=\""+form.getEmail()+"\"");
	        }
	        out.println("                />");
	        out.println("            <td>");
	        out.println("        </tr>");
	        out.println("        <tr>");
	        out.println("            <td style=\"font-size: large\">Password:</td>");
	        out.println("            <td><input type=\"password\" name=\"password\" /></td>");
	        out.println("        </tr>");
	        out.println("        <tr>");
	        out.println("            <td colspan=\"2\" align=\"center\">");
	        out.println("                <input type=\"submit\" name=\"button\" value=\"Login\" />");
	        out.println("                <input type=\"submit\" name=\"button\" value=\"Register\" />");
	        out.println("            </td>");
	        out.println("        </tr>");
	        out.println("    </table>");
	        out.println("</form>");
	        out.println("</body>");
	        out.println("</html>");
		}
	    
	    private void outputRegisterPage(HttpServletResponse response, LoginForm loginForm, RegisterForm registerForm, List<String> errors) throws IOException {
			response.setContentType("text/html");
		    PrintWriter out = response.getWriter();
		
		    out.println("<html>");
		
		    generateHead(out);
		
		    out.println("<body>");
		    out.println("<h2>Complete Registration</h2>");
		    
		    if (errors != null && errors.size() > 0) {
		    	for (String error : errors) {
		        	out.println("<p style=\"font-size: large; color: red\">");
		        	out.println(error);
		        	out.println("</p>");
		    	}
		    }
		
		    // Generate an HTML <form> to get data from the user
	        out.println("<form method=\"POST\">");
	        out.println("                <input type=\"hidden\" name=\"email\"");
	        out.println("                    value=\""+loginForm.getEmail()+"\"");
	        out.println("                />");
	        out.println("                <input type=\"hidden\" name=\"password\"");
	        out.println("                    value=\""+loginForm.getPassword()+"\"");
	        out.println("                />");
	        out.println("    <table/>");
	        out.println("        <tr>");
	        out.println("            <td style=\"font-size: x-large\">First Name:</td>");
	        out.println("            <td>");
	        out.println("                <input type=\"text\" name=\"firstName\"");
	        if (registerForm != null && registerForm.getFirstName() != null) {
	        	out.println("                    value=\""+registerForm.getFirstName()+"\"");
	        }
	        out.println("                />");
	        out.println("            <td>");
	        out.println("        </tr>");
	        out.println("        <tr>");
	        out.println("            <td style=\"font-size: x-large\">Last Name:</td>");
	        out.println("            <td>");
	        out.println("                <input type=\"text\" name=\"lastName\"");
	        if (registerForm != null && registerForm.getLastName() != null) {
	        	out.println("                    value=\""+registerForm.getLastName()+"\"");
	        }
	        out.println("                />");
	        out.println("            <td>");
	        out.println("        </tr>");
	        out.println("        <tr>");
	        out.println("            <td style=\"font-size: x-large\">Confirm Password:</td>");
	        out.println("            <td><input type=\"password\" name=\"confirmPassword\" /></td>");
	        out.println("        </tr>");
	        out.println("        <tr>");
	        out.println("            <td colspan=\"1\" align=\"center\">");
	        out.println("                <input type=\"submit\" name=\"button\" value=\"Complete Registration\" />");
	        out.println("            </td>");
	        out.println("        </tr>");
	        out.println("    </table>");
	        out.println("</form>");
	        out.println("</body>");
	        out.println("</html>");
		}
	  
	    private void outputList(HttpServletResponse response,FavoriteForm form,List<String> messages,UserBean user) throws IOException {
	    	// Get the list of items to display at the end
	    	FavoriteBean[] favorite;
	        try {
	        	favorite = favoriteDAO.getUserFavorites(user.getUserId());
	        	
	        } catch (MyDAOException e) {
	        	// If there's an access error, add the message to our list of messages
	        	messages.add(e.getMessage());
	        	favorite = new FavoriteBean[0];
	        }
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        out.println("<html>");

	        generateHead(out);

	        out.println("<body>");
	        out.println("<h2>Favorites for " + user.getFirstName() + " " + user.getLastName());
	        out.println("</h2>");

	        // Generate an HTML <form> to get data from the user
	        out.println("<form method=\"POST\">");
	        out.println("    <table>");
	        out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
	        out.println("        <tr>");
	        out.println("            <td style=\"font-size: large\">URL:</td>");
	        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"url\"/></td>");
	        out.println("        </tr>");
	        out.println("        <tr>");
	        out.println("            <td style=\"font-size: large\">Comment:</td>");
	        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"commentText\"/></td>");
	        out.println("        </tr>");
	        out.println("        <tr>");
	        out.println("            <td/>");
	        out.println("            <td colspan=\"2\"><input type=\"submit\" name=\"favoritebutton\" value=\"Add Favorite\"/>");
	        out.println("                              <input type=\"submit\" name=\"favoritebutton\" value=\"Logout\"/>");
	        out.println("            </td>");
	        out.println("        </tr>");
	        out.println("    </table>");
	        out.println("</form>");

	        if(messages != null) {
	        	for (String message : messages) {
	            	out.println("<p style=\"font-size: large; color: red\">");
	            	out.println(message);
	            	out.println("</p>");
	            }
	        }
	 
	        out.println("<p style=\"font-size: large\">Total Favorites : "+favorite.length+"</p>");
	        out.println("<table>");
	        if(favorite != null) {
	    		for(int i=0;i<favorite.length;i++) {
	        		out.println("<table>");
	        		out.println("  <tr>");
	        		out.println("    <td style=\"font-size: large\">URL:</td>");
	        		out.println("    <td><a href=\"nwu?favoriteId=" + favorite[i].getFavoriteId() + "\">" + favorite[i].getUrl() + "</a></td>");
	        		out.println("  </tr>");
	        		out.println("  <tr>");
	        		out.println("    <td style=\"font-size: large\">Comment:</td>");
	        		out.println("    <td>" + favorite[i].getCommentText() + "</td>");
	        		out.println("  </tr>");
	        		out.println("  <tr>");
	        		out.println("    <td style=\"font-size: large\">Clicks:</td>");
	        		out.println("    <td>" + favorite[i].getClickCount() + "</td>");
	        		out.println("  </tr>");
	        		out.println("</table>");
	        		out.println("</br>");
	    	    }
	    	}

	        out.println("</table>");

	        out.println("</body>");
	        out.println("</html>");
	    }
	    
}

