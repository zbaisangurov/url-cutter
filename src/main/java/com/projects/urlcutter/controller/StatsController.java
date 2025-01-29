package com.projects.urlcutter.controller;

import com.projects.urlcutter.dto.LinkStatsResponse;
import com.projects.urlcutter.service.StatService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {
  private static final Logger logger = LoggerFactory.getLogger(StatsController.class);
  private final StatService statService;

  public StatsController(StatService statService) {
    this.statService = statService;
  }

  /**
   * Получает статистику по конкретной короткой ссылке.
   *
   * @param hash уникальный идентификатор короткой ссылки
   * @return ResponseEntity с объектом статистики
   * @throws IllegalArgumentException, если такой уникальный идентификатор отсутствует в базе
   */
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

  /**
   * Получает рейтинг популярных ссылок с постраничным выводом.
   *
   * @param page номер страницы (по умолчанию 1)
   * @param linksPerPage количество записей на странице (по умолчанию 3, не более 100)
   * @return ResponseEntity со списком популярных ссылок
   */
  @GetMapping
  public ResponseEntity<List<LinkStatsResponse>> getLinkStats(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "3") int linksPerPage) {
    logger.info("Получен запрос рейтинга ссылок. Количество: {}  Страница:{}", linksPerPage, page);
    linksPerPage = Math.min(linksPerPage, 100);
    Pageable pageable = PageRequest.of(page - 1, linksPerPage);
    List<LinkStatsResponse> stats = statService.getAllLinksStats(pageable);
    return ResponseEntity.ok(stats);
  }
}
