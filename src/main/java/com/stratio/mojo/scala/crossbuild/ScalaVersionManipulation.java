package com.stratio.mojo.scala.crossbuild;

import java.util.regex.Pattern;

class ScalaVersionManipulation {

  private static Pattern IS_SCALA_BINARY_VERSION_R = Pattern.compile("2\\.[0-9]{1,2}");

  String changeScalaVersionInArtifactId(final String artifactId, final String newVersion) {
    if (newVersion == null) {
      throw new NullPointerException("newVersion");
    }
    if (artifactId == null) {
      return null;
    }
    final int lastIndexOfUnderscore = artifactId.lastIndexOf('_');
    if (lastIndexOfUnderscore == -1) {
      return artifactId;
    }
    final String potentialScalaBinaryVersion = artifactId.substring(lastIndexOfUnderscore).replace("_", "");
    if (!IS_SCALA_BINARY_VERSION_R.matcher(potentialScalaBinaryVersion).matches()) {
      return artifactId;
    }
    return artifactId.substring(0, lastIndexOfUnderscore) + "_" + newVersion;
  }

}
