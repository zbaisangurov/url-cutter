package com.projects.urlcutter.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.projects.urlcutter.entity.Link;
import com.projects.urlcutter.repository.LinkRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GeneratingServiceTest {
  @Mock private LinkRepository linkRepository;
  @InjectMocks private GeneratingService generatingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGenerateNewLink() {
    String originalUrl = "https://example.com/test";
    when(linkRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.empty());
    when(linkRepository.save(any(Link.class)))
        .thenAnswer(
            invocation -> {
              Link savedLink = invocation.getArgument(0);
              savedLink.setId(1);
              return savedLink;
            });
    String shortLink = generatingService.getShortLink(originalUrl);
    assertNotNull(shortLink);
    assertTrue(shortLink.startsWith("/l/"));
    verify(linkRepository, times(1)).findByOriginalUrl(originalUrl);
    verify(linkRepository, times(2)).save(any(Link.class));
  }

  @Test
  void testGenerateExistingLink() {
    String originalUrl = "https://example.com/test";
    Link existingLink = new Link();
    existingLink.setOriginalUrl(originalUrl);
    existingLink.setShortUrl("abc12345");
    when(linkRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.of(existingLink));
    String shortLink = generatingService.getShortLink(originalUrl);
    assertEquals("/l/abc12345", shortLink);
    verify(linkRepository, never()).save(any());
  }

  @Test
  void testGenerateShortLink_InvalidUrl_ShouldThrowException() {
    String invalidUrl = "invalid-url";

    Exception exception =
        assertThrows(
            IllegalArgumentException.class, () -> generatingService.getShortLink(invalidUrl));

    assertEquals("Некорректный URL: invalid-url", exception.getMessage());
    verify(linkRepository, never()).save(any());
  }

  @Test
  void testGetShortLink_InvalidUrl_ShouldThrowException() {
    String invalidUrl = "invalid-url";
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> generatingService.getShortLink(invalidUrl));

    assertEquals("Некорректный URL: invalid-url", thrown.getMessage());
    verify(linkRepository, never()).findByOriginalUrl(anyString());
    verify(linkRepository, never()).save(any(Link.class));
  }
}
