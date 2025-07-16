package com.example.service;

import com.example.model.*;
import com.example.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UrlService {
    @Autowired
    private UrlRepository repo;

    public UrlResponse createShortUrl(UrlRequest request) {
        String shortcode = (request.getShortcode() != null && !request.getShortcode().isEmpty())
                ? request.getShortcode()
                : UUID.randomUUID().toString().substring(0, 6);

        if (repo.existsById(shortcode)) throw new RuntimeException("Shortcode already exists");

        UrlEntity entity = new UrlEntity();
        entity.setShortcode(shortcode);
        entity.setOriginalUrl(request.getUrl());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setExpiryAt(LocalDateTime.now().plusMinutes(request.getValidity() != null ? request.getValidity() : 30));
        entity.setClickCount(0);
        entity.setClickDetails("");

        repo.save(entity);

        UrlResponse res = new UrlResponse();
        res.setShortLink("http://hostname:port/" + shortcode);
        res.setExpiry(entity.getExpiryAt().toString());
        return res;
    }

    public UrlEntity redirect(String shortcode) {
        UrlEntity entity = repo.findById(shortcode).orElseThrow(() -> new RuntimeException("Invalid shortcode"));
        if (LocalDateTime.now().isAfter(entity.getExpiryAt())) throw new RuntimeException("Link expired");
        entity.setClickCount(entity.getClickCount() + 1);
        repo.save(entity);
        return entity;
    }

    public UrlEntity getStats(String shortcode) {
        return repo.findById(shortcode).orElseThrow(() -> new RuntimeException("Shortcode not found"));
    }
}