package it.mariomastrandrea.testrubrica.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import it.mariomastrandrea.testrubrica.models.User;
import it.mariomastrandrea.testrubrica.repository.RubricaRepository;

public class LoginForm {
	private static final String usernameLabel = "Username";
	private static final String passwordLabel = "Password";
	private static final String wrongLoginMessage = "Login fallito";
	
	private static final int rowHeight = 10;
	private static final int columnWidth = 20;
	private static final int formLateralPadding = 30;
	private static final int formVerticalPadding = 5;
	
	private JTextField usernameField;
	private JTextField passwordField;
	private RubricaRepository repository;
	
	
	public LoginForm(RubricaRepository repository) {
		this.repository = repository;
	}
	
	public JPanel create() {
		GridBagLayout gridLayout = new GridBagLayout();
		JPanel panel = new JPanel(gridLayout); 
		
		// set grid layout UI
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.ipady = rowHeight;
		constraints.ipadx = columnWidth;
		constraints.insets = new Insets(formVerticalPadding, formLateralPadding, formVerticalPadding, formLateralPadding);

		// username
		
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.ipadx = columnWidth;
		panel.add(new JLabel(usernameLabel), constraints);
		
		this.usernameField = new JTextField("");
		constraints.gridx = 1;
		constraints.ipadx = columnWidth * 10;
		panel.add(this.usernameField, constraints);
		
		// password
		
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.ipadx = columnWidth;
		panel.add(new JLabel(passwordLabel), constraints);
		
		this.passwordField = new JPasswordField("");
		constraints.gridx = 1;
		constraints.ipadx = columnWidth * 10;
		panel.add(this.passwordField, constraints);
		
		return panel;
	}
	
	public User checkAndGetUserToLogIn() {
		String username = this.usernameField.getText();
		String password = this.passwordField.getText();
		
		List<String> errors = new ArrayList<String>();
		
		if (username == null || username.trim().isEmpty()) {
			errors.add(String.format("Il campo %s non puo' essere vuoto", usernameLabel));
		}
		
		if (password == null || password.trim().isEmpty()) {
			errors.add(String.format("Il campo %s non puo' essere vuoto", passwordLabel));
		}
		
		if (errors.size() > 0) {
			// input error
			JOptionPane.showMessageDialog(null, String.join("\n", errors));
			return null;
		}
		
		// login check
		User checkedUser = this.repository.checkUserLogin(new User(username.trim(), password.trim()));
		
		if (checkedUser == null) {
			// wrong login
			JOptionPane.showMessageDialog(null, wrongLoginMessage);
			return null;
		}
		
		// successful log in
		return checkedUser;
	}
}
