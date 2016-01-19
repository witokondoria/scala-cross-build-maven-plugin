package com.stratio.mojo.scala.crossbuild;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;

class FindOccurrences {

  public List<Occurrence> find(final File file, final String newVersion) throws IOException, XMLStreamException {
    final Stack<String> currentPath = new Stack<>();
    final List<Occurrence> occurrences = new ArrayList<>();
    final ScalaVersionManipulation svm = new ScalaVersionManipulation();

    Integer length = null;
    byte[] replacement = null;

    final XMLInputFactory xmlif = XMLInputFactory.newFactory();
    final XMLEventReader reader = xmlif.createXMLEventReader(file.toString(), new FileInputStream(file));
    while (reader.hasNext()) {
      final XMLEvent event = reader.nextEvent();
      if (event.isStartElement()) {
        final StartElement startElement = event.asStartElement();
        final String elementName = startElement.getName().getLocalPart();
        currentPath.push(elementName);
        final String[] arrayPath = currentPath.toArray(new String[currentPath.size()]);
        if (arrayPath.length == 2 && "project".equals(arrayPath[0]) && "artifactId".equals(arrayPath[1])) {
          int offset = reader.peek().getLocation().getCharacterOffset();
          final String oldArtifactId = reader.getElementText().trim();
          final String newArtifactId = svm.changeScalaVersionInArtifactId(oldArtifactId, newVersion);
          if (!oldArtifactId.equals(newArtifactId)) {
            replacement = newArtifactId.getBytes(StandardCharsets.UTF_8);
            length = oldArtifactId.getBytes(StandardCharsets.UTF_8).length;
            final Occurrence occurrence = new Occurrence(offset, length, replacement);
            occurrences.add(occurrence);
          }
        }
      } else if (event.isEndElement()) {
        currentPath.pop();
      }
    }
    reader.close();
    return occurrences;
  }

  /**
   * Skips events until characters start. Throws if END_ELEMENT is found early.
   *
   * @param reader
   */
  private static void skipUntilTagTextStarts(final XMLEventReader reader) {
    //while (reader.peek().isCharacters())
  }
}
