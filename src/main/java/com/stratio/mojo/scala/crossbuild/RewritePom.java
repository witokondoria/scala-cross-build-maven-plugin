package com.stratio.mojo.scala.crossbuild;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.maven.project.MavenProject;

class RewritePom {

  private static final String BKP_SUFFIX = ".bkp";

  public void rewritePom(final MavenProject pom, final String newVersion) throws IOException, XMLStreamException {
    final File pomFile = pom.getFile();
    backupFile(pomFile);
    final File bkpFile = getBackupFileName(pomFile);
    rewritePom(bkpFile, pomFile, newVersion);
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

  private static void rewritePom(final File origFile, final File newFile, final String newVersion) throws IOException,
      XMLStreamException {
    final FindOccurrences findOccurrences = new FindOccurrences();
    final List<Occurrence> occurrences = findOccurrences.find(origFile, newVersion);
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
