package com.devonfw.example.component.common.api.to;

import java.util.Date;

import com.devonfw.module.basic.common.api.query.ComparableSearchConfigTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * {@link SearchCriteriaTo} to find instances of {@link com.devonfw.example.component.common.api.Foo}.
 */
public class FooSearchCriteriaTo extends SearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String message;

  private StringSearchConfigTo messageOption;

  private Integer count;

  private ComparableSearchConfigTo countOption;

  private Date createdAt;

  private ComparableSearchConfigTo createdAtOption;

  /**
   * @return the string to search for {@link com.devonfw.example.component.common.api.Foo#getMessage() message}.
   */
  public String getMessage() {

    return this.message;
  }

  /**
   * @param message new value of {@link #getMessage()}.
   */
  public void setMessage(String message) {

    this.message = message;
  }

  /**
   * @return the {@link StringSearchConfigTo} used to search for {@link #getMessage() message}.
   */
  public StringSearchConfigTo getMessageOption() {

    return this.messageOption;
  }

  /**
   * @param messageOption new value of {@link #getMessageOption()}.
   */
  public void setMessageOption(StringSearchConfigTo messageOption) {

    this.messageOption = messageOption;
  }

  /**
   * @return count
   */
  public Integer getCount() {

    return this.count;
  }

  /**
   * @param count new value of {@link #getCount}.
   */
  public void setCount(Integer count) {

    this.count = count;
  }

  /**
   * @return countOption
   */
  public ComparableSearchConfigTo getCountOption() {

    return this.countOption;
  }

  /**
   * @param countOption new value of {@link #getCountOption}.
   */
  public void setCountOption(ComparableSearchConfigTo countOption) {

    this.countOption = countOption;
  }

  /**
   * @return createdAt
   */
  public Date getCreatedAt() {

    return this.createdAt;
  }

  /**
   * @param createdAt new value of {@link #getCreatedAt}.
   */
  public void setCreatedAt(Date createdAt) {

    this.createdAt = createdAt;
  }

  /**
   * @return createdAtOption
   */
  public ComparableSearchConfigTo getCreatedAtOption() {

    return this.createdAtOption;
  }

  /**
   * @param createdAtOption new value of {@link #getCreatedAtOption}.
   */
  public void setCreatedAtOption(ComparableSearchConfigTo createdAtOption) {

    this.createdAtOption = createdAtOption;
  }

}
