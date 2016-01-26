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
    internalTest("abcXX", asList(new Replacement(3, 2, "YYUU".getBytes())), "abcYYUU");
  }

  private void internalTest(final String input, final List<Replacement> occurrences, final String expectedOutput) throws
      IOException {
    final InputStream underlying = new StringInputStream(input);
    final InputStream in = new ReplacingInputStream(underlying, occurrences);
    final String actualOutput = IOUtils.toString(in);
    assertThat(actualOutput).isEqualToIgnoringCase(expectedOutput);
  }

}
