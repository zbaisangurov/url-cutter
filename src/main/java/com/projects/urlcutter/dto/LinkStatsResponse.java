package com.projects.urlcutter.dto;

public class LinkStatsResponse {
  private String originalUrl;
  private String shortUrl;
  private int rank;
  private int count;

  public LinkStatsResponse(String originalUrl, String shortUrl, int rank, int count) {
    this.originalUrl = originalUrl;
    this.shortUrl = shortUrl;
    this.rank = rank;
    this.count = count;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public int getRank() {
    return rank;
  }

  public int getCount() {
    return count;
  }
}
