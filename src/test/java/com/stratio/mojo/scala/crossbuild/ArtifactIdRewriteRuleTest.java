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

import org.junit.Test;

public class ArtifactIdRewriteRuleTest {

  @Test
  public void noOccurrence() {
    final RewriteRule rule = new ArtifactIdRewriteRule("2.11");
    assertThat(rule.replace("<project><artifactId>foo</artifactId></project>"))
        .isEqualTo("<project><artifactId>foo</artifactId></project>");
    assertThat(rule.replace("<project><parent><artifactId>foo</artifactId></parent></project>"))
        .isEqualTo("<project><parent><artifactId>foo</artifactId></parent></project>");
  }

  @Test
  public void sameVersion() {
    final RewriteRule rule = new ArtifactIdRewriteRule("2.10");
    assertThat(rule.replace("<project><artifactId>foo_2.10</artifactId></project>"))
        .isEqualTo("<project><artifactId>foo_2.10</artifactId></project>");
    assertThat(rule.replace("<project><parent><artifactId>foo_2.10</artifactId></parent></project>"))
        .isEqualTo("<project><parent><artifactId>foo_2.10</artifactId></parent></project>");
  }

  @Test
  public void simpleOccurrence() {
    final RewriteRule rule = new ArtifactIdRewriteRule("2.11");
    assertThat(rule.replace("<project><artifactId>foo_2.10</artifactId></project>"))
        .isEqualTo("<project><artifactId>foo_2.11</artifactId></project>");
    assertThat(rule.replace("<project><parent><artifactId>foo_2.10</artifactId></parent></project>"))
        .isEqualTo("<project><parent><artifactId>foo_2.11</artifactId></parent></project>");
    assertThat(rule.replace("\n <project>\n <parent>\n <artifactId>foo_2.10</artifactId>\n </parent>\n </project>"))
        .isEqualTo("\n <project>\n <parent>\n <artifactId>foo_2.11</artifactId>\n </parent>\n </project>");
    assertThat(rule.replace("<!-- Comment --> <project><parent><artifactId>foo_2.10</artifactId></parent></project>"))
        .isEqualTo("<!-- Comment --> <project><parent><artifactId>foo_2.11</artifactId></parent></project>");
  }

  @Test
  public void bothParentAndSelf() {
    final RewriteRule rule = new ArtifactIdRewriteRule("2.11");
    assertThat(rule.replace("<project><parent><artifactId>parent_2.10</artifactId></parent><artifactId>foo_2.10</artifactId></project>"))
        .isEqualTo("<project><parent><artifactId>parent_2.11</artifactId></parent><artifactId>foo_2.11</artifactId></project>");
  }

  @Test
  public void doNotReplaceInProfiles() {
    final RewriteRule rule = new ArtifactIdRewriteRule("2.11");
    assertThat(rule.replace("<project><properties><foo>bar</foo></properties><profiles><properties><test.prop>foobar</test.prop></properties>"))
        .isEqualTo("<project><properties><foo>bar</foo></properties><profiles><properties><test.prop>foobar</test.prop></properties>");
  }

  @Test
  public void doNotReplaceInDependencies() {
    final RewriteRule rule = new ArtifactIdRewriteRule("2.11");
    assertThat(rule.replace("<project><artifactId>foo_2.10</artifactId><dependencies><artifactId>foo_2.10</artifactId></dependencies>"))
        .isEqualTo("<project><artifactId>foo_2.11</artifactId><dependencies><artifactId>foo_2.10</artifactId></dependencies>");
  }

}
