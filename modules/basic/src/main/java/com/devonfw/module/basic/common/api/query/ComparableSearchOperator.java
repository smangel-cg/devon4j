package com.devonfw.module.basic.common.api.query;

/**
 * Enum of operators for any comparable type
 */
public enum ComparableSearchOperator implements SearchOperator {
  /** Matches if objects are {@link Comparable#equals(Object) equal}. */
  EQ("=="),

  /** Matches if strings are NOT {@link Comparable#equals(Object) equal}. */
  NE("!="),

  /** Matches if search value is less than search hit(s) using {@link Comparable#compareTo(Object) compareTo}. */
  LT("<"),

  /**
   * Matches if search value is less or equal to search hit(s) in using {@link Comparable#compareTo(Object) compareTo}.
   */
  LE("<="),

  /**
   * Matches if search value is greater than search hit(s) using {@link Comparable#compareTo(Object) compareTo}.
   */
  GT(">"),

  /**
   * Matches if search value is greater or equal to search hit(s) using {@link Comparable#compareTo(Object) compareTo}.
   */
  GE(">=");

  private final String operator;

  private ComparableSearchOperator(String operator) {

    this.operator = operator;
  }

  @Override
  public String toString() {

    return this.operator;
  }

}
