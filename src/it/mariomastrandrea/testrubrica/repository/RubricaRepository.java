package it.mariomastrandrea.testrubrica.repository;

import java.util.List;

import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.models.User;

public interface RubricaRepository {
	public boolean init();
	public void setCurrentUser(User user);

	// contacts
	public List<Persona> getContacts();
	public void createNewContact(Persona contact);
	public void editContact(Persona contact);
	public void deleteContact(Persona contact);
	
	// users
	public User checkUserLogin(User user);
}
