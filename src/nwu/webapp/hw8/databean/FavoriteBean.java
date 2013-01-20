/*Name: Ningzhi Wu*/
/*Course No.: 08-600 Java&J2EE Programming*/
/*Homework #8*/
/*Date: 11/28/2012*/
package nwu.webapp.hw8.databean;

public class FavoriteBean {
	private int userId;
	private int favoriteId; 
	private String url;
	private String commentText;
	private int clickCount;
	
	public int    getUserId()        { return userId;       }
    public int    getFavoriteId()    { return favoriteId;   }
    public String getUrl()           { return url;          }
    public String getCommentText()   { return commentText;  }
    public int    getClickCount()    { return clickCount;   }
    

	public void setUserId(int i)         { userId = i;          }
	public void setFavoriteId(int i)     { favoriteId = i;      }
	public void setUrl(String s)         { url = s;             }
	public void setCommentText(String s) { commentText = s;     }
	public void setClickCount(int i)     { clickCount = i;      }  
	
}
