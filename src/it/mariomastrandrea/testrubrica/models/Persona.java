package it.mariomastrandrea.testrubrica.models;

public class Persona {
	private Integer id;
	private String name;
	private String surname;
	private String address;
	private String telephoneNumber;
	private Integer age;
		
	public Persona(Integer id, String name, String surname, String address, String telephoneNumber, Integer age) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.telephoneNumber = telephoneNumber;
		this.age = age;
	}
	
	public Persona clone() {
		return new Persona(this.id, this.name, this.surname, this.address, this.telephoneNumber, this.age);
	}

	public int getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public String getSurname() {
		return this.surname;
	}

	public String getAddress() {
		return this.address;
	}

	public String getTelephoneNumber() {
		return this.telephoneNumber;
	}

	public Integer getAge() {
		return this.age;
	}
	
	public boolean isIdenticalTo(Persona p) {
		return ((this.id == null && p.address == null) || (this.id != null && this.id.equals(p.id))) && 
				this.name.equals(p.name) && 
				this.surname.equals(p.surname) && 
				this.address.equals(p.address) &&
				this.telephoneNumber.equals(p.telephoneNumber) &&
				((this.age == null && p.age == null) || (this.age != null && this.age.equals(p.age)));
	}
}
