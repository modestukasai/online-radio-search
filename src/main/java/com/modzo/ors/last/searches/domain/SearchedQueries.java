package com.modzo.ors.last.searches.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.stream.Stream;

public interface SearchedQueries extends ElasticsearchRepository<SearchedQuery, String> {

    void deleteAllByQueryAndType(String query, SearchedQuery.Type type);

}