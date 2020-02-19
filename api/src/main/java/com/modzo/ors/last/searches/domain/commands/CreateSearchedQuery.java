package com.modzo.ors.last.searches.domain.commands;

import com.modzo.ors.stations.domain.DomainException;
import com.modzo.ors.last.searches.domain.SearchedQueries;
import com.modzo.ors.last.searches.domain.SearchedQuery;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Component;

public class CreateSearchedQuery {

    private final String query;

    public CreateSearchedQuery(String query) {
        this.query = query;
    }

    @Component
    public static class Handler {
        private final SearchedQueries searchedQueries;

        private final Validator validator;

        public Handler(SearchedQueries searchedQueries,
                       Validator validator) {
            this.searchedQueries = searchedQueries;
            this.validator = validator;
        }

        public SearchedQuery handle(CreateSearchedQuery command) {
            validator.validate(command);
            return searchedQueries.save(new SearchedQuery(command.query));
        }
    }

    @Component
    private static class Validator {

        void validate(CreateSearchedQuery command) {
            if (StringUtil.isBlank(command.query)) {
                throw new DomainException("SEARCH_QUERY_NOT_BLANK", "Searched query was blank");
            }
        }
    }
}
