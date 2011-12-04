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

	public ArrayList<DataEntry> searchByKeyword(String keyword) {
		ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(keyword.split(" ")));
		System.out.println(keywords);
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
				DataEntry de = getDataEntryFromEntity(dataEntry);
				if(de != null) {
					dataEntries.add(de);
				}
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		 }
		return dataEntries;
	}
	
	public ArrayList<Key> searchComments(ArrayList<String> keywords, String type) {
		
		
		for(int i = 0; i < keywords.size(); i++) {
			keywords.set(i, keywords.get(i).toUpperCase());
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
		
		Query query = new Query("Patient");
		query.setKeysOnly();
		if(first != null) {
			query.addFilter("FirstName", Query.FilterOperator.EQUAL, first);
		}
		if(last != null) {
			query.addFilter("LastName", Query.FilterOperator.EQUAL, last);
		}
		if(gender != null) {
			query.addFilter("Gender", Query.FilterOperator.EQUAL, gender.toString());
		}
		
		ArrayList<Key> dataEntryKeys = new ArrayList<Key>();
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
		if(email == null || email.equals(""))
		{ return new ArrayList<Key>(); }
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
			String lastName, Gender gender, String chiefComplaint, String reason, String interpretation,
			String keywords, String userEmail) {
		
		ArrayList<ArrayList<Key>> resultLists = new ArrayList<ArrayList<Key>>();
		if(firstName != null || lastName!=null || gender != null) {
			resultLists.add(searchPatientFields(makeNull(firstName).toUpperCase(), makeNull(lastName).toUpperCase(), gender, null));
		}
		if(userEmail != null && !userEmail.equals("")) {
			resultLists.add(searchByUser(userEmail));
		}
		if(keywords != null && !keywords.equals("")) {
			resultLists.add(searchByKeywordforKeys(new ArrayList<String>(Arrays.asList(keywords.split(" ")))));
		}
		if(chiefComplaint != null && !chiefComplaint.equals("")) {
			resultLists.add(searchComments(new ArrayList<String>(Arrays.asList(chiefComplaint.split(" "))), "CC"));
		}
		if(interpretation != null && !interpretation.equals("")) {
			resultLists.add(searchComments(new ArrayList<String>(Arrays.asList(interpretation.split(" "))), "KW"));
		}
		if(reason != null && !reason.equals("")) {
			resultLists.add(searchComments(new ArrayList<String>(Arrays.asList(reason.split(" "))), "RE"));
		}
		
		ArrayList<Key> results = resultLists.get(0);
		for(int i = 1; i < resultLists.size(); i++) {
			results = intersection(resultLists.get(i), results);
		}
		return KeysToDataEntries(results);
	}
	
	public String makeNull(String str) {
		if(str != null && str.equals("")) {
			return null;
		}
		return str;
	}

    public <T> ArrayList<T> intersection(ArrayList<T> list1, ArrayList<T> list2) {
    	ArrayList<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
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
