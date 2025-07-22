package org.example.repository;


import org.example.model.UrlMapping;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    public UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUser(User user);
}
