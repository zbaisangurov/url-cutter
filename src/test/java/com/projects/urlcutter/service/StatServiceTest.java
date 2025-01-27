package com.projects.urlcutter.service;

import com.projects.urlcutter.dto.LinkStatsResponse;
import com.projects.urlcutter.entity.Link;
import com.projects.urlcutter.repository.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

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
}
