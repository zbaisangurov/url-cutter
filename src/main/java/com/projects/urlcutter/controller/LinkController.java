package com.projects.urlcutter.controller;

import com.projects.urlcutter.dto.LinkRequest;
import com.projects.urlcutter.dto.LinkResponse;
import com.projects.urlcutter.service.GeneratingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LinkController {

  private static final Logger logger = LoggerFactory.getLogger(LinkController.class);
  private final GeneratingService generatingService;

  public LinkController(GeneratingService generatingService) {
    this.generatingService = generatingService;
  }

  @PostMapping("/generate")
  public ResponseEntity<LinkResponse> generateLink(@RequestBody LinkRequest linkRequest) {
    logger.info("Получен запрос, идёт обработка");
    try {
      String shortLink = generatingService.getShortLink(linkRequest.getOriginal());
      return ResponseEntity.ok(new LinkResponse(shortLink));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new LinkResponse("Некорректный URL"));
    }
  }
}
