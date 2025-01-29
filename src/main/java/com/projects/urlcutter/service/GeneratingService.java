package com.projects.urlcutter.service;

import com.projects.urlcutter.entity.Link;
import com.projects.urlcutter.repository.LinkRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GeneratingService {
  private final Logger logger = LoggerFactory.getLogger(GeneratingService.class);
  private final LinkRepository linkRepository;

  public GeneratingService(LinkRepository linkRepository) {
    this.linkRepository = linkRepository;
  }

  private static final String URL_REGEX =
      "^(https?://)?"
          + "((([a-zA-Z\\d]([a-zA-Z\\d-]*[a-zA-Z\\d])*)\\.)+[a-zA-Z]{2,}|"
          + "localhost|"
          + "\\d{1,3}(\\.\\d{1,3}){3})"
          + "(:\\d{1,5})?"
          + "(/.*)?$";
  private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

  /**
   * Генерирует короткую ссылку на основе оригинального URL.
   * Если ссылка уже существует в БД, возвращает её.
   *
   * @param originalUrl оригинальный URL
   * @return короткая ссылка
   */
  @Transactional
  public String getShortLink(String originalUrl) {
    if (!isValidUrl(originalUrl)) {
      throw new IllegalArgumentException("Некорректный URL: " + originalUrl);
    }
    Optional<Link> existingLink = linkRepository.findByOriginalUrl(originalUrl);
    if (existingLink.isPresent()) {
      logger.info("Достаем ссылку из БД");
      return "/l/" + existingLink.get().getShortUrl();
    } else {
      logger.info("Ссылка отсутствует в БД");
      Link link = new Link();
      link.setOriginalUrl(originalUrl);
      link.setCount(0);
      linkRepository.save(link);
      String shortUrl = encodeBase62(link.getId());
      link.setShortUrl(shortUrl);
      linkRepository.save(link);
      return "/l/" + shortUrl;
    }
  }

  private static final String BASE62_ALPHABET =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  /**
   * Производит генерацию короткой ссылки на основе алгоритма хэширования Base62
   *
   * @param id уникальный идентификатор записи в базе
   * @return строка с короткой ссылкой
   */
  private String encodeBase62(int id) {
    StringBuilder encoded = new StringBuilder();
    while (id > 0) {
      encoded.insert(0, BASE62_ALPHABET.charAt(id % 62));
      id /= 62;
    }
    return encoded.toString();
  }

  /**
   * Проверяет корректность URL.
   *
   * @param url строка URL
   * @return true, если URL валиден, иначе false
   */
  private boolean isValidUrl(String url) {
    return URL_PATTERN.matcher(url).matches();
  }
}
