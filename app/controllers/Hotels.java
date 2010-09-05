package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Hotel;
import play.mvc.Controller;

import com.google.gson.Gson;

public class Hotels extends Controller {
	public static void list(int limit, int start) {
		List<Hotel> hotels = Hotel.all().fetch();

		Gson gson = new Gson();
		String data = gson.toJson(hotels);
		
		renderJSON("{hotels:" + data + "}");		
	}

	public static void create(List<Hotel> hotels) {
		if (hotels != null) {
			for (Hotel hotel : hotels) {
				hotel.save();
			}

			Gson gson = new Gson();
			String data = gson.toJson(hotels);
			
			renderJSON("{'success':true,'message':'The hotel(s) was create successfully', 'hotels':" + data + "}");
		}
	}

	public static void update(List<Hotel> hotels) {
		if (hotels != null) {
			for (Hotel h: hotels) {
				Hotel hotel = Hotel.findById(h.getId());
				
				hotel.fillFrom(h);
				hotel.save();								
			}

			Gson gson = new Gson();
			String data = gson.toJson(hotels);
			
			renderJSON("{'success':true,'message':'The hotel(s) was updated successfully', 'hotels':" + data + "}");
		}
	}

	public static void delete(List<Long> hotels) {
		for(Long id : hotels){
			Hotel.delete("from Hotel where id=?", id);
		}
		
		renderJSON("{'success':true,'message':'The hotel was deleted successfully'}");
	}
}
