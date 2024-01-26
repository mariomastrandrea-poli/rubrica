package it.mariomastrandrea.testrubrica.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.models.User;

public class RubricaFileRepository implements RubricaRepository {
	private static int nextContactId = 0;
	private static int nextUserId = 0;
	private static String fieldsSeparatorRegex = ";";
	private static int numOfContactFields = 5;
	private static int numOfUserFields = 2;
	
	private String contactsFilename;
	private String usersFilename;
	private List<Persona> contacts;
	private List<User> users;
	@SuppressWarnings("unused")
	private User currentUser;
	
	
	public RubricaFileRepository(String contactsFilename, String usersFilename) {
		this.contactsFilename = contactsFilename;
		this.usersFilename = usersFilename;
		this.contacts = new ArrayList<Persona>();
		this.users = new ArrayList<User>();
	}
	
	@Override
	public boolean init() {
		// there is no initialization in this repository
		return true;
	}
	
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}
	
	/**
	 * Load user's contacts in memory
	 */
	private boolean initAndLoadContacts() {
		contacts.clear();
		
		// read content of the file
		try {
			File rubricaFile = new File(this.contactsFilename);
			Scanner scanner = new Scanner(rubricaFile);
			
			while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineTokens = line.split(fieldsSeparatorRegex);
                
                // check if it is a valid contact line
                if (lineTokens.length != numOfContactFields) {
                	continue;
                }
                
                String name = lineTokens[0];
                String surname = lineTokens[1];
                String address = lineTokens[2];
                String telephoneNumber = lineTokens[3];
                
                int age;
                try {
                	age = Integer.parseInt(lineTokens[4]);
                }
                catch (Exception e) {
                	// skip lines with wrong age
                	continue;
                }
                
                // * Here they should be loaded only the current user's contacts, 
                // but I simplify this Repository retrieving all the contacts *
                
                // create and save a new contact
                Persona contact = new Persona(nextContactId, name, surname, address, telephoneNumber, age);
                this.contacts.add(contact);  
                nextContactId += 1;
            }
			
			scanner.close();
		} 
		catch (FileNotFoundException fnfe) {
			// if file is not found, it means the Rubrica is empty
			System.err.println(String.format("Rubrica file %s was not found", this.contactsFilename));
			fnfe.printStackTrace();
			return true;
		}
		catch (Exception e) {
			System.err.println("An unexpected error occurred in RubricaFileRepository init");
			e.printStackTrace();
			return false;
		}
		
		// contacts successfully loaded
		return true;
	}
	
	@Override
	public List<Persona> getContacts() {
		if (this.contacts.isEmpty()) {
			this.initAndLoadContacts();
		}
		
		// clone the list before returning it, to preserve data integrity in the Repository
		return this.contacts.stream().map(persona -> persona.clone()).toList();
	}

	@Override
	public void deleteContact(Persona contact) {
		if (this.contacts.isEmpty()) {
			this.initAndLoadContacts();
		}
		
		// update list
		this.contacts = this.contacts.stream()
				.filter(p -> p.getId() != contact.getId())
				.collect(Collectors.toCollection(ArrayList::new));
		
		// write to file
		this.updateContactsFile(this.contacts);
	}
	
	@Override
	public void createNewContact(Persona contact) {
		if (this.contacts.isEmpty()) {
			this.initAndLoadContacts();
		}
		
		contact.setId(nextContactId);
		nextContactId += 1;
		
		this.contacts.add(contact);
		
		// write to file
		this.updateContactsFile(this.contacts);
	}
	
	@Override
	public void editContact(Persona contact) {
		if (this.contacts.isEmpty()) {
			this.initAndLoadContacts();
		}
		
		// substitute the new contact
		this.contacts = this.contacts.stream()
				.map(p -> p.getId() == contact.getId() ? contact : p)
				.collect(Collectors.toCollection(ArrayList::new));
		
		// write to file
		this.updateContactsFile(this.contacts);
	}
	
	private void updateContactsFile(List<Persona> contacts) {
		try {
            FileOutputStream file = new FileOutputStream(this.contactsFilename);
            PrintStream stream = new PrintStream(file);

            // header
            stream.printf("%s-------------------------------------------------------------------\n", this.contactsFilename);
            
            for (Persona p : contacts) {
            	stream.printf(
            		"%s;%s;%s;%s;%d\n",
            		p.getName(), p.getSurname(), 
                	p.getAddress(), p.getTelephoneNumber(), p.getAge()
            	);
            }
            
            // footer
            stream.println("-------------------------------------------------------------------");

            stream.close();
        } 
		catch (IOException ioe) {
			System.err.println("An unexpected error occurred saving the new contacts");
            ioe.printStackTrace();
        }
	}
	
	// * Users *
	
	private boolean initAndLoadUsers() {
		this.users.clear();
		
		// read content of the file
		try {
			File usersFile = new File(this.usersFilename);
			Scanner scanner = new Scanner(usersFile);
			
			while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineTokens = line.split(fieldsSeparatorRegex);
                
                // check if it is a valid user line
                if (lineTokens.length != numOfUserFields) {
                	continue;
                }
                
                String username = lineTokens[0];
                String passwordHash = lineTokens[1];
                
                // create and save a new user
                User user = new User(nextUserId, username, passwordHash);
                this.users.add(user);  
                nextUserId += 1;
            }
			
			scanner.close();
		} 
		catch (FileNotFoundException fnfe) {
			// users file is mandatory
			System.err.println(String.format("Users file %s was not found", this.usersFilename));
			fnfe.printStackTrace();
			return false;
		}
		catch (Exception e) {
			System.err.println("An unexpected error occurred in RubricaFileRepository init");
			e.printStackTrace();
			return false;
		}
		
		// users successfully loaded
		return true;
	}
	
	public User checkUserLogin(User user) {
		if (this.users.isEmpty()) {
			boolean loadedUsers = this.initAndLoadUsers();
			if (!loadedUsers) {
				System.err.println("An error occurred loading app users");
				return null;
			}
		}
		
		for (User u: this.users) {
			if (u.getUsername().equals(user.getUsername())) {
				if (u.getPasswordhash().equals(user.getPasswordhash())) {
					return u.clone();
				}
				else {
					return null;
				}
			}
		}
		return null;
	}
}
