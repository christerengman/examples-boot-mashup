package se.christer.examples.boot.mashup.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se.christer.examples.boot.mashup.domain.Album;
import se.christer.examples.boot.mashup.domain.Artist;
import se.christer.examples.boot.mashup.services.MusicBeanzService;
import se.christer.examples.boot.mashup.services.MusicBeanzService.ReleaseGroup;
import se.christer.examples.boot.mashup.services.WikipediaService;

@Controller
@RequestMapping("/artist/{mbid}")
public class ArtistController {
	
	@Autowired
	private MusicBeanzService mbService;
	
	@Autowired
	private WikipediaService wpService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Artist artist(@PathVariable("mbid") String mbid) {
		// Create new artist and populate it  
		Artist artist = new Artist();
		artist.setMbid(mbid);

		// Get artist from MusicBrainz
		MusicBeanzService.Artist mbArtist = mbService.findArtist(mbid);

		// Get info from Wikipedia based on URL from MusicBeanz
		String wpPage = null;
		for (MusicBeanzService.Relation relation : mbArtist.getRelations()) {
			if ("wikipedia".equals(relation.getType())) {
				URI uri = relation.getUrl().getResource();
				String path = uri.getPath();
				wpPage = path.substring(path.lastIndexOf('/') + 1);
				break;
			}
		}
		String description = wpService.getExtract(wpPage);
		artist.setDescription(description);

		
		List<Album> albums = artist.getAlbums();
		for (ReleaseGroup rg : mbArtist.getReleaseGroups()) {
			Album album = new Album();
			album.setId(rg.getId());
			album.setTitle(rg.getTitle());
			albums.add(album);
		}
		
		return artist;
	}
}
