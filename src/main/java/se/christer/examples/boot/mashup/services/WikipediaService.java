package se.christer.examples.boot.mashup.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Service communicating with the Wikipedia API.
 * 
 * @author christer
 *
 */
@Service
@ConfigurationProperties(prefix = "wikipedia")
public class WikipediaService {
	private static final String API_PATH = "/w/api.php?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles={title}";

	private static Logger LOGGER = LoggerFactory.getLogger(WikipediaService.class);

	private String baseURI;

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	/**
	 * Returns an extract (short version) of the description of a page in
	 * Wikipedia
	 * 
	 * @param title
	 *            the title of the Wikipedia page to get the extract from
	 * @return the extract of the page or null if not found
	 */
	@Cacheable("wpExtracts")
	public String getExtract(String title) {
		RestTemplate mb = new RestTemplate();

		LOGGER.debug("baseURI: {}", getBaseURI());
		try {
			JsonNode root = mb.getForObject(getBaseURI().concat(API_PATH), JsonNode.class, title);

			List<String> extracts = root.findValuesAsText("extract");
			if (!extracts.isEmpty()) {
				return extracts.get(0);
			} else {
				return null;
			}
		} catch (RestClientException e) {
			LOGGER.debug(e.getMessage());
			return null;
		}
	}

}
