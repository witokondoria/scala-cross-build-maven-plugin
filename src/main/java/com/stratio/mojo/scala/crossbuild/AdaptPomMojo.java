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

import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

/**
 * Adapts pom.xml files to Scala conventions for cross version builds.
 *
 * TODO: Add link to relevant scaladoc.
 *
 * @goal adapt-pom
 * 
 * @phase process-sources
 */
public class AdaptPomMojo extends AbstractMojo {

  /**
   * The Maven Project.
   *
   * @parameter property="project"
   * @required
   * @readonly
   */
  private MavenProject project;

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

  public void execute() throws MojoExecutionException {
    final RewritePom rewritePom = new RewritePom();
    for (final MavenProject subproject: reactorProjects) {
      getLog().debug("Rewriting " + subproject.getFile());
      try {
        rewritePom.rewritePom(subproject, scalaBinaryVersion);
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
