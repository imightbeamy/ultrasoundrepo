package edu.umbc.ultra.logic;

import java.util.Date;

public class Patient {
	public static enum Gender {
		MALE, FEMALE, OTHER
	}

	private String firstName;
	private String lastName;
	private Date dob;
	private Gender gender;
	private String id;

	public Patient(String firstName, String lastName, Date dob, Gender gender,
			       String id) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.gender = gender;
		this.id = id;
	}

	public Patient(String firstName, String lastName, Date dob, Gender gender) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.gender = gender;
		this.id = "" + firstName.charAt(0) + lastName.charAt(0)
				+ dob.toString() + "";
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getDob() {
		return dob;
	}

	public Gender getGender() {
		return gender;
	}

	public String getId() {
		return id;
	}

	public static String getGenderAsString(Gender gender) {
		if (gender == Gender.MALE) {
			return "MALE";
		} 
		else if (gender == Gender.FEMALE) {
			return "FEMALE";
		} 
		else if (gender == Gender.OTHER) {
			return "OTHER";
		}
		return null;
	}

	public static Gender getGenderFromString(String in) {
		if (in.equalsIgnoreCase("MALE")) {
			return Gender.MALE;
		} 
		else if (in.equalsIgnoreCase("FEMALE")) {
			return Gender.FEMALE;
		} 
		else {
			return Gender.OTHER;
		}
	}
}
