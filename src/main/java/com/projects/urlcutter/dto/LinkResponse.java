package com.projects.urlcutter.dto;

public class LinkResponse {
  private String link;

  public LinkResponse(String link) {
    this.link = link;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }
}
