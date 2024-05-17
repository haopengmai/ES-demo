package com.basumatarau.training;

import org.apache.http.HttpHost;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;


public enum ElasticsearchClientProvider {
    INSTANCE;

    private final RestClient restClientBlocking;

    public RestClient getRestClientBlocking() {
        return restClientBlocking;
    }

    ElasticsearchClientProvider() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("172.31.101.75", 9200, "http"));

        restClientBlocking = builder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS).build();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
