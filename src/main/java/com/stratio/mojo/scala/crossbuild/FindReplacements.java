package com.stratio.mojo.scala.crossbuild;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

class FindReplacements {

  private final List<RewriteRule> rewriteRules;

  public FindReplacements(final List<RewriteRule> rewriteRules) {
    this.rewriteRules = rewriteRules;
  }

  public List<Replacement> find(final File file) throws IOException, XMLStreamException {
    final List<Replacement> replacements = new ArrayList<>();
    final XMLInputFactory xmlif = XMLInputFactory.newFactory();
    final PathTrackingXMLEventReader reader = new PathTrackingXMLEventReader(
        xmlif.createXMLEventReader(file.toString(), new FileInputStream(file))
    );
    while (reader.hasNext()) {
      final XMLEvent event = reader.nextEvent();
      for (final RewriteRule rewriteRule: rewriteRules) {
        replacements.addAll(rewriteRule.replace(reader.currentPath(), reader, event));
      }
    }
    reader.close();
    return replacements;
  }

}
