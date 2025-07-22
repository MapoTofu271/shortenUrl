package org.example.controller;


import lombok.AllArgsConstructor;
import org.example.dtos.ClickEventDTO;
import org.example.dtos.UrlMappingDTO;
import org.example.model.User;
import org.example.service.UrlMappingService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
    private UrlMappingService urlMappingService;
    private UserService userService;
    //RequestBody: request: "originalUrl":"https://example.com"

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request,
                                                        Principal principal) {
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingDTO> userUrls = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(userUrls);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlsAnalytics(@PathVariable String shortUrl,
                                                                @RequestParam("startDate") String startDate,
                                                                @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<ClickEventDTO> analytics = urlMappingService.getClickEventsByDate(shortUrl, start, end);
        return ResponseEntity.ok(analytics);
    }


    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    //result: "date": count
    public ResponseEntity<Map<LocalDate, Long>>getTotalClicks(Principal principal,
                                                                @RequestParam("startDate") String startDate,
                                                                @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        User user = userService.findByUsername(principal.getName());
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClickEventsByDate(user, start, end);
        return ResponseEntity.ok(totalClicks);
    }






}
