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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * Utilities to verify IT execution.
 */
public final class ITs {

  private ITs() { }

  public static boolean verify(
      final List<String> scalaBinaryVersions,
      final List<String> artifactNames,
      final List<String> targetPaths,
      final List<String> testCases
  ) throws IOException, FileNotFoundException {
    if (artifactNames.size() != targetPaths.size()) {
      throw new IllegalArgumentException("artifactNames and modulePaths must be equal size");
    }
    if (scalaBinaryVersions.isEmpty()) {
      throw new IllegalArgumentException("At least one scalaBinaryVersion must be provided");
    }
    for (final String scalaBinaryVersion: scalaBinaryVersions) {
      for (int i = 0; i < artifactNames.size(); i++) {
        final String artifactName = artifactNames.get(i);
        final String artifactId = artifactName + "_" + scalaBinaryVersion;
        final File targetPath = new File(targetPaths.get(i));
        final File targetScalaPath = new File(targetPath, scalaBinaryVersion);
        final File artifactPath = new File(targetScalaPath, artifactId + "-1.jar");
        if (!artifactPath.isFile()) {
          throw new FileNotFoundException("Could not find generated artifact: " + artifactPath);
        }
        if (testCases.isEmpty()) {
          continue;
        }
        final File surefireReportsPath = new File(targetScalaPath, "surefire-reports");
        if (!surefireReportsPath.isDirectory()) {
          throw new FileNotFoundException("Could not find surefire-reports directory: " + surefireReportsPath);
        }
        final File[] testReports = surefireReportsPath.listFiles(new FileFilter() {
          @Override
          public boolean accept(final File pathname) {
            return pathname.getName().endsWith(".xml") && pathname.getName().startsWith("TEST-");
          }
        });
        if (testReports.length == 0) {
          throw new FileNotFoundException("Could not find test reports");
        }
        for (final String testCase: testCases) {
          final String scalaString = "[Scala " + scalaBinaryVersion + "]";
          boolean found = false;
          for (final File testReport : testReports) {
            final String testReportString = IOUtils.toString(new FileInputStream(testReport));
            final String testCaseWithSuffix = testCase + " " + scalaString;
            if (testReportString.contains(testCaseWithSuffix)) {
              found = true;
              break;
            }
          }
          if (!found) {
            throw new RuntimeException("JUnit XML test report does not contain Scala version string (" + scalaString + ")");
          }
        }
      }
    }
    return true;
  }
}
