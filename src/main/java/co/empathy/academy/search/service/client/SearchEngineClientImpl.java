package co.empathy.academy.search.service.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.academy.search.repositories.ElasticClient;

import java.io.IOException;


public class SearchEngineClientImpl implements SearchEngineClient {

    private SearchEngineClientImpl(){

    }

    @Override
    public String executeQuery(String query) {
        return "query result";
    }

    @Override
    public String getClusterName(ElasticClient client) throws IOException {
        return client.getClusterName();
    }
}
