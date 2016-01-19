package com.stratio.mojo.scala.crossbuild;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class ScalaVersionManipulationTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void changeScalaBinaryVersionInArtifactId() {
    final ScalaVersionManipulation svm = new ScalaVersionManipulation();
    assertThat(svm.changeScalaVersionInArtifactId("hello", "2.10")).isEqualTo("hello");
    assertThat(svm.changeScalaVersionInArtifactId("hello_2.10", "2.10")).isEqualTo("hello_2.10");
    assertThat(svm.changeScalaVersionInArtifactId("hello_2.10", "2.10")).isEqualTo("hello_2.10");
    assertThat(svm.changeScalaVersionInArtifactId("hello_2.10", "2.11")).isEqualTo("hello_2.11");
    assertThat(svm.changeScalaVersionInArtifactId("hello_2.11", "2.10")).isEqualTo("hello_2.10");
    assertThat(svm.changeScalaVersionInArtifactId("hello_2222", "2.10")).isEqualTo("hello_2222");
    assertThat(svm.changeScalaVersionInArtifactId("hello_2.10.2", "2.11")).isEqualTo("hello_2.10.2");
  }

  @Test
  public void changeScalaBinaryVersionInArtifactIdWithNullArtifact() {
    final ScalaVersionManipulation svm = new ScalaVersionManipulation();
    assertThat(svm.changeScalaVersionInArtifactId(null, "2.10")).isNull();
  }

  @Test
  public void changeScalaBinaryVersionInArtifactIdWithNullVersion() {
    final ScalaVersionManipulation svm = new ScalaVersionManipulation();
    thrown.expect(NullPointerException.class);
    assertThat(svm.changeScalaVersionInArtifactId("hello", null)).isNull();
  }

}
