package com.devonfw.example.component.dataaccess.api;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.example.component.common.api.Foo;
import com.devonfw.example.general.dataaccess.api.TestApplicationPersistenceEntity;

/**
 * Implementation of {@link Foo} as {@link TestApplicationPersistenceEntity persistence entity}.
 */
@Entity
@Table(name = "Bar")
public class FooEntity extends TestApplicationPersistenceEntity implements Foo {
  private static final long serialVersionUID = 1L;

  private String message;

  private String name;

  private Integer count;

  private java.util.Date createdAt;

  @Override
  public String getMessage() {

    return this.message;
  }

  @Override
  public void setMessage(String message) {

    this.message = message;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    this.name = name;
  }

  @Override
  public Integer getCount() {

    return this.count;
  }

  @Override
  public void setCount(Integer count) {

    this.count = count;
  }

  @Override
  public java.util.Date getCreatedAt() {

    return this.createdAt;
  }

  @Override
  public void setCreatedAt(java.util.Date createdAt) {

    this.createdAt = createdAt;
  }

}
