package com.projects.urlcutter.service;

import com.projects.urlcutter.dto.LinkStatsResponse;
import com.projects.urlcutter.entity.Link;
import com.projects.urlcutter.repository.LinkRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StatService {
  private final Logger logger = LoggerFactory.getLogger(RedirectService.class);
  private final LinkRepository linkRepository;

  public StatService(LinkRepository linkRepository) {
    this.linkRepository = linkRepository;
  }

  /**
   * Получает статистику по конкретной ссылке.
   *
   * @param hash идентификатор короткой ссылки
   * @return объект статистики LinkStatsResponse
   * @throws IllegalArgumentException если ссылка не найдена
   */
  public LinkStatsResponse getLinkStats(String hash) {
    logger.info("Получение статистики по запросу");
    Optional<Link> linkOptional = linkRepository.findByShortUrl(hash);
    if (linkOptional.isEmpty()) {
      throw new IllegalArgumentException("Такая ссылка не найдена");
    }
    Link link = linkOptional.get();
    int rank = linkRepository.countByCountGreaterThan(link.getCount()) + 1;
    return new LinkStatsResponse(
        link.getOriginalUrl(), "/l/" + link.getShortUrl(), rank, link.getCount());
  }

  /**
   * Получает рейтинг популярных ссылок с постраничным выводом.
   *
   * @param pageable объект пагинации
   * @return список объектов LinkStatsResponse
   */
  public List<LinkStatsResponse> getAllLinksStats(Pageable pageable) {
    Page<Link> page = linkRepository.findAllByOrderByCountDesc(pageable);
    return page.getContent().stream()
        .map(
            link ->
                new LinkStatsResponse(
                    link.getOriginalUrl(),
                    "/l/" + link.getShortUrl(),
                    linkRepository.countByCountGreaterThan(link.getCount()) + 1,
                    link.getCount()))
        .collect(Collectors.toList());
  }
}
