package edu.umbc.ultra.dbase;

import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
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
		if(firstName != null || lastName != null || gender != null)
		{
			return searchForPatients(firstName, lastName, gender, chiefComplaint, keywords, userEmail);
		}
		
		return searchDataEntriesByPatientKey(null, chiefComplaint, keywords, (userEmail == null || userEmail == "") ? null : KeyFactory.createKey("User", userEmail));
	}
	
	private ArrayList<DataEntry> searchForPatients(String firstName, String lastName, Gender gender, String chiefComplaint, String keyWords, String userEmail)
	{
		ArrayList<DataEntry> results = new ArrayList<DataEntry>();
		Query query = new Query("Patient");
		
		// Add filter by user
		if(userEmail != null && userEmail != "")
		{
			query.setAncestor(KeyFactory.createKey("User", userEmail));
		}
		
		// Add filters by patient
		if(firstName != null && firstName != "")
		{
			query.addFilter("FirstName", FilterOperator.EQUAL, firstName);
		}
		if(lastName != null && lastName != "")
		{
			query.addFilter("LastName", FilterOperator.EQUAL, lastName);
		}
		if(gender != null)
		{
			query.addFilter("Gender", FilterOperator.EQUAL, Patient.getGenderAsString(gender));
		}
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(query);
		
		for(Entity patientEntity : pq.asIterable())
		{
			ArrayList<DataEntry> entries = searchDataEntriesByPatientKey(patientEntity.getKey(), chiefComplaint, keyWords, KeyFactory.createKey("User", userEmail));
			results.addAll(entries);
		}
		
		return results;
	}
	
	private ArrayList<DataEntry> searchDataEntriesByPatientKey(Key patientKey, String chiefComplaint, String keywords, Key userKey)
	{
		// Do two queries, one for chief complaint and one for the other keywords
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		ArrayList<DataEntry> results = new ArrayList<DataEntry>();
		ArrayList<Key> dataEntryKeys = new ArrayList<Key>();
		
		if(chiefComplaint != null && chiefComplaint != "")
		{
			Query query = new Query("Keyword");
			if(patientKey != null)
			{
				query.setAncestor(patientKey);
			}
			else if(userKey != null)
			{
				query.setAncestor(userKey);
			}
			query.addFilter("Type", FilterOperator.EQUAL, "CC");
			query.addFilter("Word", FilterOperator.EQUAL, chiefComplaint.toLowerCase());
			
			PreparedQuery pq = datastore.prepare(query);
			for(Entity kwe : pq.asIterable())
			{
				Key dataEntityKey = kwe.getParent().getParent();
				if(!dataEntryKeys.contains(dataEntityKey))
				{
					dataEntryKeys.add(dataEntityKey);
				}
			}
		}
		
		if(keywords != null && keywords != "")
		{
			Query query = new Query("Keyword");
			if(patientKey != null)
			{
				query.setAncestor(patientKey);
			}
			else if(userKey != null)
			{
				query.setAncestor(userKey);
			}
			query.addFilter("Type", FilterOperator.EQUAL, "KW");
			ArrayList<String> kwc = new ArrayList<String>();
			System.out.println("Splitting keywords...");
			for(String kw : keywords.toLowerCase().split("\\s+"))
			{
				kwc.add(kw);
				System.out.println("\"" + kw + "\"");
			}
			query.addFilter("Word", FilterOperator.IN, kwc);
			
			PreparedQuery pq = datastore.prepare(query);
			int count = 0;
			for(Entity kwe : pq.asIterable())
			{
				System.out.println("Processing kwr " + (++count));
				Key dataEntityKey = kwe.getParent().getParent();
				if(!dataEntryKeys.contains(dataEntityKey))
				{
					dataEntryKeys.add(dataEntityKey);
				}
			}
		}
		
		for(Key key : dataEntryKeys)
		{
			try
			{
				Entity dataEntity = datastore.get(key);
				DataEntry de = getDataEntryFromEntity(dataEntity);
				results.add(de);
			}
			catch (EntityNotFoundException e)
			{
			}
		}
		
		return results;
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
