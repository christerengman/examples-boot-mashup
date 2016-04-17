package se.christer.examples.boot.mashup.controllers;

import java.net.URI;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se.christer.examples.boot.mashup.domain.Album;
import se.christer.examples.boot.mashup.domain.Artist;
import se.christer.examples.boot.mashup.services.CoverArtArchiveService;
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
	
	@Autowired
	private CoverArtArchiveService caaService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Artist artist(@PathVariable("mbid") String mbid) {
		// Create new artist and populate it  
		Artist artist = new Artist();
		artist.setMbid(mbid);

		// Get artist from MusicBrainz
		MusicBeanzService.Artist mbArtist = mbService.findArtist(mbid);

		// Enrich artist with description from Wikipedia (if available)
		String wpPage = null;
		for (MusicBeanzService.Relation relation : mbArtist.getRelations()) {
			if ("wikipedia".equals(relation.getType())) {
				URI uri = relation.getUrl().getResource();
				String path = uri.getPath();
				wpPage = path.substring(path.lastIndexOf('/') + 1);
				break;
			}
		}
		if (wpPage != null) {
			String description = wpService.getExtract(wpPage);
			artist.setDescription(description);
		}


		// Enrich albums with front cover art from Cover Art Archive
		List<Album> albums = artist.getAlbums();
		for (ReleaseGroup rg : mbArtist.getReleaseGroups()) {
			Album album = new Album();
			
			album.setId(rg.getId());
			album.setTitle(rg.getTitle());
			
			URL coverArt = caaService.getCoverArt(rg.getId());
			album.setCoverArt(coverArt);
			
			albums.add(album);
		}
		
		return artist;
	}
}
