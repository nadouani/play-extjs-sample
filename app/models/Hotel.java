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
	
	public Hotel(String name, String website, String phoneNumber, String city,
			String country, Integer rooms, Integer beds, Integer stars) {
		super();
		this.name = name;
		this.website = website;
		this.phoneNumber = phoneNumber;
		this.city = city;
		this.country = country;
		this.rooms = rooms;
		this.beds = beds;
		this.stars = stars;
	}
	
	public void fillFrom(Hotel hotel){
		this.name = hotel.name;
		this.website = hotel.website;
		this.phoneNumber = hotel.phoneNumber;
		this.city = hotel.city;
		this.country = hotel.country;
		this.rooms = hotel.rooms;
		this.beds = hotel.beds;
		this.stars = hotel.stars;
	}
}
