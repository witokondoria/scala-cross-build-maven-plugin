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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.maven.project.MavenProject;

class RewritePom {

  public void rewrite(final MavenProject pom, final String newBinaryVersion, final String newVersion) throws IOException, XMLStreamException {
    final List<RewriteRule> rewriteRules = Arrays.asList(
        new ArtifactIdRewriteRule(newBinaryVersion),
        new PropertyRewriteRule("scala.binary.version", newBinaryVersion),
        new PropertyRewriteRule("scala.version", newVersion)
    );
    final XMLRewriter rewriter = new XMLRewriter(rewriteRules);
    rewriter.rewrite(pom.getFile());
  }

  public void restorePom(final MavenProject pom) throws IOException {
    XMLRewriter.restoreFile(pom.getFile());
  }
}
