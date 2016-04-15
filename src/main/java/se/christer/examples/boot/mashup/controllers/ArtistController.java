package se.christer.examples.boot.mashup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se.christer.examples.boot.mashup.domain.Artist;
import se.christer.examples.boot.mashup.services.MusicBeanzService;

@Controller
@RequestMapping("/artist/{mbid}")
public class ArtistController {
	
	@Autowired
	private MusicBeanzService mbService;
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Artist artist(@PathVariable("mbid") String mbid) {
		
		Artist mbArtist = mbService.getArtist(mbid);
		
		// Create new and aggregate from other  
		Artist response = new Artist();
		response.setMbid(mbArtist.getMbid());
		
		return response;
	}
}
