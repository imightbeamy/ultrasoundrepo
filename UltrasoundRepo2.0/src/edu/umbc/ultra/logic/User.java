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
	
	public User(com.google.appengine.api.users.User googleUser, PrivilegeLevel privelegeLevel, Date registeredDate)
	{
		this.googleUser = googleUser;
		this.privilegeLevel = privilegeLevel;
		this.registeredDate = registeredDate;
	}
	
	public User(){}
	
	public com.google.appengine.api.users.User getGoogleUser()
	{
		return googleUser;
	}
	public PrivilegeLevel getPrivelegeLevel()
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
}
