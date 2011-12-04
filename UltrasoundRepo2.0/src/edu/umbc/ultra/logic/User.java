package edu.umbc.ultra.logic;

import java.util.Date;

public class User {
	public static enum PrivilegeLevel {
		PENDING, RESIDENT, ATTENDING
	}

	private String googleUser;
	private PrivilegeLevel privilegeLevel;
	private Date registeredDate;
	private String firstName;
	private String lastName;

	public User(String googleUser, PrivilegeLevel privilegeLevel, Date date,
			String firstName, String lastName) {
		this.googleUser = googleUser;
		this.privilegeLevel = privilegeLevel;
		this.registeredDate = date;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// This is for testing TODO: remove
	public User() {
	}

	public String getGoogleUser() {
		return googleUser;
	}

	public PrivilegeLevel getPrivilegeLevel() {
		return privilegeLevel;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public String getFullName() {
		return firstName + " " + lastName;
	}

	
	public static User findUser(String userEmail) {
		/*
		 * User user = null; for(int i = 0; i < dataStore.userCount; i++) {
		 * if(dataStore.Users[i].googleUser.equals(userEmail)) { user =
		 * dataStore.Users[i]; return user; } } return user;
		 */
		return new User(null, PrivilegeLevel.ATTENDING, new Date(), "?", "??");
	}

	public String toString() {
		return this.privilegeLevel.toString();
		// return u1.privilegeLevel.toString();
	}

	public static PrivilegeLevel getPrivilegeLevelFromString(String pLevel) {
		if (pLevel.equalsIgnoreCase("ATTENDING")) {
			return PrivilegeLevel.ATTENDING;
		} 
		else if (pLevel.equalsIgnoreCase("RESIDENT")) {
			return PrivilegeLevel.RESIDENT;
		} 
		else if (pLevel.equalsIgnoreCase("PENDING")) {
			return PrivilegeLevel.PENDING;
		} 
		else {
			return null;
		}
	}
}
