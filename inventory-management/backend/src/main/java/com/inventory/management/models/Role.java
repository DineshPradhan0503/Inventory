package com.inventory.management.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "roles")
@Data
public class Role {
  @Id
  private String id;

  private ERole name;

  public Role() {

  }

  public Role(ERole name) {
    this.name = name;
  }
}
