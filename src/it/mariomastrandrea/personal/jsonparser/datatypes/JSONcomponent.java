package it.mariomastrandrea.personal.jsonparser.datatypes;

/**
 * An abstract class for a generic JSON object: it can be either a 'concrete' 
 * 	JSON object (associative array), a JSON array, a string, a number, a boolean or a 'null' value
 * @author mariomastrandrea
 *
 */
public abstract class JSONcomponent
{
	/**
	 * Print the current JSON object respecting the right indentation
	 * @param indentation Starting indentation value (0 -> at the line beginning; 
	 * 	1 -> with one leading '\t'; 2 -> with two leading '\t'; and so on)
	 * @return A String representation of the JSON object itself
	 */
	public abstract String print(int indentation);
	
	/**
	 * It removes properties from the current JSON object, using a recursive algorithm
	 * @param properties Properties to be removed
	 * @return A reference to the cleaned JSON object, without the undesired properties
	 */
	public abstract JSONcomponent removeProperties(String... properties);
}
