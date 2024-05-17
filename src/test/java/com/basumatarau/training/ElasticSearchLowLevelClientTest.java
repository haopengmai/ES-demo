package com.basumatarau.training;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ElasticSearchLowLevelClientTest {

    private RestClient elasticSearchRestClient = ElasticsearchClientProvider.INSTANCE.getRestClientBlocking();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void failpublish() throws IOException {
        // Read query JSON from external file
//        DSL语句默认情况下会分页，要自己设置size才会返回所有的信息
        String queryJson = new String(Files.readAllBytes(Paths.get("src/test/java/resources/publis_fail_DSL.json")));

        // Create a request to search for documents in the turbodesk-* index pattern
        Request request = new Request("GET", "/turbodesk-*/_search");
        request.addParameter("pretty", "true");

        // Set the entity with the content of queryJson
        request.setEntity(new NStringEntity(queryJson, ContentType.APPLICATION_JSON));

        // Execute the request and get the response
        Response response = elasticSearchRestClient.performRequest(request);
        String stringEntity = EntityUtils.toString(response.getEntity());
        assertThat(stringEntity, notNullValue());

        Map<String, Object> responseMap = objectMapper.readValue(stringEntity, Map.class);
        Map<String, Object> hitsMap = (Map<String, Object>) responseMap.get("hits");
        List<Map<String, Object>> hits = (List<Map<String, Object>>) hitsMap.get("hits");

        for (Map<String, Object> hit : hits) {
            Map<String, Object> source = (Map<String, Object>) hit.get("_source");
            String userId = (String) source.get("userId");
            String message = (String) source.get("message");
            //        engine/log/collect/publish/fail 接口下发生错误的全量信息
//            System.out.println("{\n  \"_source\": {\n    \"userId\": \"" + userId + "\",\n    \"message\": \"" + message + "\"\n  }\n}");

            // engine/log/collect/publish/fail 接口下发生->文章同步出错，稍后再试哦
            if (message.contains("文章同步出错，稍后再试哦")) {
                System.out.println("{\n  \"_source\": {\n    \"userId\": \"" + userId + "\",\n    \"message\": \"" + message + "\"\n  }\n}");
            }
            // engine/log/collect/publish/fail 封面图下载不畅
            if (message.contains("封面图下载不畅")) {
                System.out.println("{\n  \"_source\": {\n    \"userId\": \"" + userId + "\",\n    \"message\": \"" + message + "\"\n  }\n}");
            }
            // engine/log/collect/publish/fail 百家号发布失败
            if (message.contains("百家号发布失败")) {
                System.out.println("{\n  \"_source\": {\n    \"userId\": \"" + userId + "\",\n    \"message\": \"" + message + "\"\n  }\n}");
            }
        }
    }

    @Test
    public void trace_stack_info() throws IOException
    {
        // Read query JSON from external file
//        DSL语句默认情况下会分页，要自己设置size才会返回所有的信息
        String queryJson = new String(Files.readAllBytes(Paths.get("src/test/java/resources/trace_stackinfo_DSL.json")));

        // Create a request to search for documents in the turbodesk-* index pattern
        Request request = new Request("GET", "/turbodesk-*/_search");
        request.addParameter("pretty", "true");

        // Set the entity with the content of queryJson
        request.setEntity(new NStringEntity(queryJson, ContentType.APPLICATION_JSON));

        // Execute the request and get the response
        Response response = elasticSearchRestClient.performRequest(request);
        String stringEntity = EntityUtils.toString(response.getEntity());
        assertThat(stringEntity, notNullValue());

        Map<String, Object> responseMap = objectMapper.readValue(stringEntity, Map.class);
        Map<String, Object> hitsMap = (Map<String, Object>) responseMap.get("hits");
        List<Map<String, Object>> hits = (List<Map<String, Object>>) hitsMap.get("hits");
////        engine/log/collect/publish/fail 接口下发生错误的全量信息
        for (Map<String, Object> hit : hits) {
            Map<String, Object> source = (Map<String, Object>) hit.get("_source");
            String userId = (String) source.get("userId");
            String message = (String) source.get("message");
            //        engine/log/collect/publish/fail 接口下发生错误的全量信息
            System.out.println("{\n  \"_source\": {\n    \"userId\": \"" + userId + "\",\n    \"message\": \"" + message + "\"\n  }\n}");

//            // engine/log/collect/publish/fail 接口下发生->文章同步出错，稍后再试哦
//            if (message.contains("文章同步出错，稍后再试哦")) {
//                System.out.println("{\n  \"_source\": {\n    \"userId\": \"" + userId + "\",\n    \"message\": \"" + message + "\"\n  }\n}");
//            }
//            // engine/log/collect/publish/fail 封面图下载不畅
//            if (message.contains("封面图下载不畅")) {
//                System.out.println("{\n  \"_source\": {\n    \"userId\": \"" + userId + "\",\n    \"message\": \"" + message + "\"\n  }\n}");
//            }
//            // engine/log/collect/publish/fail 百家号发布失败
//              if (message.contains("百家号发布失败，需要手动继续完成发布哦")) {
//                System.out.println("{\n  \"_source\": {\n    \"userId\": \"" + userId + "\",\n    \"message\": \"" + message + "\"\n  }\n}");
//            }
        }
    }
}

