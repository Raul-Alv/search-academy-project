package co.empathy.academy.search.service.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.academy.search.repositories.ElasticClient;

import java.io.IOException;

public interface SearchEngineClient {

    String executeQuery(String query);
    String getClusterName (ElasticClient client) throws IOException;
}
