package edu.umbc.ultra.logic;

import java.util.Date;

public class Patient
{
	public static enum Gender {MALE, FEMALE, OTHER}
	
	private String firstName;
	private String lastName;
	private Date dob;
	private Gender gender;
	private String id;
	
	
	
	public Patient(String firstName, String lastName, Date dob, Gender gender, String id)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.gender = gender;
		this.id = id;
	}
	public String getFullName()
	{
		return firstName + " " + lastName;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public Date getDob()
	{
		return dob;
	}
	public Gender getGender()
	{
		return gender;
	}
	public String getId()
	{
		return id;
	}
}
