package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.empathy.academy.search.configuration.ElasticsearchCall;
import co.empathy.academy.search.models.Movie;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
                                s.index("imdb")
                                .size(size),
                        Movie.class);

        return response.hits().hits().stream().map(h -> h.source()).toList();
    }

    public List<Movie> executeMultipleQuery(List<Query> termQuery, int i) {
        return null;
    }
}
