package se.christer.examples.boot.mashup.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Service communicating with the MusicBrainz API
 * 
 * @author christer
 *
 */
@Service
@ConfigurationProperties(prefix = "musicBrainz")
public class MusicBrainzService {

	private static final String API_PATH = "/ws/2/artist/{mbid}?fmt=json&inc=url-rels+release-groups";

	private static Logger LOGGER = LoggerFactory.getLogger(MusicBrainzService.class);

	private String baseURI;

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	/**
	 * Finds and returns an artist.
	 * 
	 * @param mbid
	 *            the MusicBrainz artist ID
	 * @return the artist if found, or null if not found
	 */
	@Cacheable("mbArtists")
	public Artist findArtist(String mbid) {
		RestTemplate mb = new RestTemplate();

		LOGGER.debug("baseURI: {}", getBaseURI());
		try {
			Artist artist = mb.getForObject(getBaseURI().concat(API_PATH), Artist.class, mbid);
			return artist;
		} catch (RestClientException e) {
			LOGGER.debug(e.getMessage());
			return null;
		}
	}

	/**
	 * Representation of an MusicBrainz artist
	 * 
	 * @author christer
	 *
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Artist {
		private String id;

		private String name;

		@JsonProperty("release-groups")
		private List<ReleaseGroup> releaseGroups = new ArrayList<ReleaseGroup>();

		private List<Relation> relations = new ArrayList<Relation>();

		public Artist() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<ReleaseGroup> getReleaseGroups() {
			return releaseGroups;
		}

		public void setReleaseGroups(List<ReleaseGroup> releaseGroups) {
			this.releaseGroups = releaseGroups;
		}

		public List<Relation> getRelations() {
			return relations;
		}

		public void setRelations(List<Relation> relations) {
			this.relations = relations;
		}
	}

	/**
	 * Representation of an MusicBrainz artist release group
	 * 
	 * @author christer
	 *
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ReleaseGroup {
		private String id;
		private String title;

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
	}

	/**
	 * Representation of an MusicBrainz artist relation
	 * 
	 * @author christer
	 *
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Relation {
		private String type;
		private RelationURL url;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public RelationURL getUrl() {
			return url;
		}

		public void setUrl(RelationURL url) {
			this.url = url;
		}
	}

	/**
	 * Representation of an MusicBrainz artist relation URL
	 * 
	 * @author christer
	 *
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RelationURL {
		private URI resource;

		public URI getResource() {
			return resource;
		}

		public void setResource(URI resource) {
			this.resource = resource;
		}
	}
}
