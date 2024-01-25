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

public class RubricaFileRepository implements RubricaRepository {
	private static int nextContactId = 0;
	private static String contactsSeparatorRegex = ";";
	private static int numOfFields = 5;
	private String filename;
	private List<Persona> contacts;
	
	
	public RubricaFileRepository(String filename) {
		this.filename = filename;
		this.contacts = new ArrayList<Persona>();
	}

	@Override
	public boolean initAndLoadContacts() {
		contacts.clear();
		
		// read content of the file
		try {
			File rubricaFile = new File(this.filename);
			Scanner scanner = new Scanner(rubricaFile);
			
			while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] lineTokens = line.split(contactsSeparatorRegex);
                
                // check if it is a valid contact line
                if (lineTokens.length != numOfFields) {
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
                
                // create and save a new contact
                Persona contact = new Persona(nextContactId, name, surname, address, telephoneNumber, age);
                this.contacts.add(contact);  
                nextContactId += 1;
            }
			
			scanner.close();
		} 
		catch (FileNotFoundException fnfe) {
			// if file is not found, it means the Rubrica is empty
			System.err.println(String.format("Rubrica file %s was not found", this.filename));
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
		// clone the list before returning it, to preserve data integrity in the Repository
		return this.contacts.stream().map(persona -> persona.clone()).toList();
	}
	
	@Override
	public Persona getContact(int id) {
		Persona found = null;
		
		for (Persona p: this.contacts) {
			if (p.getId() == id) {
				found = p;
				break;
			}
		}
		
		return found;
	}


	@Override
	public void deleteContact(Persona contact) {
		// update list
		this.contacts = this.contacts.stream()
				.filter(p -> p.getId() != contact.getId())
				.collect(Collectors.toCollection(ArrayList::new));
		
		// write to file
		this.updateFile(this.contacts);
	}
	
	@Override
	public void createNewContact(Persona contact) {
		contact.setId(nextContactId);
		nextContactId += 1;
		
		this.contacts.add(contact);
		
		// write to file
		this.updateFile(this.contacts);
	}
	
	@Override
	public void editContact(Persona contact) {
		// substitute the new contact
		this.contacts = this.contacts.stream()
				.map(p -> p.getId() == contact.getId() ? contact : p)
				.collect(Collectors.toCollection(ArrayList::new));
		
		// write to file
		this.updateFile(this.contacts);
	}
	
	private void updateFile(List<Persona> contacts) {
		try {
            FileOutputStream file = new FileOutputStream(this.filename);
            PrintStream stream = new PrintStream(file);

            // header
            stream.printf("%s-------------------------------------------------------------------\n", this.filename);
            
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
}
