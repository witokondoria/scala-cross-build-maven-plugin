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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ArtifactIdRewriteRule implements RewriteRule {

  private final String newVersion;
  private final Pattern artifactIdPattern = Pattern.compile(
      "(<artifactId>(?:[^<]|\\n){0,50}?_)(?:[0-9]+\\.[0-9]+)(</artifactId>)",
      Pattern.MULTILINE & Pattern.DOTALL
  );
  private final Pattern limitPattern = Pattern.compile("<(?:build|dependencies|reporting|profiles)>");

  public ArtifactIdRewriteRule(final String newVersion) {
    this.newVersion = newVersion;
  }

  @Override
  public String replace(final String input) {
    final Matcher limitMatcher = limitPattern.matcher(input);
    int limit = input.length();
    String prefix = input;
    String suffix = "";
    if (limitMatcher.find()) {
      limit = limitMatcher.toMatchResult().start();
      prefix = input.substring(0, limit);
      suffix = input.substring(limit);
    }
    final Matcher replaceMatcher = artifactIdPattern.matcher(prefix).region(0, limit);
    return replaceMatcher.replaceAll("$1" + newVersion + "$2") + suffix;
  }
}
