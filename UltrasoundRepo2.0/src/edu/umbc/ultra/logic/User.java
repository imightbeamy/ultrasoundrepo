package edu.umbc.ultra.logic;

import java.util.Date;

public class User
{
	public static enum PrivilegeLevel {PENDING, RESIDENT, ATTENDING}
	
	/* Using this may get cumbersome, perhaps we should rename our class to something else?
	 *  Or possibly, just make this an inherited class from theirs...though from a design
	 *  perspective that may not be a good idea.
	 */
	private com.google.appengine.api.users.User googleUser;
	private PrivilegeLevel privilegeLevel;
	private Date registeredDate;
	
	public User(com.google.appengine.api.users.User googleUser, PrivilegeLevel privilegeLevel, Date registeredDate)
	{
		this.googleUser = googleUser;
		this.privilegeLevel = privilegeLevel;
		this.registeredDate = registeredDate;
	}
	
	//This is for testing TODO: remove
	public User(){}
	
	public com.google.appengine.api.users.User getGoogleUser()
	{
		return googleUser;
	}
	public PrivilegeLevel getPrivilegeLevel()
	{
		return privilegeLevel;
	}
	public Date getRegisteredDate()
	{
		return registeredDate;
	}
	public String getUserID() {
		//get id from googleUser this is for testing
		return "thisisanid";
	}
	public static User findUser(String userEmail) {
		/*
		User user = null;
		for(int i = 0; i < dataStore.userCount; i++) {
			if(dataStore.Users[i].googleUser.equals(userEmail)) {
				user = dataStore.Users[i];
				return user;
			}
		}
		return user;
		*/
		return new User(null, PrivilegeLevel.ATTENDING, new Date());
	}
	public String toString() {
		return this.privilegeLevel.toString();
		//return u1.privilegeLevel.toString();
	}
}
