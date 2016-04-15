package se.christer.examples.boot.mashup.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import se.christer.examples.boot.mashup.domain.Artist;

@Service
@ConfigurationProperties(prefix="musicBeanz")
public class MusicBeanzService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(MusicBeanzService.class);
	
	private String baseURI;
	
	public Artist getArtist(String mbid) {
		RestTemplate mb = new RestTemplate();

		LOGGER.info("baseURI: {}", getBaseURI());
		Artist artist = mb.getForObject(getBaseURI().concat("/ws/2/artist/{mbid}?fmt=json&inc=url-rels+release-groups"), Artist.class, mbid);
		
		return artist;
		
	}

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}
}
