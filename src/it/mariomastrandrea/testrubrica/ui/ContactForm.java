package it.mariomastrandrea.testrubrica.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.mariomastrandrea.testrubrica.models.Persona;

public class ContactForm {
	private static final String nameLabel = "Nome";
	private static final String surnameLabel = "Cognome";
	private static final String addressLabel = "Indirizzo";
	private static final String telephoneNumberLabel = "Telefono";
	private static final String ageLabel = "Età";
	
	private static final int rowHeight = 10;
	private static final int columnWidth = 20;
	private static final int formLateralPadding = 30;
	private static final int formVerticalPadding = 5;
	
	private Persona contact;

	private JTextField nameTextField;
	private JTextField surnameTextField;
	private JTextField addressTextField;
	private JTextField telephoneNumberTextField;
	private JTextField ageTextField;

	public ContactForm(Persona contact) {
		this.contact = contact;
	}
	
	public JPanel create() {
		GridBagLayout gridLayout = new GridBagLayout();
		JPanel panel = new JPanel(gridLayout); 
		
		// set grid layout UI
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL; 
		constraints.ipady = rowHeight;
		constraints.ipadx = columnWidth;
		constraints.insets = new Insets(formVerticalPadding, formLateralPadding, formVerticalPadding, formLateralPadding);
		
		// name
		
		constraints.gridy = 0;
		constraints.gridx = 0;
		panel.add(new JLabel(nameLabel), constraints);
		
		this.nameTextField = new JTextField(contact == null ? "" : contact.getName());
		constraints.gridx = 1;
		constraints.weightx = 1;
		panel.add(this.nameTextField, constraints);
		
		// surname
		
		constraints.gridy = 1;
		constraints.gridx = 0;	
		constraints.weightx = 0;
		panel.add(new JLabel(surnameLabel), constraints);
		
		this.surnameTextField = new JTextField(contact == null ? "" : contact.getSurname());
		constraints.gridx = 1;
		constraints.weightx = 1;
		panel.add(this.surnameTextField, constraints);
		
		// address
		
		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.weightx = 0;
		panel.add(new JLabel(addressLabel), constraints);
		
		this.addressTextField = new JTextField(contact == null ? "" : contact.getAddress());
		constraints.gridx = 1;
		constraints.weightx = 1;
		panel.add(this.addressTextField, constraints);
		
		// telephone number
		constraints.gridy = 3;
		constraints.gridx = 0;	
		constraints.weightx = 0;
		panel.add(new JLabel(telephoneNumberLabel), constraints);
		
		this.telephoneNumberTextField = new JTextField(contact == null ? "" : contact.getTelephoneNumber());
		constraints.gridx = 1;
		constraints.weightx = 1;
		panel.add(this.telephoneNumberTextField, constraints);
		
		// age
		constraints.gridy = 4;
		constraints.gridx = 0;	
		constraints.weightx = 0;
		panel.add(new JLabel(ageLabel), constraints);
		
		String ageString = contact == null ? "" : contact.getAge() == null ? "" : Integer.toString(contact.getAge());
		this.ageTextField = new JTextField(ageString);
		constraints.gridx = 1;
		constraints.weightx = 1;
		panel.add(this.ageTextField, constraints);		
				
		return panel;
	}
	
	public Persona getPersonDetailsToSave() {
		String name = this.nameTextField.getText();
		String surname = this.surnameTextField.getText();
		String address = this.addressTextField.getText();
		String telephoneNumber = this.telephoneNumberTextField.getText();
		String ageString = this.ageTextField.getText();
		
		List<String> errors = new ArrayList<String>();
		
		// check input
		
		if (name.trim().isEmpty()) {
			errors.add(String.format("Errore: il campo %s non può essere vuoto", nameLabel));
		}
		
		Integer age = null;
				
		if (!ageString.trim().isEmpty()) {
			try {
				age = Integer.parseInt(ageString.trim());
			}
			catch (NumberFormatException nfe) {
				errors.add(String.format("Errore: il campo %s è in un formato errato. Inserisci un numero intero", ageLabel));
			}
		}
		
		if(!telephoneNumber.trim().isEmpty()) {
			if (!telephoneNumber.matches("^+?[\\d]+$")) {
				// wrong format
				errors.add(String.format("Errore: il campo %s è in un formato errato. Inserisci un numero di telefono valido ", telephoneNumberLabel));
			}
		}
		
		if (errors.size() > 0) {
			// some error occurred
    		JOptionPane.showMessageDialog(null, String.join("\n", errors));
    		return null;
		}
		else {
			Persona newContact = new Persona(
				this.contact == null ? null : this.contact.getId(),
				name.trim(),
				surname.trim(),
				address.trim(),
				telephoneNumber.trim(),
				age
			);
						
			return newContact;
		}
	}
}
