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

import se.christer.examples.boot.mashup.ArtistNotFoundException;
import se.christer.examples.boot.mashup.domain.Album;
import se.christer.examples.boot.mashup.domain.Artist;
import se.christer.examples.boot.mashup.services.CoverArtArchiveService;
import se.christer.examples.boot.mashup.services.MusicBrainzService;
import se.christer.examples.boot.mashup.services.MusicBrainzService.ReleaseGroup;
import se.christer.examples.boot.mashup.services.WikipediaService;

/**
 * Handles the artist resource in the REST API.
 * 
 * @author christer
 *
 */
@Controller
@RequestMapping("/artist/{mbid}")
public class ArtistController {

	@Autowired
	private MusicBrainzService mbService;

	@Autowired
	private WikipediaService wpService;

	@Autowired
	private CoverArtArchiveService caaService;

	/**
	 * Returns the aggregated information for an artist.
	 * 
	 * @param mbid
	 *            the MusicBrainz artist ID
	 * @return the JSON representation of the artist
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Artist artist(@PathVariable("mbid") String mbid) {
		// Get artist from MusicBrainz
		MusicBrainzService.Artist mbArtist = mbService.findArtist(mbid);
		if (mbArtist == null) {
			throw new ArtistNotFoundException(mbid);
		}

		// Create new artist and populate it
		Artist artist = new Artist();
		artist.setMbid(mbid);

		// Enrich artist with description from Wikipedia (if available)
		// TODO: Might want to use JSONPath for this instead of linear search.
		String wpPage = null;
		for (MusicBrainzService.Relation relation : mbArtist.getRelations()) {
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

		// Merge albums from MusicBrainz with front cover art from Cover Art
		// Archive
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
