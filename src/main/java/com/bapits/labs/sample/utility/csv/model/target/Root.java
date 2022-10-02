package com.bapits.labs.sample.utility.csv.model.target;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {

  @JsonProperty("name-data-map")
  protected List<NameDataMap> nameDataMap;

  public List<NameDataMap> getNameDataMap() {
    return nameDataMap;
  }

  public void setNameDataMap(List<NameDataMap> nameDataMap) {
    this.nameDataMap = nameDataMap;
  }

}
