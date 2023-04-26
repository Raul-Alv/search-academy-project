package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.BucketAggregationBase;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import co.empathy.academy.search.configuration.ElasticsearchCall;
import co.empathy.academy.search.models.Facet;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.repositories.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

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
        Query termQuery = TermQuery.of(t -> t
                .value(query)
                .field(field))._toQuery();
        return elasticClient.executeQuery(termQuery, 100);
    }

    @Override
    public List<Movie> multiTermSearch(String query, String field) {
        return null;
    }

    @Override
    public List<Movie> termAndFilterSearch(String query, String field, String filterField, String filterValue) throws IOException {
        BoolQuery boolQuery = BoolQuery.of(b -> b
                .must(MatchQuery.of(t -> t
                        .query(query)
                        .field(field))._toQuery())
                .filter(RangeQuery.of(t -> t
                        .field(filterField)
                        .gte(JsonData.of(filterValue))).
                        _toQuery())
        );
        return elasticClient.executeBoolQuery(boolQuery, 100);
    }

    @Override
    public List<Movie> quizResult(String generoQuery, String duracionMinQuery, String duracionMaxQuery, String ratingQuery) throws IOException {
        BoolQuery boolQuery = BoolQuery.of(b -> b.
                must(MatchQuery.of(t -> t
                        .query(generoQuery)
                        .field("genres"))._toQuery())
                .must(RangeQuery.of(t -> t
                        .field("runtimeMinutes")
                        .gte(JsonData.of(duracionMinQuery))
                        .lte(JsonData.of(duracionMaxQuery))).
                        _toQuery())
                .must(RangeQuery.of(t -> t
                        .field("averageRating")
                        .lte(JsonData.of(ratingQuery))).
                        _toQuery())
        );
        return elasticClient.executeBoolQuery(boolQuery, 100);
    }

    @Override
    public List<Movie> latest() throws IOException {
        Query latest = RangeQuery.of(t -> t
                .field("startYear")
                .gte(JsonData.of("2023"))).
                _toQuery();

        FieldSort sorted = FieldSort.of(t -> t
                .field("startYear")
                .order(SortOrder.Desc));

        SortOptions sortOptions = SortOptions.of(t -> t.field(sorted));

        return elasticClient.executeAndSortQuery(latest, sortOptions, 100);
    }

    @Override
    public Facet generes() throws IOException {
        Query query = BoolQuery.of(b -> b
                .filter(MatchAllQuery.of(q -> q.queryName("MatchAll"))._toQuery()))._toQuery();

        Aggregation aggs = TermsAggregation.of(t -> t.field("genres").size(100))._toAggregation();
        Map<String, Aggregation> aggsMap = new HashMap<String, Aggregation>();
        aggsMap.put("genres", aggs);

        return elasticClient.executeAggregation(query, aggsMap, 100);
    }


}
