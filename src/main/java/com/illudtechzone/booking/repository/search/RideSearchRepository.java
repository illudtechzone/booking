package com.illudtechzone.booking.repository.search;

import com.illudtechzone.booking.domain.Ride;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ride entity.
 */
public interface RideSearchRepository extends ElasticsearchRepository<Ride, Long> {
}
