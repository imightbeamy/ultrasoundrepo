package edu.umbc.ultra.dbase;

import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

import edu.umbc.ultra.logic.Comment;
import edu.umbc.ultra.logic.DataEntry;
import edu.umbc.ultra.logic.Patient;
import edu.umbc.ultra.logic.Patient.Gender;
import edu.umbc.ultra.logic.User;

public class SearchController
{
	public SearchController()
	{
		
	}
	
	public DataEntry getEntryByID(String id)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("DataEntry");
		query.addFilter("uniqueID", FilterOperator.EQUAL, id);
		
		PreparedQuery pq = datastore.prepare(query);
		
		try
		{
			return getDataEntryFromEntity(pq.asSingleEntity());
		}
		catch(Exception e)
		{
			return null;
		}
		
	}
	
	// Returns a DataEntry given an entity of kind DataEntry
	public DataEntry getDataEntryFromEntity(Entity result)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		try
		{
			Entity patientEntity = datastore.get(result.getParent());
			Entity userEntity = datastore.get(patientEntity.getKey());
			User user = new User(userEntity.getKey().toString(), User.getPrivilegeLevelFromString((String)userEntity.getProperty("Privilege")), (Date)userEntity.getProperty("Registered"), (String)userEntity.getProperty("FirstName"), (String)userEntity.getProperty("LastName"));
			Patient patient = new Patient((String)patientEntity.getProperty("FirstName"), (String)patientEntity.getProperty("LastName"), (Date)patientEntity.getProperty("DOB"), Patient.getGenderFromString((String)patientEntity.getProperty("Gender")), (String)patientEntity.getProperty("ID"));
			DataEntry de = new DataEntry(getCommentsFromEntry(result), patient, user, (BlobKey)result.getProperty("blobKey"), (String)result.getProperty("uniqueID"), (Date)result.getProperty("timestamp"));
			return de;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public ArrayList<Comment> getCommentsFromEntry(Entity entry)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Comment").setAncestor(entry.getKey());
		PreparedQuery pq = datastore.prepare(query);
		
		ArrayList<Comment> comments = new ArrayList<Comment>();
		for(Entity result : pq.asIterable())
		{
			comments.add(new Comment((String)result.getProperty("Text"), (Date)result.getProperty("Timestamp"), RightsManagementController.getInstance().getUser((String)result.getProperty("Author"))));
		}
		
		return comments;
	}
	
	public ArrayList<DataEntry> searchForEntries(String firstName, String lastName, Gender gender, String chiefComplaint, String keywords, String userEmail)
	{
		Query query = new Query("DataEntry");
		if(userEmail != null)
		{
			query.setAncestor(KeyFactory.createKey("User", userEmail));
		}
		
		
		return null;
	}

	public ArrayList<DataEntry> searchForPatients(String firstName, String lastName, Gender gender, String chiefComplaint, String keyWords, String userEmail)
	{
		Query query = new Query("Patient");
		if(userEmail != null)
		{
			query.setAncestor(KeyFactory.createKey("User", userEmail));
		}
		
		
		return null;
	}
	
	//Gender being null means search for any gender
	public ArrayList<DataEntry> searchForEntriesTEST(String firstName, String lastName, Gender gender, String chiefComplaint, String keywords, String userEmail)
	{
		ArrayList<DataEntry> results = new ArrayList<DataEntry>();
		ArrayList<Comment> comments = new ArrayList<Comment>();
		User user = new User();
		//Patient newPatient = new Patient("Nathan", "Broemm", new Date(), Gender.MALE, "nab0415");
		Comment testComment = new Comment("Turn the Computer off and on!", user);
		comments.add(testComment);
		//results.add(new DataEntry(comments, newPatient, new User()));
		return results;
	}
	
	/* Shameful shameful singleton code */
	private static SearchController instance;
	
	public static SearchController getInstance()
	{
		if(instance == null)
		{
			instance = new SearchController();
		}
		
		return instance;
	}
}
