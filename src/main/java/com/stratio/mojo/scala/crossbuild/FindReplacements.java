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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.ctc.wstx.api.WstxInputProperties;

class FindReplacements {

  private final List<RewriteRule> rewriteRules;

  public FindReplacements(final List<RewriteRule> rewriteRules) {
    this.rewriteRules = rewriteRules;
  }

  public List<Replacement> find(final File file) throws IOException, XMLStreamException {
    final List<Replacement> replacements = new ArrayList<>();
    final XMLInputFactory xmlif = XMLInputFactory.newFactory();
    xmlif.setProperty(WstxInputProperties.P_NORMALIZE_LFS, "false");
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
