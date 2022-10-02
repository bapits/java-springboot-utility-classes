package com.bapits.labs.sample.utility.csv.model.target;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NameDataMap {

  @JsonProperty("PutRequest")
  protected PutRequest putRequest;

  public PutRequest getPutRequest() {
    return putRequest;
  }

  public void setPutRequest(PutRequest putRequest) {
    this.putRequest = putRequest;
  }



}
