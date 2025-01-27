package com.projects.urlcutter.service;

import com.projects.urlcutter.dto.LinkStatsResponse;
import com.projects.urlcutter.entity.Link;
import com.projects.urlcutter.repository.LinkRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StatService {
  private final Logger logger = LoggerFactory.getLogger(RedirectService.class);
  private final LinkRepository linkRepository;

  public StatService(LinkRepository linkRepository) {
    this.linkRepository = linkRepository;
  }

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
}
