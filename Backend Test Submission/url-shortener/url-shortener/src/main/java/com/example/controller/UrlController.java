package com.example.controller;

import com.example.model.*;
import com.example.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shorturls")
public class UrlController {

    @Autowired
    private UrlService service;

    @PostMapping
    public ResponseEntity<UrlResponse> create(@RequestBody UrlRequest req) {
        return new ResponseEntity<>(service.createShortUrl(req), HttpStatus.CREATED);
    }

    @GetMapping("/{shortcode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortcode) {
        UrlEntity entity = service.redirect(shortcode);
        return ResponseEntity.status(HttpStatus.FOUND).location(java.net.URI.create(entity.getOriginalUrl())).build();
    }

    @GetMapping("/{shortcode}/stats")
    public ResponseEntity<UrlEntity> stats(@PathVariable String shortcode) {
        return ResponseEntity.ok(service.getStats(shortcode));
    }
}