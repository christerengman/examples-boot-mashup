package se.christer.examples.boot.mashup.domain;

import java.net.URI;

/**
 * Representation object for an album
 *  
 * @author christer
 *
 */
public class Album {
	private final String id;
	private final String title;
	private final URI coverArt;

	public Album(String id, String title, URI coverArt) {
		this.id = id;
		this.title = title;
		this.coverArt = coverArt;
	}
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public URI getCoverArt() {
		return coverArt;
	}
}
