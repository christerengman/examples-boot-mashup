package se.christer.examples.boot.mashup.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation object for an artist
 * 
 * @author christer
 *
 */

public class Artist {
	private String mbid;

	private String description;

	private List<Album> albums = new ArrayList<Album>();

	public Artist() {
	}

	public String getMbid() {
		return mbid;
	}

	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Album> getAlbums() {
		return this.albums;
	}

	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}
}
