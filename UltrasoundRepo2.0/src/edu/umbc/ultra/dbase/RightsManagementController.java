package edu.umbc.ultra.dbase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import edu.umbc.ultra.logic.User;

public class RightsManagementController {
	public RightsManagementController() {

	}

	public User.PrivilegeLevel getPrivilegeLevel(String email) {
		User user = getUser(email);
		if (user != null) {
			return user.getPrivilegeLevel();
		}
		return null;
	}

	public User getUser(String email) {
		// Get an instance of the data store controller
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		try {
			// Attempt to get the user entity from the database matching the
			// passed in email address
			Entity userEntity = datastore.get(KeyFactory.createKey("User",
					email));

			// Populate a user object with the resulting entity properties
			User newUser = new User(email,
					User.getPrivilegeLevelFromString((String) userEntity
							.getProperty("Privilege")),
					(java.util.Date) userEntity.getProperty("Registered"),
					(String) userEntity.getProperty("FirstName"),
					(String) userEntity.getProperty("LastName"));
			return newUser;
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public boolean changePrivilegeLevel(String email,
			User.PrivilegeLevel privileges) {
		User user = getUser(email);
		if (user != null) {
			// If the user exists, overwrite it in the database with the updated
			// privilege level
			return putUser(new User(email, privileges,
					user.getRegisteredDate(), user.getFirstName(),
					user.getLastName()));
		}
		return false;
	}

	// Returns false if user already exists, true otherwise
	public boolean addUser(User user) {
		// See if this user exists in the dbase already...
		User userFromDBase = getUser(user.getGoogleUser());
		if (userFromDBase != null) {
			return false;
		}

		// Create root entity with a key specifier created from the user's email
		Entity userEntity = new Entity("User", user.getGoogleUser());

		userEntity.setProperty("Registered", user.getRegisteredDate());
		userEntity.setProperty("Privilege", user.getPrivilegeLevel().toString());
		userEntity.setProperty("FirstName", user.getFirstName());
		userEntity.setProperty("LastName", user.getLastName());

		return putUser(user);
	}

	private boolean putUser(User user) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		// Create root entity with a key specifier created from the user's email
		Entity userEntity = new Entity("User", user.getGoogleUser());

		userEntity.setProperty("Registered", user.getRegisteredDate());
		userEntity.setProperty("Privilege", user.getPrivilegeLevel().toString());
		userEntity.setProperty("FirstName", user.getFirstName());
		userEntity.setProperty("LastName", user.getLastName());

		try {
			datastore.put(userEntity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/* Shameful shameful singleton code */
	private static RightsManagementController instance;

	public static RightsManagementController getInstance() {
		if (instance == null) {
			instance = new RightsManagementController();
		}

		return instance;
	}
}