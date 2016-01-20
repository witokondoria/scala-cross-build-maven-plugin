package com.stratio.mojo.scala.crossbuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

class PathTrackingXMLEventReader implements XMLEventReader {

  private final XMLEventReader reader;
  private final Stack<String> path;

  public PathTrackingXMLEventReader(final XMLEventReader reader) {
    this.reader = reader;
    this.path = new Stack<>();
  }

  public List<String> currentPath() {
    return new ArrayList<>(path);
  }

  @Override public XMLEvent nextEvent() throws XMLStreamException {
    final XMLEvent event = reader.nextEvent();
    if (event.isStartElement()) {
      path.push(event.asStartElement().getName().getLocalPart());
    } else if (event.isEndElement()) {
      path.pop();
    }
    return event;
  }

  @Override public boolean hasNext() {
    return reader.hasNext();
  }

  @Override public XMLEvent peek() throws XMLStreamException {
    return reader.peek();
  }

  @Override public String getElementText() throws XMLStreamException {
    final String text = reader.getElementText();
    path.pop();
    return text;
  }

  @Override public XMLEvent nextTag() throws XMLStreamException {
    final XMLEvent event = reader.nextTag();
    if (event.isStartElement()) {
      path.push(event.asStartElement().getName().getLocalPart());
    } else if (event.isEndElement()) {
      path.pop();
    }
    return event;
  }

  @Override public Object getProperty(String name) throws IllegalArgumentException {
    return reader.getProperty(name);
  }

  @Override public void close() throws XMLStreamException {
    reader.close();
  }

  @Override public Object next() {
    throw new UnsupportedOperationException();
  }

  @Override public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override public void forEachRemaining(Consumer action) {
    throw new UnsupportedOperationException();
  }
}
