package com.stratio.mojo.scala.crossbuild;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

class ArtifactIdRewriteRule implements RewriteRule {

  private final String newVersion;

  public ArtifactIdRewriteRule(final String newVersion) {
    this.newVersion = newVersion;
  }

  @Override
  public List<Replacement> replace(final List<String> path, final XMLEventReader reader, final XMLEvent event) throws
      XMLStreamException {
    if (!event.isStartElement()) {
      return Collections.emptyList();
    }
    if (path.size() != 2 || !"project".equals(path.get(0)) || !"artifactId".equals(path.get(1))) {
      return Collections.emptyList();
    }
    final ScalaVersionManipulation svm = new ScalaVersionManipulation();
    final int offset = reader.peek().getLocation().getCharacterOffset();
    final String oldArtifactId = reader.getElementText().trim();
    final String newArtifactId = svm.changeScalaVersionInArtifactId(oldArtifactId, newVersion);
    if (oldArtifactId.equals(newArtifactId)) {
      return Collections.emptyList();
    }

    final byte[] replacement = newArtifactId.getBytes(StandardCharsets.UTF_8);
    final int length = oldArtifactId.getBytes(StandardCharsets.UTF_8).length;
    final Replacement occurrence = new Replacement(offset, length, replacement);
    return Collections.singletonList(occurrence);
  }
}
