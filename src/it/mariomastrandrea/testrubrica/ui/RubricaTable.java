package it.mariomastrandrea.testrubrica.ui;

import java.awt.Font;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import it.mariomastrandrea.testrubrica.models.Persona;
import it.mariomastrandrea.testrubrica.repository.RubricaRepository;

public class RubricaTable {
	static String[] columnNames = { "Nome", "Cognome", "Telefono" };
	static final float fontSize = 15.0f;
	static final float headerFontSize = Float.valueOf(fontSize * 1.08f);
	static final float rowHeightMultiplier = 1.45f;
	
	private RubricaRepository repository;
	private DefaultTableModel tableModel;
	private List<Persona> showedContacts;
	private Persona selectedContact;
	private int selectedRow;

	public RubricaTable(RubricaRepository repository) {
		this.repository = repository;
		this.showedContacts = this.repository.getContacts();
		this.selectedContact = null;
		this.selectedRow = -1;
	}
	
	public Persona getSelectedContact() {
		return this.selectedContact;
	}
	
	public JTable create() {
		String[][] rawContacts = this.getRowContactsFromRepository();

		// create non editable table and insert data
		NonEditableTableModel nonEditableDataModel = new NonEditableTableModel(rawContacts, columnNames);
		JTable table = new JTable(nonEditableDataModel);
		
		// disable multiple selection
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    // set table font size and height
	    Font sizedFont = table.getFont().deriveFont(fontSize);
	    table.setFont(sizedFont);
	    table.setRowHeight((int)(table.getRowHeight() * rowHeightMultiplier));
	    
	    // set header font size and height
	    JTableHeader header = table.getTableHeader();
	    header.setFont(header.getFont().deriveFont(headerFontSize));
	    
	    // save table model
	    this.tableModel = nonEditableDataModel;
	    
	    // add a listener for each selection
	    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        @Override
	        public void valueChanged(ListSelectionEvent e) {
	            // Check if it is a valid event
	            if (!e.getValueIsAdjusting()) {
	                int newSelectedRow = table.getSelectedRow();
	                
	                if (newSelectedRow == -1) {
	                	selectedContact = null;
	                	selectedRow = newSelectedRow;
	                }
	                else if (newSelectedRow != selectedRow){
	                	// update selected Contact
	                	selectedContact = showedContacts.get(newSelectedRow).clone();
	                	selectedRow = newSelectedRow;
	                }
	            }
	        }
	    });
	    
	    return table;
	}

	public void deleteContact(Persona contact) {
		this.repository.deleteContact(contact);
		this.updateTableContacts();
	}
	
	public void createNewContact(Persona contact) {
		if (contact == null) {
			return;
		}
		
		// perform action and update table
		this.repository.createNewContact(contact);
		this.updateTableContacts();
	}
	
	public void editContact(Persona contact) {
		if (contact == null) {
			return;
		}
		
		this.repository.editContact(contact);
		this.updateTableContacts();
	}
	
	private String[][] getRowContactsFromRepository() {
		List<Persona> contacts = this.repository.getContacts();
		this.showedContacts = contacts;
		String[][] rawContacts = new String[contacts.size()][columnNames.length];
		
		int index = 0;
		for (Persona p : contacts) {
			String[] array = new String[columnNames.length];
			
			array[0] = p.getName();
			array[1] = p.getSurname();
			array[2] = p.getTelephoneNumber();
			
			rawContacts[index] = array;
			index += 1;
		}
		
		return rawContacts;
	}
	
	private void updateTableContacts() {
		String[][] newRawContacts = this.getRowContactsFromRepository();
		
		// remove existing data
		this.tableModel.setRowCount(0);
		
		for (String[] row: newRawContacts) {
			this.tableModel.addRow(row);
		}
		
		this.selectedContact = null;
		this.selectedRow = -1;
	}
	
}

class NonEditableTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;

	public NonEditableTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // This will make all cells non-editable
        return false;
    }
}
