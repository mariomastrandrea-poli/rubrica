package it.mariomastrandrea.testrubrica.ui;

import it.mariomastrandrea.testrubrica.models.Persona;

public interface ContactsNavigator {
	public void navigateToEditContact(Persona contact);
	
	public void navigateToAddContact();
}
