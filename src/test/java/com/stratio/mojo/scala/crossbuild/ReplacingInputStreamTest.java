package com.stratio.mojo.scala.crossbuild;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.StringInputStream;
import org.junit.Test;

import static java.util.Arrays.asList;

public class ReplacingInputStreamTest {

  @Test
  public void testReplacingInputStream() throws IOException {
    internalTest("abcXXabc", asList(new Replacement(3, 2, "YY".getBytes())), "abcYYabc");
    internalTest("XXabc", asList(new Replacement(0, 2, "YY".getBytes())), "YYabc");
    internalTest("abcXX", asList(new Replacement(3, 2, "YY".getBytes())), "abcYY");
  }

  private void internalTest(final String input, final List<Replacement> occurrences, final String expectedOutput) throws
      IOException {
    final InputStream underlying = new StringInputStream(input);
    final InputStream in = new ReplacingInputStream(underlying, occurrences);
    final String actualOutput = IOUtils.toString(in);
    assertThat(actualOutput).isEqualToIgnoringCase(expectedOutput);
  }

}
