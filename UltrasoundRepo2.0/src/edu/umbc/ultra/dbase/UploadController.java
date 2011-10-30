package edu.umbc.ultra.dbase;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import edu.umbc.ultra.logic.Comment;
import edu.umbc.ultra.logic.DataEntry;
import edu.umbc.ultra.logic.Patient;
import edu.umbc.ultra.logic.User;

/* Making this class and other controller classes singletons...didn't want to, don't really have a choice */
public class UploadController
{
	public UploadController()
	{
		// Nothing here yet...
	}
	
	// Returns NULL upon success, otherwise returns the error heading
	public String UploadEntry(DataEntry entry)
	{
		Entity entity = getEntityFromDataEntry(entry);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		try
		{
			datastore.put(entity);
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace(System.err);
			return e.getMessage();
		}
		catch(ConcurrentModificationException e)
		{
			e.printStackTrace(System.err);
			return e.getMessage();
		}
		catch(DatastoreFailureException e)
		{
			e.printStackTrace(System.err);
			return e.getMessage();
		}
		
		return null;
	}
	
	/*
	 * The dbase entity heirarchy is as follows:
	 * 	User Entity 1
	 *   -> DataEntry Entity 1
	 *   	 -> Patient Entity
	 *   	 -> Comment Entity 1
	 *   	 -> Comment Entity 2
	 *   -> DataEntry Entity 2
	 *   etc...
	 *   
	 * The advantage here is that restricting a lower level user to only accessing his/her
	 *  own uploads becomes very simple.
	 *  
	 * Therefore, this method takes in a DataEntry and returns a User entity with associated children.	
	 */
	private Entity getEntityFromDataEntry(DataEntry entry)
	{
		User author = entry.getAuthor();
		Patient patient = entry.getPatient();
		ArrayList<Comment> comments = entry.getComments();
		
		// Create root entity with a key specifier created from the user's email
		Entity userEntity = new Entity("User", author.getGoogleUser().getEmail());
		userEntity.setProperty("GoogleAccount", author.getGoogleUser());
		userEntity.setProperty("Registered", author.getRegisteredDate());
		userEntity.setProperty("Privelege", author.getPrivelegeLevel()); // This may not work
		
		// Create and add DataEntry entity with a generated unique key, specifying the parent key as the user
		Entity dataEntity = new Entity("DataEntry", userEntity.getKey());
		dataEntity.setProperty("timestamp", entry.getTimestamp());
		
		// STUB: No facilitation of blob storage yet
		
		// Create and add Patient entity using as a key the unique id assigned upon creation
		Entity patientEntity = new Entity("Patient", patient.getId(), dataEntity.getKey());
		patientEntity.setProperty("FirstName", patient.getFirstName());
		patientEntity.setProperty("LastName", patient.getLastName());
		patientEntity.setProperty("DOB", patient.getDob());
		patientEntity.setProperty("Gender", patient.getGender());
		patientEntity.setProperty("ID", patient.getId());
		
		// Add each comment using a system generated key for each
		for(Comment comment: comments)
		{
			Entity commentEntity = new Entity("Comment", dataEntity.getKey());
			commentEntity.setProperty("Text", comment.getContent());
			// Author entry consists of user's google account
			commentEntity.setProperty("Author", comment.getAuthor().getGoogleUser());
			commentEntity.setProperty("Timestamp", comment.getTimestamp());
		}
		
		return userEntity;
	}
	
	/* Shameful shameful singleton code */
	private static UploadController instance;
	
	public static UploadController getInstance()
	{
		if(instance == null)
		{
			instance = new UploadController();
		}
		
		return instance;
	}
}
