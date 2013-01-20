/*Name: Ningzhi Wu*/
/*Course No.: 08-600 Java&J2EE Programming*/
/*Homework #8*/
/*Date: 11/28/2012*/
package nwu.webapp.hw8.databean;

public class UserBean {
	private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public int getUserId()             { return userId;    }
    public String getEmail()           { return email;     }
    public String getPassword()        { return password;  }
    public String getFirstName()       { return firstName; }
    public String getLastName()        { return lastName;  }
    
    public void setUserId(int i)       { userId = i;       }
    public void setPassword(String s)  { password = s;     }
    public void setEmail(String s)     { email = s;        }
    public void setFirstName(String s) { firstName = s;    }
    public void setLastName(String s)  { lastName = s;     }
    
}
