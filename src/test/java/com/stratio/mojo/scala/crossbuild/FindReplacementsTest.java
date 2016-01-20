package com.stratio.mojo.scala.crossbuild;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.fest.util.Collections;
import org.junit.Test;

public class FindReplacementsTest {
  @Test
  public void testFindReplacements() throws IOException, XMLStreamException {
    final List<RewriteRule> rewriteRules = Collections.<RewriteRule>list(
        new ArtifactIdRewriteRule("2.11")
    );
    final FindReplacements findOccurrences = new FindReplacements(rewriteRules);
    final File file = new File(getClass().getResource("/basic_pom.xml").getPath());
    final List<Replacement> actual =  findOccurrences.find(file);
    final List<Replacement> expected = Collections.list(new Replacement(288, 9, "test_2.11".getBytes(StandardCharsets.UTF_8)));
    assertThat(actual).containsExactly(expected.toArray());
  }
}
