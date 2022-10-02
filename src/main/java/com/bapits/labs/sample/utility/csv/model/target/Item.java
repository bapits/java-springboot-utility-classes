package com.bapits.labs.sample.utility.csv.model.target;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

  @JsonProperty("id")
  private Id id;

  @JsonProperty("name")
  private Name entity;

  @JsonProperty("description")
  private Description description;

  @JsonProperty("reference")
  private Reference reference;

  @JsonProperty("comId")
  private ComId comId;

  public Id getId() {
    return id;
  }

  public void setId(Id id) {
    this.id = id;
  }

  public Name getEntity() {
    return entity;
  }

  public void setEntity(Name entity) {
    this.entity = entity;
  }

  public Description getDescription() {
    return description;
  }

  public void setDescription(Description description) {
    this.description = description;
  }

  public Reference getReference() {
    return reference;
  }

  public void setReference(Reference reference) {
    this.reference = reference;
  }

  public ComId getComId() {
    return comId;
  }

  public void setComId(ComId comId) {
    this.comId = comId;
  }

}
