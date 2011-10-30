package edu.umbc.ultra.logic;

import java.util.Date;

public class User
{
	public static enum PrivelegeLevel {PENDING, RESIDENT, ATTENDING}
	
	/* Using this may get cumbersome, perhaps we should rename our class to something else?
	 *  Or possibly, just make this an inherited class from theirs...though from a design
	 *  perspective that may not be a good idea.
	 */
	private com.google.appengine.api.users.User googleUser;
	private PrivelegeLevel privelegeLevel;
	private Date registeredDate;
	
	public User(com.google.appengine.api.users.User googleUser, PrivelegeLevel privelegeLevel, Date registeredDate)
	{
		this.googleUser = googleUser;
		this.privelegeLevel = privelegeLevel;
		this.registeredDate = registeredDate;
	}
	
	public com.google.appengine.api.users.User getGoogleUser()
	{
		return googleUser;
	}
	public PrivelegeLevel getPrivelegeLevel()
	{
		return privelegeLevel;
	}
	public Date getRegisteredDate()
	{
		return registeredDate;
	}
}
