package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Hotel extends Model{
		
	public String name;
	public String website;
	public String phoneNumber;
	public String city;
	public String country;
	public Integer rooms;
	public Integer beds;
	public Integer stars;
	
	public String toString(){
		return name;
	}
}
