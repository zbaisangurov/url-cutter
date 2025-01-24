package com.projects.urlcutter;

import io.sentry.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlCutterApplication {
  private static final Logger logger = LoggerFactory.getLogger(UrlCutterApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(UrlCutterApplication.class, args);
  }
}
