package com.stratio.mojo.scala.crossbuild;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

class PropertyRewriteRule implements RewriteRule {

  private final String property;
  private final String newValue;

  public PropertyRewriteRule(final String property, final String newValue) {
    this.property = property;
    this.newValue = newValue;
  }

  @Override
  public List<Replacement> replace(final List<String> path, final XMLEventReader reader, final XMLEvent event) throws
      XMLStreamException {
    if (!event.isStartElement()) {
      return Collections.emptyList();
    }
    if (path.size() != 3 || !"project".equals(path.get(0)) || !"properties".equals(path.get(1)) || !property.equals(path.get(2))) {
      return Collections.emptyList();
    }
    final int offset = reader.peek().getLocation().getCharacterOffset();
    final String oldValue = reader.getElementText().trim();
    if (newValue.equals(oldValue)) {
      return Collections.emptyList();
    }

    final byte[] replacement = newValue.getBytes(StandardCharsets.UTF_8);
    final int length = oldValue.getBytes(StandardCharsets.UTF_8).length;
    final Replacement occurrence = new Replacement(offset, length, replacement);
    return Collections.singletonList(occurrence);
  }
}
