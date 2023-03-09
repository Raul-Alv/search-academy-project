package co.empathy.academy.search.service;

import co.empathy.academy.search.configuration.ElasticsearchCall;
import co.empathy.academy.search.repositories.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private final ElasticClient elasticClient;

    public SearchServiceImpl(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    // We use a DI approach, SearchService receives its dependencies via constructor
    // If we created the client directly in the constructor we would be strongly coupling this class to one of its dependencies
    // As a result it would be very difficult to swap implementations for different purposes and sometimes downright impossible to unit test


    @Override
    public String search(String query) throws IOException {
        String result = "Query: " + query + ", Cluster name: " + elasticClient.getClusterName();
        return result;
    }
}
