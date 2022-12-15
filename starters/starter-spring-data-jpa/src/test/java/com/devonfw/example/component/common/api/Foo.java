package com.devonfw.example.component.common.api;

import com.devonfw.example.general.common.api.TestApplicationEntity;

public interface Foo extends TestApplicationEntity {

  String getMessage();

  void setMessage(String message);

  String getName();

  void setName(String name);

  void setCreatedAt(java.util.Date createdAt);

  java.util.Date getCreatedAt();

  void setCount(Integer count);

  Integer getCount();

}
