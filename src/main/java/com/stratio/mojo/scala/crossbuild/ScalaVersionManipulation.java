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
