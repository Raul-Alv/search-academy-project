package co.empathy.academy.search.service;

import co.empathy.academy.search.service.client.SearchEngineClient;

public class SearchServiceImpl implements SearchService {

    private final SearchEngineClient searchEngine;

    // We use a DI approach, SearchService receives its dependencies via constructor
    // If we created the client directly in the constructor we would be strongly coupling this class to one of its dependencies
    // As a result it would be very difficult to swap implementations for different purposes and sometimes downright impossible to unit test
    public SearchServiceImpl(SearchEngineClient searchEngine) {
        this.searchEngine = searchEngine;
    }

    @Override
    public String search(String query) {
        if (!query.isBlank()) {
            return searchEngine.executeQuery(query);
        }
        return "";
    }
}
