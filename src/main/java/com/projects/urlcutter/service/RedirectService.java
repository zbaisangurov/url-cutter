package com.projects.urlcutter.service;

import com.projects.urlcutter.entity.Link;
import com.projects.urlcutter.repository.LinkRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {

  private final Logger logger = LoggerFactory.getLogger(RedirectService.class);
  private final LinkRepository linkRepository;

  public RedirectService(LinkRepository linkRepository) {
    this.linkRepository = linkRepository;
  }

  public String getOriginalUrl(String hash) {
    logger.info("Поиск оригинального URL...");
    Optional<Link> linkOptional = linkRepository.findByShortUrl(hash);
    if (linkOptional.isEmpty()) {
      throw new IllegalArgumentException("Такой короткой ссылки не найдено.");
    }
    Link link = linkOptional.get();
    link.setCount(link.getCount() + 1);
    linkRepository.save(link);
    return link.getOriginalUrl();
  }
}
