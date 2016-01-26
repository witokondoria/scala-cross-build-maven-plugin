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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

class RewriteJUnitXML {

  private static Pattern SCALA_SUFFIX = Pattern.compile("\\[Scala [0-9]+\\.[0-9]+\\]");

  public void rewrite(final File file, final String scalaBinaryVersion) throws IOException, JDOMException {
    final SAXBuilder jdomBuilder = new SAXBuilder();
    final Document doc = jdomBuilder.build(file);
    for (final Element element: doc.getRootElement().getChildren()) {
      if (!"testcase".equals(element.getName())) {
        continue;
      }
      final org.jdom2.Attribute nameAttribute = element.getAttribute("name");
      if (nameAttribute == null) {
        continue;
      }
      final String oldValue = nameAttribute.getValue();
      if (SCALA_SUFFIX.matcher(oldValue).matches()) {
        continue;
      }
      final String newValue = oldValue + " [Scala " + scalaBinaryVersion + "]";
      if (newValue.equals(oldValue)) {
        continue;
      }
      nameAttribute.setValue(newValue);
    }
    final Format format = Format.getRawFormat();
    final XMLOutputter xmlOut = new XMLOutputter();
    xmlOut.output(doc, new FileOutputStream(file));
  }

}
