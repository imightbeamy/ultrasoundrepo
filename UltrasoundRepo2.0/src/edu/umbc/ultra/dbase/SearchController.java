package edu.umbc.ultra.dbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

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
import com.google.appengine.api.datastore.FetchOptions.Builder;

import edu.umbc.ultra.logic.Comment;
import edu.umbc.ultra.logic.DataEntry;
import edu.umbc.ultra.logic.Patient;
import edu.umbc.ultra.logic.Patient.Gender;
import edu.umbc.ultra.logic.User;

public class SearchController {
	public SearchController() {

	}

	public DataEntry getEntryByID(String id) {
		// Get instance of data store controller
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		try {
			Entity data_entry = dataStore.get(KeyFactory.stringToKey(id));
			return getDataEntryFromEntity(data_entry);
		} catch (Exception e) {
			return null;
		}

	}

	// Returns a DataEntry given an entity of kind DataEntry
	public DataEntry getDataEntryFromEntity(Entity result) {
		// Get instance of data store controller
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			// Get the patient entity matching the passed in entity parent
			Entity patientEntity = datastore.get(result.getParent());

			// Get the high-level user entity that is the ancestor of this
			// patient entity
			Entity userEntity = datastore.get(patientEntity.getParent());

			// Populate user object with user entity properties from database
			User user = new User(userEntity.getKey().toString(),
					User.getPrivilegeLevelFromString((String) userEntity
							.getProperty("Privilege")),
					(Date) userEntity.getProperty("Registered"),
					(String) userEntity.getProperty("FirstName"),
					(String) userEntity.getProperty("LastName"));

			// Populate patient object with patient entity properties from
			// database
			Patient patient = new Patient(
					(String) patientEntity.getProperty("FirstName"),
					(String) patientEntity.getProperty("LastName"),
					(Date) patientEntity.getProperty("DOB"),
					Patient.getGenderFromString((String) patientEntity.getProperty("Gender")),
					(String) patientEntity.getProperty("ID"));

			// Populate dataentry object with entity properties, linking to
			// previously built user and patient objects
			DataEntry de = new DataEntry(getCommentsFromEntry(result), patient,
					user, (BlobKey) result.getProperty("blobKey"),
					KeyFactory.keyToString(result.getKey()),
					(Date) result.getProperty("timestamp"));
			return de;
		} catch (Exception e) {
			// If any keys are not able to be retrieved from the database, or
			// not found, return null.
			e.printStackTrace(System.out);
			return null;
		}
	}

	public ArrayList<Comment> getCommentsFromEntry(Entity entry) {
		// Get an instance of the data store controller
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		// Build a query, serching on Comment entities
		Query query = new Query("Comment").setAncestor(entry.getKey());

		// Submit the query
		PreparedQuery pq = datastore.prepare(query);

		ArrayList<Comment> comments = new ArrayList<Comment>();

		// Iterate over the results from the query
		for (Entity result : pq.asIterable()) {
			User author = RightsManagementController.getInstance().getUser(result.getProperty("Author").toString());
			Comment comment = new Comment(result.getProperty("Text").toString(),
										(Date) result.getProperty("Timestamp"),
										author, result.getProperty("Title").toString());
			comments.add(comment);
		}

		return comments;
	}

	public ArrayList<DataEntry> searchByKeyword(ArrayList<String> keywords) {
		return KeysToDataEntries(searchByKeywordforKeys(keywords));
	}
	
	public ArrayList<Key> searchByKeywordforKeys(ArrayList<String> keywords) {
		ArrayList<Key> dataEntries = searchComments(keywords, null);
		dataEntries.addAll(searchPatients(keywords));
		for(String word: keywords) {
		dataEntries.addAll(searchByUser(word));
		}
		return dataEntries;
	}
	
	public ArrayList<DataEntry> KeysToDataEntries(ArrayList<Key> dataEntities) {
		dataEntities = GetUniqueKeys(dataEntities);
		ArrayList<DataEntry> dataEntries = new ArrayList<DataEntry>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		for (Key key : dataEntities) {
			Entity dataEntry;
			try {
				dataEntry = datastore.get(key);
				dataEntries.add(getDataEntryFromEntity(dataEntry));
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		 }
		return dataEntries;
	}
	
	public ArrayList<Key> searchComments(ArrayList<String> keywords, String type) {
		for(String word: keywords) {
			word = word.toUpperCase();
		}
		Query query = new Query("Keyword");
		query.addFilter("Word", Query.FilterOperator.IN, keywords);
		if(type != null) {
			query.addFilter("Type", Query.FilterOperator.EQUAL, type);
		}
		query.setKeysOnly();
		
		// Get an instance of the data store controller
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// Submit the previously prepared query
		PreparedQuery pq = datastore.prepare(query);
		
		ArrayList<Key> dataEntryKeys = new ArrayList<Key>();
		for (Entity result : pq.asIterable()) {
			Key dataEntry_key = result.getParent().getParent();
			dataEntryKeys.add(dataEntry_key);
		 }
		return dataEntryKeys;
	}
	
	//Searches for keyword in any patient field 
	public ArrayList<Key> searchPatients(ArrayList<String> keywords) {
		// Get an instance of the data store controller
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		ArrayList<Key> dataEntryKeys = new ArrayList<Key>();
		
		String[] fields = {"FirstName", "LastName", "Gender", "ID"}; 
		for(String keyword: keywords) {
			keyword = keyword.toUpperCase();
			for(String field: fields) {
				Query query = new Query("Patient");
				query.addFilter(field, Query.FilterOperator.EQUAL, keyword);
				query.setKeysOnly();
				// Submit the query
				PreparedQuery pq = datastore.prepare(query);
				for (Entity result : pq.asIterable()) {
					Key patients_key = result.getKey();
					Query data_query = new Query("DataEntry", patients_key);
					PreparedQuery dq = datastore.prepare(data_query);
					for (Entity de : dq.asIterable()) {
					  dataEntryKeys.add(de.getKey());
					}
				 }
			}
		}
		return dataEntryKeys;
	}

	public ArrayList<Key> searchPatientFields(String first, String last, Gender gender, String dob) {
		// Get an instance of the data store controller
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		ArrayList<Key> dataEntryKeys = new ArrayList<Key>();
		ArrayList<Query> querys = new ArrayList<Query>();
		
		if(first != null) {
			Query first_query = new Query("Patient");
			first_query.addFilter("FirstName", Query.FilterOperator.EQUAL, first);
			first_query.setKeysOnly();
			querys.add(first_query);
		}
		if(first != null) {
			Query last_query = new Query("Patient");
			last_query.addFilter("LastName", Query.FilterOperator.EQUAL, last);
			last_query.setKeysOnly();
			querys.add(last_query);
		}
		if(first != null) {
			Query g_query = new Query("Patient");
			g_query.addFilter("g_query", Query.FilterOperator.EQUAL, gender.toString());
			g_query.setKeysOnly();
			querys.add(g_query);
		}
		for(Query q: querys) {
			PreparedQuery pq = datastore.prepare(q);
			for (Entity result : pq.asIterable()) {
				Key patients_key = result.getKey();
				Query data_query = new Query("DataEntry", patients_key);
				PreparedQuery dq = datastore.prepare(data_query);
				for (Entity de : dq.asIterable()) {
				  dataEntryKeys.add(de.getKey());
				}
			 }
		}
		return dataEntryKeys;
	}
	
	public String summaryReport(){
		String report =  "";
		
		Query query = new Query("Patient");
		query.setKeysOnly();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(query);
		report+= "Patients in system: " + pq.countEntities(Builder.withDefaults());
		
		Query de_query = new Query("DataEntry");
		query.setKeysOnly();
		PreparedQuery de_q = datastore.prepare(de_query);
		report+= "<br>Studies in system: " + de_q.countEntities(Builder.withDefaults());
		
		Query u_query = new Query("User");
		query.setKeysOnly();
		PreparedQuery u_q = datastore.prepare(u_query);
		report+= "<br>Users in system: " + u_q.countEntities(Builder.withDefaults());
		return report;
	}
	
	public ArrayList<Key> searchByUser(String email) {
		// Get an instance of the data store controller
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		ArrayList<Key> dataEntryKeys = new ArrayList<Key>();
	 	
		Query query = new Query("Patient", KeyFactory.createKey("User", email));
		query.setKeysOnly();
		// Submit the query
		PreparedQuery pq = datastore.prepare(query);
		for (Entity result : pq.asIterable()) {
			Key patients_key = result.getKey();
			Query data_query = new Query("DataEntry", patients_key);
			PreparedQuery dq = datastore.prepare(data_query);
			for (Entity de : dq.asIterable()) {
			  dataEntryKeys.add(de.getKey());
			}
		 }
		return dataEntryKeys;
	}

	
	public ArrayList<Key> GetUniqueKeys(ArrayList<Key> values)
	{
	    return new ArrayList<Key>(new HashSet<Key>(values));
	}
	
	public ArrayList<DataEntry> searchForEntries(String firstName,
			String lastName, Gender gender, String chiefComplaint,
			String keywords, String userEmail) {
		ArrayList<Key> results = new ArrayList<Key>();
		if(firstName != null || lastName!=null || gender != null) {
			results.addAll(searchPatientFields(firstName, lastName, gender, null));
		}
		if(userEmail != null) {
			results.addAll(searchByUser(userEmail));
		}
		
		results.addAll(searchByKeywordforKeys(new ArrayList<String>(Arrays.asList(keywords.split(" ")))));
		results.addAll(searchComments(new ArrayList<String>(Arrays.asList(chiefComplaint.split(" "))), "CC"));
		return KeysToDataEntries(results);
	}

	/* Shameful shameful singleton code */
	private static SearchController instance;

	public static SearchController getInstance() {
		if (instance == null) {
			instance = new SearchController();
		}

		return instance;
	}
}
