package com.devonfw.module.basic.common.api.query;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * {@link AbstractTo TO} for the options to search for a comparable value.
 *
 */
public class ComparableSearchConfigTo extends SearchConfigTo {

  private ComparableSearchOperator operator;

  /**
   * @return operator the {@link ComparableSearchOperator} used to search. If {@code null} a "magic auto mode" is used
   *         where {@link ComparableSearchOperator#EQ} is used.
   */
  public ComparableSearchOperator getOperator() {

    return this.operator;
  }

  /**
   * @param operator new value of {@link #getOperator()}.
   */
  public void setOperator(ComparableSearchOperator operator) {

    this.operator = operator;
  }

  /**
   * @param operator the {@link ComparableSearchOperator}.
   * @return a new {@link ComparableSearchConfigTo} with the given config.
   */
  public static ComparableSearchConfigTo of(ComparableSearchOperator operator) {

    ComparableSearchConfigTo result = new ComparableSearchConfigTo();
    result.setOperator(operator);
    return result;
  }

}
