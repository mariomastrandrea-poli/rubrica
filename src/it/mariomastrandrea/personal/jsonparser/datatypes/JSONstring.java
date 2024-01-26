package it.mariomastrandrea.personal.jsonparser.datatypes;

/**
 * A concrete implementation of a generic JSON object. It represent a string value
 * @author mariomastrandrea
 */
public class JSONstring extends JSONcomponent {
	private String string;
	
	
	public JSONstring(String string) {
		this.string = string;
	}
	
	public String get() { return this.string; }
	
	@Override
	public String print(int indentation) {
		return String.format("\"%s\"", this.string);
	}

	@Override
	public JSONcomponent removeProperties(String... properties) {
		return this;
	}
}

