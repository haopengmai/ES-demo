package com.basumatarau.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ElasticSearchLowLevelClientTest {

    private RestClient elasticSearchRestClient = ElasticsearchClientProvider.INSTANCE.getRestClientBlocking();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenDomainObjectPersisted_thenDomainObjectGetsRetrieved() throws IOException {
        // Create a request to search for documents in the turbodesk index
        Request request = new Request("GET", "/turbodesk/_search");
        request.addParameter("pretty", "true");

        // Define the search query to match documents with message field as engine/log/collect/publish/fail
        String queryJson = "{\"query\": {\"match\": {\"message\": \"engine/log/collect/publish/fail\"}}}";
        request.setEntity(new NStringEntity(queryJson, ContentType.APPLICATION_JSON));

        // Execute the request and get the response
        Response response = elasticSearchRestClient.performRequest(request);
        String stringEntity = EntityUtils.toString(response.getEntity());
        assertThat(stringEntity, notNullValue());
        System.out.println(stringEntity);
    }

    private String getContentAsString(StringBuilder builder, Response response) {
        String content;
        try(BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent()))){
            builder.setLength(0);
            String line;
            while((line = bufferedReader.readLine()) != null){
                builder.append(line);
            }
            content = builder.toString();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return content;
    }
}

