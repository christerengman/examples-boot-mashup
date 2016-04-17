package se.christer.examples.boot.mashup.domain;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Representation object for an album
 * 
 * @author christer
 *
 */
public class Album {
	private String id;
	private String title;
	@JsonIgnore
	private URI coverArt;

	public Album() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URI getCoverArt() {
		return coverArt;
	}

	public void setCoverArt(URI coverArt) {
		this.coverArt = coverArt;
	}

}
