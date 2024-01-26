package it.mariomastrandrea.personal.jsonparser.datatypes;

/**
 * A concrete implementation of a generic JSON object. It represent a numeric value, 
 * either an integer or a floating point value
 * @author mariomastrandrea
 *
 */
public class JSONnumber extends JSONcomponent
{
	private Double doubleNumber;
	private Long longNumber;
	private String stringRepresentation;
	
	
	public JSONnumber(double number)
	{
		this.doubleNumber = number;
		this.longNumber = null;
	}
	
	public JSONnumber(long number)
	{
		this.longNumber = number;
		this.doubleNumber = null;
	}
	
	public JSONnumber(double number, String possibleNumber)
	{
		this(number);
		this.stringRepresentation = possibleNumber;
	}

	public double getDouble() { return this.doubleNumber; }
	public long getLong() { return this.longNumber; }
	public double get() { return this.doubleNumber != null ? this.doubleNumber : (double)this.longNumber; }
	
	@Override
	public String print(int indentation) 
	{
		if(this.stringRepresentation != null)
			return this.stringRepresentation;
		
		if(this.doubleNumber != null)
			return String.valueOf(this.doubleNumber);
		
		if(this.longNumber != null)
			return String.valueOf(this.longNumber);
		
		return null;
	}

	@Override
	public JSONcomponent removeProperties(String... properties)
	{
		return this;
	}
}
