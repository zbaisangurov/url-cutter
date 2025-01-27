package com.projects.urlcutter.controller;

import com.projects.urlcutter.dto.LinkStatsResponse;
import com.projects.urlcutter.service.StatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {
  private static final Logger logger = LoggerFactory.getLogger(StatsController.class);
  private final StatService statService;

  public StatsController(StatService statService) {
    this.statService = statService;
  }

  @GetMapping("/{hash}")
  public ResponseEntity<LinkStatsResponse> getLinkStats(@PathVariable String hash) {
    logger.info("Получен запрос статистики по короткой ссылке {}", hash);
    try {
      LinkStatsResponse response = statService.getLinkStats(hash);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      logger.error("Ссылка с таким хэшем не найдена");
      return ResponseEntity.notFound().build();
    }
  }
}
