package edu.umbc.ultra.dbase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import edu.umbc.ultra.logic.User;

public class RightsManagementController
{
	public RightsManagementController()
	{
		
	}
	
	public User getUser(String email)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		try
		{
			Entity userEntity = datastore.get(KeyFactory.createKey("User", email));
			User newUser = new User(email,User.getPrivilegeLevelFromString((String)userEntity.getProperty("Privilege")),(java.util.Date)userEntity.getProperty("Registered"), (String)userEntity.getProperty("FirstName"), (String)userEntity.getProperty("LastName"));
			return newUser;
		}
		catch (EntityNotFoundException e)
		{
			return null;
		}
	}
	
	// Returns false if user already exists, true otherwise
	public boolean addUser(User user)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// See if this user exists in the dbase already...
		User userFromDBase = getUser(user.getGoogleUser());
		if(userFromDBase != null)
		{
			return false;
		}
		
		// Create root entity with a key specifier created from the user's email
		Entity userEntity = new Entity("User", user.getGoogleUser());
		
		userEntity.setProperty("Registered", user.getRegisteredDate());
		userEntity.setProperty("Privilege", User.getPrivilegeLevelAsString(user.getPrivilegeLevel()));
		userEntity.setProperty("FirstName", user.getFirstName());
		userEntity.setProperty("LastName", user.getLastName());
		
		try
		{
			datastore.put(userEntity);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
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