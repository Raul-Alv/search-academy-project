package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.empathy.academy.search.configuration.ElasticsearchCall;
import co.empathy.academy.search.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
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
        elastic.establishConnection().indices().delete(i -> i.index("imdb"));
        elastic.establishConnection().indices().create(i -> i.index("imdb"));

     }

     public void createIndex(InputStream input){
         CreateIndexResponse indexResp = null;
         try {
             indexResp = elastic.establishConnection().indices().create(
                     ind -> ind.index("movies").withJson(input)
             );
             if(indexResp.acknowledged()) {
                 System.out.println("Index created");
             }
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }

     public void indexDocument(List<Movie> movies){
        if(!movies.isEmpty()) {
            try {
                BulkRequest.Builder br = new BulkRequest.Builder();
                for (Movie m : movies) {
                    br.operations(op -> op
                            .index(idx -> idx
                                    .index("imdb")
                                    .id(m.getTconst())
                                    .document(m)));
                    System.out.println("indexada: " + m.getTconst());
                }
                BulkResponse result = elastic.establishConnection().bulk(br.build());
                if(result.errors()) {
                    System.out.println("Bulk had errors");
                    for(BulkResponseItem item : result.items()) {
                        if(item.error() != null) {
                            System.out.println(item.error().reason());
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
     }


}
