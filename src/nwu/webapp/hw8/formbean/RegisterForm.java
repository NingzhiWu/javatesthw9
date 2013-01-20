/*Name: Ningzhi Wu*/
/*Course No.: 08-600 Java&J2EE Programming*/
/*Homework #8*/
/*Date: 11/28/2012*/
package nwu.webapp.hw8.formbean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class RegisterForm  {
    private String firstName;
    private String lastName;
    private String confirmPassword;
    private String button;
	
    public RegisterForm(HttpServletRequest request) {
    	firstName = request.getParameter("firstName");
    	lastName = request.getParameter("lastName");
    	confirmPassword = request.getParameter("confirmPassword");
    	button   = request.getParameter("button");
    }
    
    public String getFirstName()          { return firstName; }
    public String getLastName()           { return lastName;  }
    public String getConfirmPassword()    { return confirmPassword; }
    public String getButton()             { return button;   }
    
    public boolean isPresent()            { return button != null; }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (firstName == null || firstName.length() == 0) 
        	errors.add("First name is required");
        
        if (lastName == null || lastName.length() == 0) 
        	errors.add("Last name is required");
        
        if (confirmPassword == null || confirmPassword.length() == 0) 
        	errors.add("Password is required");
        
        if (button == null) 
        	errors.add("Button is required");

        if (errors.size() > 0) return errors;

        if (!button.equals("Complete Registration")) 
        	errors.add("Invalid button");
		
        return errors;
    }
}

