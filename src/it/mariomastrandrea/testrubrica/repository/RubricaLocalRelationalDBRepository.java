package it.mariomastrandrea.testrubrica.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.mariomastrandrea.testrubrica.models.DbConfig;
import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.models.User;

public class RubricaLocalRelationalDBRepository implements RubricaRepository {	
	private User currentUser;
	private DbConfig dbConfig;
	private Connection connection;
	
	
	public RubricaLocalRelationalDBRepository(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	@Override
	public boolean init() {
		// Connect to the database
        Connection connection = this.dbConfig.getJdbcConnection();
        
        if (connection == null) {
        	// some error occurred
        	return false;
        }
        
        this.connection = connection;
        System.out.println("** RubricaLocalRelationalDBRepository initialized **");
		return true;
	}
	
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}

	@Override
	public List<Persona> getContacts() {
		List<Persona> allContacts = new ArrayList<Persona>();
		
		String sqlQuery = "SELECT id, name, surname, address, telephone_number, age "
						+ "FROM   Persona "
						+ "WHERE  userId = ? ";
		
		try {
			PreparedStatement stm = this.connection.prepareStatement(sqlQuery);
			stm.setInt(1, this.currentUser.getId());
			
			ResultSet result = stm.executeQuery();
			
			while(result.next()) {
				int contactId = result.getInt("id");
				String name = result.getString("name");
				String surname = result.getString("surname");
				String address = result.getString("address");
				String telephoneNumber = result.getString("telephone_number");
				Integer age = result.getInt("age");
				if (age == 0) { age = null; }
				
				Persona contact = new Persona(contactId, name, surname, address, telephoneNumber, age);
				allContacts.add(contact);
			}
			
			result.close();
			stm.close();
		} 
		catch (SQLException sqle) {
			System.err.println("An error occurred during getContacts()");
			sqle.printStackTrace();
			return null;
		}
		
		return allContacts;
	}

	@Override
	public void createNewContact(Persona contact) {
		String sqlInsert = "INSERT INTO Persona (userId, name, surname, address, telephone_number, age) "
					 	 + "VALUES (?, ?, ?, ?, ?, ?)";
				
		try {
			PreparedStatement stm = this.connection.prepareStatement(sqlInsert);
			stm.setInt(1, this.currentUser.getId());
			stm.setString(2, contact.getName());
			stm.setString(3, contact.getSurname());
			stm.setString(4, contact.getAddress());
			stm.setString(5, contact.getTelephoneNumber());
			stm.setInt(6, contact.getAge() == null ? 0 : contact.getAge());
			
			int numUpdatedRows = stm.executeUpdate();
			
			if (numUpdatedRows != 1) {
				System.err.println("Failed creation of new contact");
			}
			
			stm.close();
		}
		catch (SQLException sqle) {
			System.err.println("An error occurred during createNewContact()");
			sqle.printStackTrace();
		}
	}

	@Override
	public void editContact(Persona contact) {
		String sqlUpdate = "UPDATE Persona "
						 + "SET    name=?, surname=?, address=?, telephone_number=?, age=? "
						 + "WHERE  id=? ";
		
		try {
			PreparedStatement stm = this.connection.prepareStatement(sqlUpdate);
			stm.setString(1, contact.getName());
			stm.setString(2, contact.getSurname());
			stm.setString(3, contact.getAddress());
			stm.setString(4, contact.getTelephoneNumber());
			stm.setInt(5, contact.getAge() == null ? 0 : contact.getAge());
			stm.setInt(6, contact.getId());
			
			int numUpdatedRows = stm.executeUpdate();
			
			if (numUpdatedRows != 1) {
				System.err.println(String.format("Failed update of contact %s %s", contact.getName(), contact.getSurname()));
			}
			
			stm.close();
		}
		catch (SQLException sqle) {
			System.err.println("An error occurred during editContact()");
			sqle.printStackTrace();
		}
	}

	@Override
	public void deleteContact(Persona contact) {
		String sqlDelete = "DELETE FROM Persona "
						 + "WHERE  id=? ";

		try {
			PreparedStatement stm = this.connection.prepareStatement(sqlDelete);
			stm.setInt(1, contact.getId());
			
			int numUpdatedRows = stm.executeUpdate();
			
			if (numUpdatedRows != 1) {
				System.err.println(String.format("Failed delete of contact %s %s", contact.getName(), contact.getSurname()));
			}
			
			stm.close();
		}
		catch (SQLException sqle) {
			System.err.println("An error occurred during deleteContact()");
			sqle.printStackTrace();
		}
	}

	@Override
	public User checkUserLogin(User user) {
		String sqlQuery = "SELECT id, username, password_hash " + 
						  "FROM   User " + 
						  "WHERE  username = ? AND password_hash = ?";
		
		PreparedStatement stm;
		try {
			stm = this.connection.prepareStatement(sqlQuery);
			stm.setString(1, user.getUsername());
			stm.setString(2, user.getPasswordhash());

			ResultSet result = stm.executeQuery();
			User loggedUser = null;
			
			if(result.next()) {
				// (just one row)
				
				// user found: successful login
				int userId = result.getInt("id");
				String username = result.getString("username");
				String passwordHash = result.getString("password_hash");
				
				loggedUser = new User(userId, username, passwordHash);
			}
			
			result.close();
			stm.close();
			return loggedUser;
		} 
		catch (SQLException sqle) {
			System.err.println("An error occurred during checkUserLogin()");
			sqle.printStackTrace();
			return null;
		}
	}

}
