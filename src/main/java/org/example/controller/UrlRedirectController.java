package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.UrlMapping;
import org.example.repository.UrlMappingRepository;
import org.example.service.UrlMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class UrlRedirectController {
    private UrlMappingService urlMappingService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        String originalUrls = urlMappingService.getOriginalUrl(shortUrl);
        if (originalUrls != null) {
            return ResponseEntity.status(301)
                    .location(URI.create(originalUrls))
                    .build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}