package com.bapits.labs.sample.utility.csv.model.target;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("comId")
public class ComId {

  @JsonProperty("N")
  protected String n;

  public String getN() {
    return n;
  }

  public void setN(String n) {
    this.n = n;
  }



}
