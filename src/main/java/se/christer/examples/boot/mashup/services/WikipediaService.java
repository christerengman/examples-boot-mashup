package se.christer.examples.boot.mashup.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

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

	public String getExtract(String title) {
		RestTemplate mb = new RestTemplate();

		LOGGER.debug("baseURI: {}", getBaseURI());
		JsonNode root = mb.getForObject(getBaseURI().concat(API_PATH), JsonNode.class, title);
		
		return root.findValuesAsText("extract").get(0);

	}

}
