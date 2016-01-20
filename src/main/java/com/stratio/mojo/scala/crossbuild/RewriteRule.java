package com.stratio.mojo.scala.crossbuild;

import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

interface RewriteRule {
  List<Replacement> replace(List<String> path, XMLEventReader reader, XMLEvent event) throws XMLStreamException;
}
