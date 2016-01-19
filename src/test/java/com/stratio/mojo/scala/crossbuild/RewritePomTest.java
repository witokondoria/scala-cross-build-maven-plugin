package com.stratio.mojo.scala.crossbuild;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.maven.project.MavenProject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.ctc.wstx.exc.WstxEOFException;

public class RewritePomTest {

  @Rule
  public TemporaryFolder tempDir = new TemporaryFolder();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static MavenProject getMockMavenProject(final File file) {
    final MavenProject project = mock(MavenProject.class);
    when(project.getFile()).thenReturn(file);
    return project;
  }

  @Test
  public void rewriteNonExistentFile() throws IOException, XMLStreamException {
    final RewritePom rewritePom = new RewritePom();
    final String path = "pom_does_not_exist.xml";
    final MavenProject project = getMockMavenProject(new File(path));
    final String newScalaVersion = "2.11";
    thrown.expect(NoSuchFileException.class);
    thrown.expectMessage(path);
    rewritePom.rewritePom(project, newScalaVersion);
  }

  @Test
  public void rewriteEmptyFile() throws IOException, XMLStreamException {
    final RewritePom rewritePom = new RewritePom();
    tempDir.create();
    final File file = tempDir.newFile();
    file.delete();
    assertThat(file.createNewFile()).isTrue();
    final MavenProject project = getMockMavenProject(file);
    final String newScalaVersion = "2.11";
    //TODO: thrown.expect(IOException.class); ????
    thrown.expect(WstxEOFException.class);
    rewritePom.rewritePom(project, newScalaVersion);
  }

  @Test
  public void rewriteBaseArtifactId() throws IOException, XMLStreamException {
    final RewritePom rewritePom = new RewritePom();
    tempDir.create();
    final File file = tempDir.newFile();
    file.delete();
    Files.copy(getClass().getResourceAsStream("/basic_pom.xml"), file.toPath());
    final MavenProject project = getMockMavenProject(file);
    final String newScalaVersion = "2.11";
    rewritePom.rewritePom(project, newScalaVersion);
    assertEqualToResource(file, "/basic_pom_result.xml");
    file.delete();
  }

  private void assertEqualToResource(final File actual, final String expected) throws IOException {
    final List<String> actualLines = Files.readAllLines(actual.toPath());
    final List<String> expectedLines = new ArrayList<>(actualLines.size());
    try (final BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(expected)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        expectedLines.add(line);
      }
    }
    assertThat(actualLines).isEqualTo(expectedLines);
  }

}
