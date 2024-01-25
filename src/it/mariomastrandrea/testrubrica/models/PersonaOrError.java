package it.mariomastrandrea.testrubrica.models;

public class PersonaOrError {
	Persona contact;
	String errorMessage;
	
	public PersonaOrError(Persona contact, String errorMessage) {
		this.contact = contact;
		this.errorMessage = errorMessage;
	}
	
	public Persona getContact() { return this.contact; }
	public String getErrorMessage() { return this.errorMessage; }
}
