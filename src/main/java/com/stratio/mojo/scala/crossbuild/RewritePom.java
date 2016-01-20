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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.maven.project.MavenProject;

class RewritePom {

  private static final String BKP_SUFFIX = ".bkp";

  public void rewritePom(final MavenProject pom, final String newBinaryVersion, final String newVersion) throws IOException, XMLStreamException {
    final File pomFile = pom.getFile();
    backupFile(pomFile);
    final File bkpFile = getBackupFileName(pomFile);
    rewritePom(bkpFile, pomFile, newBinaryVersion, newVersion);
  }


  public void restorePom(final MavenProject pom) throws IOException {
    restoreFile(pom.getFile());
  }

  private static void backupFile(final File origFile) throws IOException {
    final File bkpFile = getBackupFileName(origFile);
    Files.copy(origFile.toPath(), bkpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  private static void restoreFile(final File origFile) throws IOException {
    final File bkpFile = getBackupFileName(origFile);
    Files.copy(bkpFile.toPath(), origFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  private static File getBackupFileName(final File origFile) throws IOException {
    return new File(origFile.getAbsolutePath() + BKP_SUFFIX);
  }

  private static void rewritePom(final File origFile, final File newFile, final String newBinaryVersion, final String newVersion) throws IOException,
      XMLStreamException {
    final List<RewriteRule> rewriteRules = Arrays.asList(
        new ArtifactIdRewriteRule(newBinaryVersion),
        new PropertyRewriteRule("scala.binary.version", newBinaryVersion),
        new PropertyRewriteRule("scala.version", newVersion)
    );
    final FindReplacements findReplacements = new FindReplacements(rewriteRules);
    final List<Replacement> occurrences = findReplacements.find(origFile);
    if (!occurrences.isEmpty()) {
      try (final BufferedInputStream in = new BufferedInputStream(
          new ReplacingInputStream(new FileInputStream(origFile), occurrences)
      )) {
        try (final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile, false))) {
          int b;
          while ((b = in.read()) != -1) {
            out.write(b);
          }
        }
      }
    }
  }

}
