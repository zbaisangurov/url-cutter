package com.projects.urlcutter.service;

import com.projects.urlcutter.dto.LinkStatsResponse;
import com.projects.urlcutter.entity.Link;
import com.projects.urlcutter.repository.LinkRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatServiceTest {
  @Mock private LinkRepository linkRepository;

  @InjectMocks private StatService statService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetLinkStats_ExistingShortUrl() {
    String shortUrl = "x";
    Link link = new Link();
    link.setShortUrl(shortUrl);
    link.setOriginalUrl("https://example.com");
    link.setCount(50);

    when(linkRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(link));
    when(linkRepository.countByCountGreaterThan(50)).thenReturn(2);
    LinkStatsResponse response = statService.getLinkStats(shortUrl);

    assertEquals("/l/x", response.getShortUrl());
    assertEquals("https://example.com", response.getOriginalUrl());
    assertEquals(3, response.getRank());
    assertEquals(50, response.getCount());

    verify(linkRepository, times(1)).findByShortUrl(shortUrl);
  }

  @Test
  void testGetLinkStats_NonExistingShortUrl() {
    String shortUrl = "y";
    when(linkRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> statService.getLinkStats(shortUrl));

    assertEquals("Такая ссылка не найдена", thrown.getMessage());
    verify(linkRepository, times(1)).findByShortUrl(shortUrl);
  }

  @Test
  void testGetAllLinksStats() {
    Pageable pageable = PageRequest.of(0, 2);
    Link link1 = new Link();
    link1.setShortUrl("a");
    link1.setOriginalUrl("https://example1.com");
    link1.setCount(50);
    Link link2 = new Link();
    link2.setShortUrl("b");
    link2.setOriginalUrl("https://example2.com");
    link2.setCount(30);
    List<Link> links = Arrays.asList(link1, link2);
    Page<Link> page = new PageImpl<>(links, pageable, links.size());
    when(linkRepository.findAllByOrderByCountDesc(pageable)).thenReturn(page);
    when(linkRepository.countByCountGreaterThan(50)).thenReturn(0);
    when(linkRepository.countByCountGreaterThan(30)).thenReturn(1);
    List<LinkStatsResponse> response = statService.getAllLinksStats(pageable);
    assertEquals(2, response.size());
    assertEquals("https://example1.com", response.get(0).getOriginalUrl());
    assertEquals("/l/a", response.get(0).getShortUrl());
    verify(linkRepository, times(1)).findAllByOrderByCountDesc(pageable);
  }
}
