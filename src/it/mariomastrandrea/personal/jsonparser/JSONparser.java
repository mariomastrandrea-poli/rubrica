package it.mariomastrandrea.personal.jsonparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import it.mariomastrandrea.personal.jsonparser.datatypes.JSONarray;
import it.mariomastrandrea.personal.jsonparser.datatypes.JSONbool;
import it.mariomastrandrea.personal.jsonparser.datatypes.JSONcomponent;
import it.mariomastrandrea.personal.jsonparser.datatypes.JSONnull;
import it.mariomastrandrea.personal.jsonparser.datatypes.JSONnumber;
import it.mariomastrandrea.personal.jsonparser.datatypes.JSONobject;
import it.mariomastrandrea.personal.jsonparser.datatypes.JSONstring;

public class JSONparser {
	public JSONparser() { }
	
	public JSONcomponent parseFile(String filePath) {
		File configFile = new File(filePath);
		JSONcomponent parsedJson;
		
		try {
			FileReader fileReader = new FileReader(configFile);
			BufferedReader br = new BufferedReader(fileReader);
			
			String firstLine = br.readLine();
			
			if (firstLine == null) {
				br.close();
				return null;
			}
			
			parsedJson = this.parse(new StringBuilder(firstLine), br);
			br.close();
		} 
		catch (FileNotFoundException fnfe) {
			System.err.println(String.format("File not found at %s", filePath));
			fnfe.printStackTrace();
			return null;
		} 
		catch (IOException ioe) {
			System.err.println("An unexpected I/O error occurred during JSONparser.parseFile()");
			ioe.printStackTrace();
			return null;
		}
		
		return parsedJson;
	}
	
	/**
	 * It parses a generic JSON object from an input buffered reader, using a recursive algorithm
	 * @param currentLine
	 * @param br buffered reader linked to the input JSON text
	 * @return the corresponding JSON object
	 * @throws IOException
	 */
	private JSONcomponent parse(StringBuilder currentLine, BufferedReader br) throws IOException 
	{		
		String currentLineString = currentLine != null ? currentLine.toString() : null;
		
		if(currentLineString == null) {
			currentLineString = br.readLine();
			if(currentLineString == null) return null;
		}
		
		// ignore blank strings
		while(currentLineString.isBlank()) {
			currentLineString = br.readLine();
			if(currentLineString == null) return null;
		}
		
		// remove whitespace characters
		currentLine.delete(0, currentLine.length());
		currentLine.append(currentLineString.trim());		
		char firstChar = currentLine.charAt(0);
		
		if(firstChar == '{')  	// there is a JSON object
			return this.parseObject(currentLine, br);
		
		if(firstChar == '[') 	// there is a JSON array
			return this.parseArray(currentLine, br);
		
		if(firstChar == '"') 	// there is a string
			return this.parseString(currentLine);
		
		JSONcomponent result;
		
		if((result = this.parseNull(currentLine)) != null) // there is a null value
			return result;
		
		if((result = this.parseBool(currentLine)) != null) // there is a boolean value
			return result;
		
		// it may be a number	
		return this.parseNumber(currentLine, br);
	}
	
	/**
	 * It parses a JSON object (delimited by '{' and '}')
	 * @param currentLine buffer containing the next characters that will be analysed
	 * @param br buffered reader linked to the input JSON text
	 * @return the JSON object itself
	 * @throws IOException
	 */
	private JSONobject parseObject(StringBuilder currentLine, BufferedReader br) throws IOException
	{
		// remove starting '{'
		String currentLineString = currentLine.deleteCharAt(0).toString();
		
		// remove blank lines
		while(currentLineString.isBlank()) {
			currentLineString = br.readLine();
			if(currentLineString == null) return null;
		}
		
		// remove whitespaces
		currentLineString = currentLineString.trim();
		currentLine.delete(0, currentLine.length());
		currentLine.append(currentLineString);
			
		JSONobject object = new JSONobject();
		
		while(!currentLineString.startsWith("}")) {
			
			String fields[] = currentLineString.split(":", 2);
			String key = fields[0].trim();
			key = key.substring(1, key.length() - 1);
			
			// update current line buffer: removing 'key:'
			currentLine.delete(0, fields[0].length() + 1); 
			
			JSONcomponent value = this.parse(currentLine, br);
			object.addProperty(key, value);
			
			// remove comma separator
			if(!currentLine.isEmpty() && currentLine.charAt(0) == ',') 
				currentLine.deleteCharAt(0); 
			
			currentLineString = currentLine.toString();
			
			while(currentLineString.isBlank()) {
				currentLineString = br.readLine();
				if(currentLineString == null) break;
			}
			
			currentLineString = currentLineString.trim();
			currentLine.delete(0, currentLine.length());
			currentLine.append(currentLineString);
		}
		
		// remove starting '{'
		currentLine.deleteCharAt(0);
		
		return object;
	}

