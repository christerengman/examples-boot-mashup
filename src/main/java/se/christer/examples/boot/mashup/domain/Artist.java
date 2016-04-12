package se.christer.examples.boot.mashup.domain;

public class Artist {
	private final String mbid;
	private final String description;
	
	public Artist(String mbid, String description) {
		this.mbid = mbid;
		this.description = description;
	}

	public String getMbid() {
		return mbid;
	}

	public String getDescription() {
		return description;
	}
}
