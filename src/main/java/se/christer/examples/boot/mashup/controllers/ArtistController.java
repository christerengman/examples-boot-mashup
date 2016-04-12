package se.christer.examples.boot.mashup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se.christer.examples.boot.mashup.domain.Artist;

@Controller
@RequestMapping("/artist/{mbid}")
public class ArtistController {
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Artist artist(@PathVariable("mbid") String mbid) {
		return new Artist(mbid, "Nirvana DUMMY");
	}
}
