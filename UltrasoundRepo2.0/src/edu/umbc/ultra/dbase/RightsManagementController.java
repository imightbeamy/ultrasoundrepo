package edu.umbc.ultra.dbase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import edu.umbc.ultra.logic.User;

public class RightsManagementController
{
	public RightsManagementController()
	{
		
	}
	
	public User.PrivilegeLevel getUserPrivelegeLevel(String email)
	{
		return null;
	}
	
	public boolean addUser(User user)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Create root entity with a key specifier created from the user's email
		Entity userEntity = new Entity("User", user.getGoogleUser().getEmail());
		userEntity.setProperty("Registered", user.getRegisteredDate());
		userEntity.setProperty("Privelege", user.getPrivilegeLevel().toString()); // This may not work
		
		
		
		return false;
	}
	
	
	/* Shameful shameful singleton code */
	private static RightsManagementController instance;
	
	public static RightsManagementController getInstance()
	{
		if(instance == null)
		{
			instance = new RightsManagementController();
		}
		
		return instance;
	}
}