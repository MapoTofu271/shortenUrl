package org.example.repository;

import org.example.model.ClickEvent;
import org.example.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ClickEventsRepository extends JpaRepository<ClickEvent, Long> {
    //SHow click events of 1 urlmapping
    List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping urlMapping, LocalDateTime start, LocalDateTime end);

    //Show all the click events of all urlmapping
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(Collection<UrlMapping> urlMappings, LocalDateTime start, LocalDateTime end);

}
