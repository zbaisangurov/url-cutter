package com.projects.urlcutter.service;

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

public class RedirectServiceTest {
  @Mock
  private LinkRepository linkRepository;

  @InjectMocks
  private RedirectService redirectService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetOriginalUrl_ExistingShortUrl() {
    String shortUrl = "f";
    Link link = new Link();
    link.setShortUrl(shortUrl);
    link.setOriginalUrl("https://hello-balya.com");
    link.setCount(10);

    when(linkRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(link));
    String originalUrl = redirectService.getOriginalUrl(shortUrl);
    assertEquals("https://hello-balya.com", originalUrl);
    assertEquals(11, link.getCount());
    verify(linkRepository, times(1)).save(link);
  }

  @Test
  void testGetOriginalUrl_NonExistingShortUrl() {
    String shortUrl = "x";
    when(linkRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
        redirectService.getOriginalUrl(shortUrl));
    assertEquals("Такой короткой ссылки не найдено.", thrown.getMessage());
    verify(linkRepository, never()).save(any(Link.class));
  }
}
