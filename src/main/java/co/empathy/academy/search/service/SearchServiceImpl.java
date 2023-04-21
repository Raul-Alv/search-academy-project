package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.empathy.academy.search.configuration.ElasticsearchCall;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.repositories.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> searchGenre(String query) throws IOException {

        return null;
    }

    @Override
    public List<Movie> termSearch(String query, String field) throws IOException {
        System.out.println("entra");
        Query termQuery = TermQuery.of(t -> t
                .value(query)
                .field(field))._toQuery();
        return elasticClient.executeQuery(termQuery, 100);
    }

    @Override
    public List<Movie> multiTermSearch(String query, String field) {
        return null;
    }

    
}
