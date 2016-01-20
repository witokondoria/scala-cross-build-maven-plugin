package com.stratio.mojo.scala.crossbuild;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import static org.fest.assertions.Assertions.*;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class ArtifactIdRewriteRuleTest {

  private XMLEventReader mockXMLEventReader(final String artifactId) throws XMLStreamException {
    final XMLEventReader reader = mock(XMLEventReader.class);
    when(reader.getElementText()).thenReturn(artifactId);
    final XMLEvent nextEvent = mock(XMLEvent.class);
    when(reader.peek()).thenReturn(nextEvent);
    final Location nextLocation = mock(Location.class);
    when(nextLocation.getCharacterOffset()).thenReturn(0);
    when(nextEvent.getLocation()).thenReturn(nextLocation);
    return reader;
  }

  @Test
  public void noStartDocument() throws XMLStreamException {
    final String oldArtifactId = "ABC_2.10";
    final String newVersion = "2.11";
    final RewriteRule rule = new ArtifactIdRewriteRule(newVersion);
    final XMLEventReader reader = mockXMLEventReader(oldArtifactId);
    final XMLEvent event = mock(XMLEvent.class);
    when(event.isStartElement()).thenReturn(false);
    assertThat(rule.replace(Arrays.asList("project", "artifactId"), reader, event))
        .isEmpty();
  }

  @Test
  public void badPath() throws XMLStreamException {
    final String oldArtifactId = "ABC_2.10";
    final String newVersion = "2.11";
    final RewriteRule rule = new ArtifactIdRewriteRule(newVersion);
    final XMLEventReader reader = mockXMLEventReader(oldArtifactId);
    final XMLEvent event = mock(XMLEvent.class);
    when(event.isStartElement()).thenReturn(true);
    assertThat(rule.replace(Collections.<String>emptyList(), reader, event))
      .isEmpty();
    assertThat(rule.replace(Arrays.asList("project"), reader, event))
        .isEmpty();
    assertThat(rule.replace(Arrays.asList("artifactId"), reader, event))
        .isEmpty();
    assertThat(rule.replace(Arrays.asList("project", "artifactId", "other"), reader, event))
        .isEmpty();
  }

  @Test
  public void noArtifactChange() throws XMLStreamException {
    final String oldArtifactId = "ABC_2.10";
    final String newVersion = "2.10";
    final RewriteRule rule = new ArtifactIdRewriteRule(newVersion);
    final XMLEventReader reader = mockXMLEventReader(oldArtifactId);
    final XMLEvent event = mock(XMLEvent.class);
    when(event.isStartElement()).thenReturn(true);
    assertThat(rule.replace(
        Arrays.asList("project", "artifactId"),
        reader,
        event
    )).isEmpty();
  }

  @Test
  public void replacement() throws XMLStreamException {
    final String oldArtifactId = "ABC_2.10";
    final String newArtifactId = "ABC_2.11";
    final String newVersion = "2.11";
    final RewriteRule rule = new ArtifactIdRewriteRule(newVersion);
    final XMLEventReader reader = mockXMLEventReader(oldArtifactId);
    final XMLEvent event = mock(XMLEvent.class);
    when(event.isStartElement()).thenReturn(true);
    assertThat(rule.replace(
        Arrays.asList("project", "artifactId"),
        reader,
        event
    )).containsExactly(new Replacement(0, oldArtifactId.length(), newArtifactId.getBytes()));
  }

}
