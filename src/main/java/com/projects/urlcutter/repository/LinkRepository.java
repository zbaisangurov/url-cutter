package com.projects.urlcutter.repository;

import com.projects.urlcutter.entity.Link;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, String> {
  Optional<Link> findByOriginalUrl(String originalUrl);

  Optional<Link> findByShortUrl(String shortUrl);

  int countByCountGreaterThan(int count);

  Page<Link> findAllByOrderByCountDesc(Pageable pageable);

  boolean existsByShortUrl(String shortUrl);
}
