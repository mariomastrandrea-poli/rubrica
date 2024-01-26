package it.mariomastrandrea.testrubrica.repository;

import java.util.List;

import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.models.User;

public interface RubricaRepository {
	public boolean initAndLoadContacts();
	public List<Persona> getContacts();
	public void deleteContact(Persona contact);
	public Persona getContact(int id);
	public void createNewContact(Persona contact);
	public void editContact(Persona contact);
	public boolean initAndLoadUsers();
	public User checkUserLogin(User user);
}
