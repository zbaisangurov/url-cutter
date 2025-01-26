package com.projects.urlcutter.controller;

import com.projects.urlcutter.service.RedirectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RedirectController {

  private static final Logger logger = LoggerFactory.getLogger(RedirectController.class);
  private final RedirectService redirectService;

  public RedirectController(RedirectService redirectService) {
    this.redirectService = redirectService;
  }

  @GetMapping("/l/{hash}")
  public ResponseEntity<Void> redirectLink(@PathVariable String hash) {
    logger.info("Получен запрос на редирект: /l/{}", hash);
    try {
      String originalUrl = redirectService.getOriginalUrl(hash);
      logger.info("Редирект на {}", originalUrl);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.LOCATION, originalUrl);
      return new ResponseEntity<>(headers, HttpStatus.FOUND);
    } catch (IllegalArgumentException e) {
      logger.error("Оригинальная ссылка не найдена");
      return ResponseEntity.notFound().build();
    }
  }
}
