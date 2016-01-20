/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.mojo.scala.crossbuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

}
