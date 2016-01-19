package com.stratio.mojo.scala.crossbuild;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.fest.assertions.Assertions.assertThat;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class FindOccurrencesTest {
  @Test
  public void testFindOccurrences() throws IOException, XMLStreamException {
    final FindOccurrences findOccurrences = new FindOccurrences();
    final List<Occurrence> actual =  findOccurrences.find(new File(getClass().getResource("/basic_pom.xml").getPath()), "2.11");
    final List<Occurrence> expected = Arrays.asList(new Occurrence(288, 9, "test_2.11".getBytes(StandardCharsets.UTF_8)));
    assertThat(actual).containsExactly(expected.toArray());
  }
}
