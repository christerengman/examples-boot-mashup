package se.christer.examples.boot.mashup;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such artist")
public class ArtistNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String mbid;

	public ArtistNotFoundException(String mbid) {
		this.mbid = mbid;
	}
	
	@Override
	public String getMessage() {
		return "Artist with ID ".concat(mbid).concat(" was not found!");
	}

}
