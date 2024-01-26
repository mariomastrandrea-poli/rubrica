package it.mariomastrandrea.testrubrica.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import it.mariomastrandrea.testrubrica.models.User;

public class LoginToolbar {
	private static final String loginLabel = "Login";
	
	private LoginForm loginForm;
	private LoginNavigator navigator;
	
	
	public LoginToolbar(LoginForm loginForm, LoginNavigator navigator) {
		this.loginForm = loginForm;
		this.navigator = navigator;
	}
	
	public JToolBar create() {
		JToolBar toolbar = new JToolBar();
		
		JButton loginButton = new JButton(loginLabel);
		
		loginButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	User userToLogin = loginForm.checkAndGetUserToLogIn();
		    	
		    	if (userToLogin == null) {
		    		// an error occurred
		    		return;
		    	}
		    	
		    	// * successful login *
		    	
		    	// open new window
		    	navigator.navigateToContactsTable(userToLogin);
		    	return;
		    }
		});
		
		toolbar.add(loginButton);
		
		return toolbar;
	}
}
