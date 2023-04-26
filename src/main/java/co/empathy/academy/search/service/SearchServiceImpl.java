package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import co.empathy.academy.search.models.Facet;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.repositories.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private final ElasticClient elasticClient;


    public SearchServiceImpl(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    /**
     * Get the cluster name from the elastic client
     * @param query text to add as a test
     * @return
     * @throws IOException
     */
    @Override
    public String search(String query) throws IOException {
        String result = "Query: " + query + ", Cluster name: " + elasticClient.getClusterName();
        return result;
    }

    /**
     * Method to get all genres (not implemented)
     * @param query
     * @return
     * @throws IOException
     */
    @Override
    public List<String> searchGenre(String query) throws IOException {

        return null;
    }

    /**
     * Method to get a query to search a name and execute it
     * @param query term to search
     * @param field field to search the term
     * @return
     * @throws IOException
     */
    @Override
    public List<Movie> termSearch(String query, String field) throws IOException {
        Query termQuery = TermQuery.of(t -> t
                .value(query)
                .field(field))._toQuery();
        return elasticClient.executeQuery(termQuery, 100);
    }

    /**
     * Method to search a movie term and add a filter by a category
     * @param query
     * @param field
     * @param filterField
     * @param filterValue
     * @return
     * @throws IOException
     */
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

    /**
     * Method to get the results for the quiz
     * @param genreQuery
     * @param minDurationQuery
     * @param maxDurationQuery
     * @param ratingQuery
     * @return
     * @throws IOException
     */
    @Override
    public List<Movie> quizResult(String genreQuery, String minDurationQuery, String maxDurationQuery, String ratingQuery) throws IOException {
        BoolQuery boolQuery = BoolQuery.of(b -> b.
                must(MatchQuery.of(t -> t
                        .query(genreQuery)
                        .field("genres"))._toQuery())
                .must(RangeQuery.of(t -> t
                        .field("runtimeMinutes")
                        .gte(JsonData.of(minDurationQuery))
                        .lte(JsonData.of(maxDurationQuery))).
                        _toQuery())
                .must(RangeQuery.of(t -> t
                        .field("averageRating")
                        .lte(JsonData.of(ratingQuery))).
                        _toQuery())
        );
        return elasticClient.executeBoolQuery(boolQuery, 100);
    }

    /**
     * Method to get the latest movies
     * @return
     * @throws IOException
     */
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

    /**
     * Method to get all genres
     * @return
     * @throws IOException
     */
    @Override
    public Facet genres() throws IOException {
        Query query = BoolQuery.of(b -> b
                .filter(MatchAllQuery.of(q -> q.queryName("MatchAll"))._toQuery()))._toQuery();

        Aggregation aggs = TermsAggregation.of(t -> t.field("genres").size(100))._toAggregation();
        Map<String, Aggregation> aggsMap = new HashMap<String, Aggregation>();
        aggsMap.put("genres", aggs);

        return elasticClient.executeAggregation(query, aggsMap, 100);
    }


}
