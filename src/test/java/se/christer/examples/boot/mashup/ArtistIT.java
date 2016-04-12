package se.christer.examples.boot.mashup;

import org.junit.Test;

import com.consol.citrus.annotations.CitrusXmlTest;
import com.consol.citrus.junit.AbstractJUnit4CitrusTest;

public class ArtistIT extends AbstractJUnit4CitrusTest {

    @Test
    @CitrusXmlTest(name = "Artist-Nirvana")
	public void gettingNirvanaShouldReturnValidData() {
	}

}
