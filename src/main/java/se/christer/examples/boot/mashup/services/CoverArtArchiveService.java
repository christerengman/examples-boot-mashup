package se.christer.examples.boot.mashup.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

@Service
@ConfigurationProperties(prefix = "coverArtArchive")
public class CoverArtArchiveService {
	private static final String API_PATH = "/release-group/{id}";

	private static Logger LOGGER = LoggerFactory.getLogger(CoverArtArchiveService.class);

	private String baseURI;

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	/**
	 * Finds front cover art URL for an release-group
	 * @param id the MusicBrainz release-group id
	 * @return URL of front cover art image, or null if not available 
	 */
	public URL getCoverArt(String id) {
		RestTemplate api = new RestTemplate();

		LOGGER.debug("baseURI: {}", getBaseURI());
		try {
			String json = api.getForObject(getBaseURI().concat(API_PATH), String.class, id);
			List<String> fronts = JsonPath.parse(json).read("$..images[?(@.front == true)].image");
			try {
				return new URL(fronts.get(0));
			} catch (MalformedURLException e) {
				return null;
			}
		} catch (RestClientException e) {
			LOGGER.debug(e.getMessage());
			return null;
		}
	}
}
