package it.mariomastrandrea.testrubrica.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.models.PersonaOrError;

public class ContactForm {
	private Persona contact;
	
	private static final String nameLabel = "Nome";
	private static final String surnameLabel = "Cognome";
	private static final String addressLabel = "Indirizzo";
	private static final String telephoneNumberLabel = "Telefono";
	private static final String ageLabel = "Età";
	
	private JTextField nameTextField;
	private JTextField surnameTextField;
	private JTextField addressTextField;
	private JTextField telephoneNumberTextField;
	private JTextField ageTextField;



	public ContactForm(Persona contact) {
		this.contact = contact;
	}
	
	public JPanel create() {
		JPanel panel = new JPanel(new GridLayout(0, 2)); 
		
		// name
		panel.add(new JLabel(nameLabel));
		this.nameTextField = new JTextField(contact == null ? "" : contact.getName());
		panel.add(this.nameTextField);
		
		// surname
		panel.add(new JLabel(surnameLabel));
		this.surnameTextField = new JTextField(contact == null ? "" : contact.getSurname());
		panel.add(this.surnameTextField);
		
		// address
		panel.add(new JLabel(addressLabel));
		this.addressTextField = new JTextField(contact == null ? "" : contact.getAddress());
		panel.add(this.addressTextField);
		
		// telephone number
		panel.add(new JLabel(telephoneNumberLabel));
		this.telephoneNumberTextField = new JTextField(contact == null ? "" : contact.getTelephoneNumber());
		panel.add(this.telephoneNumberTextField);
		
		// age
		panel.add(new JLabel(ageLabel));
		String ageString = contact == null ? "" : contact.getAge() == null ? "" : Integer.toString(contact.getAge());
		this.ageTextField = new JTextField(ageString);
		panel.add(this.ageTextField);	
		
		return panel;
	}
	
	public PersonaOrError getPersonDetailsToSave() {
		String name = this.nameTextField.getText();
		String surname = this.surnameTextField.getText();
		String address = this.addressTextField.getText();
		String telephoneNumber = this.telephoneNumberTextField.getText();
		String ageString = this.ageTextField.getText();
		
		List<String> errors = new ArrayList<String>();
		
		// check input
		
		if (name.isBlank()) {
			errors.add(String.format("Errore: il campo %s non può essere vuoto", nameLabel));
		}
		
		Integer age = null;
				
		if (!ageString.isBlank()) {
			try {
				age = Integer.parseInt(ageString.strip());
			}
			catch (NumberFormatException nfe) {
				errors.add(String.format("Errore: il campo %s è in un formato errato. Inserisci un numero intero", ageLabel));
			}
		}
		
		if(!telephoneNumber.isBlank()) {
			if (!telephoneNumber.matches("^+?[\\d]+$")) {
				// wrong format
				errors.add(String.format("Errore: il campo %s è in un formato errato. Inserisci un numero di telefono valido ", telephoneNumberLabel));
			}
		}
		
		if (errors.size() > 0) {
			// some error occurred
			return new PersonaOrError(null, String.join("\n", errors));
		}
		else {
			Persona newContact = new Persona(
				this.contact == null ? null : this.contact.getId(),
				name.strip(),
				surname.strip(),
				address.strip(),
				telephoneNumber.strip(),
				age
			);
						
			return new PersonaOrError(newContact, null);
		}
	}
}
