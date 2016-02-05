package com.stratio.mojo.scala.crossbuild;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegexRewriteRule implements RewriteRule {

  private final Pattern searchPattern;
  private final String replacement;
  private final Pattern limitPattern;

  public RegexRewriteRule(final Pattern searchPattern, String replacement, final Pattern limitPattern) {
    this.searchPattern = searchPattern;
    this.replacement = replacement;
    this.limitPattern = limitPattern;
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
    final Matcher replaceMatcher = searchPattern.matcher(prefix).region(0, limit);
    return replaceMatcher.replaceAll(replacement) + suffix;
  }

}
