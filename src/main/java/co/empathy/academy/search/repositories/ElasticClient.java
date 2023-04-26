package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.BucketAggregationBase;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.empathy.academy.search.configuration.ElasticsearchCall;
import co.empathy.academy.search.models.Facet;
import co.empathy.academy.search.models.FacetValues;
import co.empathy.academy.search.models.Movie;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ElasticClient {

    @Autowired
    private final ElasticsearchCall elastic;

    public ElasticClient(ElasticsearchCall elastic){

         this.elastic = elastic;
     }

     public String getClusterName() throws IOException {
         return elastic.establishConnection().cluster().health().clusterName();
     }

     public void createIndex() throws IOException {
        try {
            elastic.establishConnection().indices().delete(i -> i.index("imdb"));
        } catch (Exception e) {
            System.out.println("Index not found");
        }
        elastic.establishConnection().indices().create(i -> i.index("imdb"));

     }

     public void indexDocument(List<Movie> movies){
        try {
            BulkRequest.Builder br = new BulkRequest.Builder();
            /*
            movies.forEach(m ->
                    br.operations(op -> op
                    .index(idx -> idx
                            .index("imdb")
                            .id(m.getTconst())
                            .document(m))));

             */
            for (Movie m : movies) {
                br.operations(op -> op
                        .index(idx -> idx
                                .index("imdb")
                                .id(m.getTconst())
                                .document(m)));
                System.out.println("indexada: " + m.getTconst());
            }
            BulkResponse result = elastic.establishConnection().bulk(br.build());
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     }

    public void mapping() throws IOException {
        InputStream mapping = getClass().getClassLoader().getResourceAsStream("mapping.json");
        //System.out.println(IOUtils.toString(mapping, StandardCharsets.UTF_8));
        elastic.establishConnection().indices().putMapping(p -> p.index("imdb").withJson(mapping));
    }

    public void settings() throws IOException {
        elastic.establishConnection().indices().close(c -> c.index("imdb"));

        InputStream analyzer = getClass().getClassLoader().getResourceAsStream("analyzer.json");
        elastic.establishConnection().indices().putSettings(p -> p.index("imdb").withJson(analyzer));

        elastic.establishConnection().indices().open(o -> o.index("imdb"));
    }

    public List<Movie> executeQuery(Query query, int size) throws IOException {
        SearchResponse<Movie> response =
                elastic.establishConnection().search(s ->
                                s.index("imdb").query(query)
                                .size(size),
                        Movie.class);
        List<Movie> a = response.hits().hits().stream().map(h -> h.source()).toList();
        return response.hits().hits().stream().map(h -> h.source()).toList();
    }

    public List<Movie> executeAndSortQuery(Query query, SortOptions sorter, int size) throws IOException{
        SearchResponse<Movie> response =
                elastic.establishConnection().search(s ->
                                s.index("imdb").query(query).sort(sorter)
                                        .size(size),
                        Movie.class);
        List<Movie> a = response.hits().hits().stream().map(h -> h.source()).toList();
        return response.hits().hits().stream().map(h -> h.source()).toList();
    }

    public List<Movie> executeMultipleQuery(List<Query> termQuery, int i) {
        return null;
    }

    public void indexSingleDocument(Movie m) {
        try {
            BulkRequest.Builder br = new BulkRequest.Builder();
                br.operations(op -> op
                        .index(idx -> idx
                                .index("imdb")
                                .id(m.getTconst())
                                .document(m)));
                System.out.println("indexada: " + m.getTconst());

            BulkResponse result = elastic.establishConnection().bulk(br.build());
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> executeBoolQuery(BoolQuery boolQuery, int size) throws IOException {
        //Query query = boolQuery._toQuery();
        SearchResponse<Movie> response =
                elastic.establishConnection().search(s ->
                                s.index("imdb")
                                        .query(boolQuery._toQuery())
                                        .size(size),
                        Movie.class);
        List<Movie> a = response.hits().hits().stream().map(h -> h.source()).toList();
        return response.hits().hits().stream().map(h -> h.source()).toList();
    }


    public Facet executeAggregation(Query query, Map<String, Aggregation> aggs, int size) throws IOException {
        SearchResponse<Movie> response =
                elastic.establishConnection().search(s ->
                                s.index("imdb")
                                        .query(query)
                                        .aggregations(aggs)
                                        .size(size),
                        Movie.class);
        List<FacetValues> values = new ArrayList<>();
        Aggregate ag = response.aggregations().get("genres");
        ag.sterms().buckets().array().forEach(b -> {
                values.add(
                        new FacetValues(b.key().toString().toLowerCase(), b.key().toString().toLowerCase(), b.docCount(), "genres: " + b.key()));
        });
        return new Facet("facetGenres", "values", values);
    }
}
