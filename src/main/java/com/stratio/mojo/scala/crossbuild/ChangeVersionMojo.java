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

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Adapts pom.xml files to Scala conventions for cross version builds.
 *
 * TODO: Add link to relevant scaladoc.
 *
 * @goal change-version
 */
public class ChangeVersionMojo extends AbstractMojo {

  /**
   * The projects in the reactor.
   *
   * @parameter expression="${reactorProjects}"
   * @required
   * @readonly
   */
  private List<MavenProject> reactorProjects;

  /**
   * The Scala binary version to switch to.
   *
   * @parameter expression="${scala.binary.version}"
   * @required
   * @readonly
   */
  private String scalaBinaryVersion;

  /**
   * The Scala library/compiler version to switch to.
   *
   * @parameter expression="${scala.version}"
   * @required
   * @readonly
   */
  private String scalaVersion;

  public void execute() throws MojoExecutionException {
    final RewritePom rewritePom = new RewritePom();
    for (final MavenProject subproject: reactorProjects) {
      getLog().debug("Rewriting " + subproject.getFile());
      try {
        rewritePom.rewritePom(subproject, scalaBinaryVersion, scalaVersion);
      } catch (final IOException | XMLStreamException ex) {
        restoreProjects(reactorProjects);
        throw new MojoExecutionException("Failed to rewrite POM", ex);
      }
    }
  }

  private static void restoreProjects(final List<MavenProject> projects) throws MojoExecutionException {
    final RewritePom rewritePom = new RewritePom();
    for (final MavenProject project: projects) {
      try {
        rewritePom.restorePom(project);
      } catch (final IOException ex) {
        throw new MojoExecutionException("Failed to restore POM", ex);
      }
    }
  }

}