	/**
	 * It parses a JSON array of generic objects, delimited by '[' and ']'
	 * @param currentLine buffer containing the next characters that will be analysed
	 * @param br buffered reader linked to the input JSON text
	 * @return the JSON array itself
	 * @throws IOException
	 */
	private JSONarray parseArray(StringBuilder currentLine, BufferedReader br) throws IOException
	{
		// remove starting '['
		String currentLineString = currentLine.deleteCharAt(0).toString();
				
		// removing blank lines
		while(currentLineString.isBlank()) {
			currentLineString = br.readLine();
			if(currentLineString == null) return null;
		}
				
		// remove whitespaces
		currentLineString = currentLineString.trim();
		currentLine.delete(0, currentLine.length());
		currentLine.append(currentLineString);
		
		JSONarray array = new JSONarray();
		
		while(!currentLineString.startsWith("]")) {
					
			JSONcomponent nextComponent = this.parse(currentLine, br);
			array.add(nextComponent);
			
			// remove comma separator
			if(!currentLine.isEmpty() && currentLine.charAt(0) == ',') 
				currentLine.deleteCharAt(0); 
			
			currentLineString = currentLine.toString();
			
			while(currentLineString.isBlank()) {
				currentLineString = br.readLine();
				if(currentLineString == null) break;
			}
			
			currentLineString = currentLineString.trim();
			currentLine.delete(0, currentLine.length());
			currentLine.append(currentLineString);
		}
		
		// remove starting '['
		currentLine.deleteCharAt(0);
			
		return array;
	}

	/**
	 * It parses a JSON string value, delimited by double quotes (")
	 * @param currentLine buffer containing the next characters that will be analysed
	 * @param br buffered reader linked to the input JSON text
	 * @return A JSONstring object associated to the parsed string value
	 * @throws IOException
	 */
	private JSONstring parseString(StringBuilder currentLine) throws IOException
	{
		// remove starting '"'
		String currentLineString = currentLine.deleteCharAt(0).toString();
		
		String[] fields = currentLineString.split("\"", 2);
		String stringValue = fields[0];	//retrieving String value
		
		//check if there is an escaped '"' character
		while(stringValue.length() > 0 && 
				stringValue.charAt(stringValue.length() - 1) == '\\' &&
				!(stringValue.length() > 1 && stringValue.charAt(stringValue.length() - 2) == '\\'))
		{
			fields = fields[1].split("\"", 2);
			stringValue = stringValue + '"' + fields[0];
		}
		
		// update current line buffer: delete the string value and trailing '"'
		currentLine.delete(0, stringValue.length()+1);
		
		return new JSONstring(stringValue);
	}
	
	private JSONnull parseNull(StringBuilder currentLine) 
	{
		if(currentLine.length() >= 4 && 
				currentLine.substring(0, 4).equals("null"))	{ // there is a null value
			currentLine.delete(0, 4); //remove 'null'
			return new JSONnull();
		}
		
		return null;
	}
	
	private JSONbool parseBool(StringBuilder currentLine) 
	{
		if(currentLine.length() >= 4 && 
				currentLine.substring(0,4).toLowerCase().equals("true")) { // there is a 'true' boolean value
			currentLine.delete(0, 4);
			return new JSONbool(true);
		}
		
		if(currentLine.length() >= 5 && 
				currentLine.substring(0,5).toLowerCase().equals("false")) { // there is a 'false' boolean value
			currentLine.delete(0, 5);
			return new JSONbool(false);
		}
		
		return null;
	}
	
	/**
	 * It parses a generic JSON number 
	 * @param currentLine
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private JSONnumber parseNumber(StringBuilder currentLine, BufferedReader br) throws IOException
	{
		String possibleNumber = this.retrievePossibleLeadingDouble(currentLine.toString());
		
		try
		{
			double number = Double.parseDouble(possibleNumber);
			currentLine.delete(0, possibleNumber.length());
			return new JSONnumber(number, possibleNumber);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Number format exception with line: \"" + currentLine + "\"");	//for debugging
		}
		
		return null;
	}
	
	/**
	 * This method returns the leading possible JSON number contained in a string
	 * @param line
	 * @return The starting chars sequence containing an optional only digits, 
	 * at most one dot and an optional starting '-'
	 */
	private String retrievePossibleLeadingDouble(String line) {
		
		char[] chars = line.toCharArray();
		int resultPointer = 0;
		boolean dotFlag = false;
		
		for(char c : chars) {
			//possible chars are only digits, one dot and an optional starting '-'
			if((!Character.isDigit(c) && c != '.' && c != '-' && resultPointer > 0) || (c == '.' && dotFlag))	
				break;
			
			if(c == '.') dotFlag = true;
			resultPointer++;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<resultPointer; i++)
			sb.append(chars[i]);
		
		return sb.toString();
	}
}
