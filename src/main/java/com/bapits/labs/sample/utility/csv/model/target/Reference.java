package com.bapits.labs.sample.utility.csv.model.target;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("reference")
public class Reference {

  @JsonProperty("S")
  protected String s;

  public String getS() {
    return s;
  }

  public void setS(String s) {
    this.s = s;
  }



}
