package com.bapits.labs.sample.utility.csv.model.source;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "description", "name", "reference", "comId"})
public class NameMap {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("reference")
  private String reference;

  @JsonProperty("comId")
  private int comId;

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public int getComId() {
    return comId;
  }

  public void setComId(int comId) {
    this.comId = comId;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "NameMap [id=" + id + ", name=" + name + ", description=" + description + ", reference="
        + reference + ", comId=" + comId + "]";
  }

}

