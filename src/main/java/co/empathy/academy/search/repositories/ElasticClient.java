package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.academy.search.configuration.ElasticsearchCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ElasticClient {

    @Autowired
    private final ElasticsearchCall elastic;

    public ElasticClient(ElasticsearchCall elastic){

         this.elastic = elastic;
     }

     public String getClusterName() throws IOException {
         System.out.println("Pruebas: " + elastic.establishConnection().cluster());
         return elastic.establishConnection().cluster().health().clusterName();
     }
}
