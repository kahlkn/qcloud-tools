package store.code.elasticsearch;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.mock.MockUtils;
import artoria.util.StringUtils;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.ZERO;

@Ignore
public class ElasticsearchTest {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchTest.class);
    private static RestHighLevelClient restHighLevelClient;

    static {
        String[] uris = new String[]{"http://10.23.31.111:9200"};
        String username = "elastic";
        String password = "JnLD6m8IxE3kuRF2O77_";
        HttpHost[] hosts = new HttpHost[uris.length];
        for (int i = ZERO; i < uris.length; i++) {
            String uri = uris[i];
            if (StringUtils.isBlank(uri)) { continue; }
            hosts[i] = HttpHost.create(uri);
        }
        RestClientBuilder restClientBuilder = RestClient.builder(hosts);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials(username, password);
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        restClientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        });
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
    }

    @Test
    public void createIndexTest() throws Exception {
        // 准备创建索引的请求
        CreateIndexRequest request = new CreateIndexRequest("test_index1");
        // 设置索引的设置（可选）
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );
        // 执行创建索引请求
        CreateIndexResponse createIndexResponse = restHighLevelClient
                .indices().create(request, RequestOptions.DEFAULT);
        // 处理响应
        boolean acknowledged = createIndexResponse.isAcknowledged();
        log.info("Create index acknowledged: {}", acknowledged);
    }

    @Test
    public void putMappingTest() throws Exception {
        // 修改 mapping 时，修改原字段类型将会报错，但是增加新的字段是没关系的，并且不会报错的。
        // 创建映射的内容
        /*XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("field1");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();*/
        // 设置映射的内容
//        request.source(builder);
        String mappingSource = "{\n" +
                "  \"properties\": {\n" +
                "    \"name\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"fields\": {\n" +
                "        \"keyword\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"age\": {\n" +
                "      \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"email\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"index\": false\n" +
                "    },\n" +
                "    \"address\": {\n" +
                "      \"type\": \"nested\",\n" +
                "      \"properties\": {\n" +
                "        \"street\": { \"type\": \"text\" },\n" +
                "        \"city\": { \"type\": \"text\" },\n" +
                "        \"zip\": { \"type\": \"keyword\" }\n" +
                "        }\n" +
                "    },\n" +
                "    \"fullAddress\": {\n" +
                "      \"type\": \"text\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        /*String mappingSource = "{\n" +
                "  \"properties\": {\n" +
                "    \"name\": {\n" +
                "      \"type\": \"text\"\n" +
                "    },\n" +
                "    \"age\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"email\": {\n" +
                "      \"type\": \"text\"\n" +
                "    },\n" +
                "    \"address\": {\n" +
                "      \"type\": \"nested\",\n" +
                "      \"properties\": {\n" +
                "        \"street\": { \"type\": \"text\" },\n" +
                "        \"city\": { \"type\": \"text\" },\n" +
                "        \"zip\": { \"type\": \"keyword\" }\n" +
                "       }\n" +
                "     },\n" +
                "    \"address1\": {\n" +
                "      \"type\": \"text\"\n" +
                "      }\n" +
                "  }\n" +
                "}\n";*/
        log.info(JSONUtil.toJsonPrettyStr(mappingSource));
        // 准备创建映射的请求
        PutMappingRequest request = new PutMappingRequest("test_index");
        request.source(mappingSource, XContentType.JSON);
        // 执行创建映射请求
        AcknowledgedResponse putMappingResponse = restHighLevelClient
                .indices().putMapping(request, RequestOptions.DEFAULT);
        // 处理响应
        boolean acknowledged = putMappingResponse.isAcknowledged();
        log.info("Put mapping acknowledged: {}", acknowledged);
    }

    @Test
    public void addDocTest() throws Exception {
        // 准备索引数据的JSON字符串
        for (int i = 0; i < 10000000; i++) {
            String name = MockUtils.mock(String.class);
            String jsonString = "{\n" +
                    "    \"address\": {\n" +
                    "        \"zip\": \"" + i + "\",\n" +
                    "        \"city\": \"city1\",\n" +
                    "        \"street\": \"street1\"\n" +
                    "    },\n" +
                    "    \"name\": \"" + name + i + "\",\n" +
                    "    \"age\": 31,\n" +
                    "    \"email\": \"" + name + i + "@example.com\",\n" +
                    "    \"fullAddress\": \"fullAddressfullAddressfullAddressfullAddressfullAddress\"\n" +
                    "}";
            // 准备插入数据的请求
            IndexRequest request = new IndexRequest("test_index");
            request.source(jsonString, XContentType.JSON);
            // 执行插入数据请求
            try {
                IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
                // 处理响应
                String id = indexResponse.getId();
                log.info("Document indexed with ID: {}", id);
            }
            catch (Exception e) {
                log.error("error! ", e);
            }
        }
    }

    @Test
    public void deleteDocTest() throws Exception {
        // 准备要删除的文档ID
        String documentId = "IA-30I4BgeyemHyIv1-b";
        // 准备删除数据的请求
        DeleteRequest request = new DeleteRequest("test_index", documentId);
        // 执行删除数据请求
        DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        // 处理响应
//        deleteResponse.getResult() == DeleteResponse.Result.DELETED
        log.info("Document delete result: {}", deleteResponse.getResult());
    }

    @Test
    public void reindexTest() throws Exception {
        // Elasticsearch 的 Reindex 操作通常是原子性的，事务性操作、幂等性、失败回滚。
        // 即操作要么全部成功，要么全部失败，不存在部分成功或部分失败的情况。
        // Reindex 操作时，可以将比如 integer 转换成 text 等
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 准备 reindex 的请求
        ReindexRequest request = new ReindexRequest();
        // 设置源索引
        request.setSourceIndices("test_user");
        // 设置目标索引
        request.setDestIndex("test_user1");
        // 可选：使用QueryBuilders设置查询条件
        request.setSourceQuery(QueryBuilders.matchAllQuery());
        // 可选：设置批量请求大小和超时时间
        request.setSourceBatchSize(300);
        request.setDestOpType("create");
        request.setRefresh(true);
        request.setTimeout(TimeValue.timeValueMinutes(10));
        // 执行异步 reindex 操作
        Cancellable cancellable = restHighLevelClient.reindexAsync(
                request, RequestOptions.DEFAULT, new ActionListener<BulkByScrollResponse>() {
            @Override
            public void onResponse(BulkByScrollResponse response) {
                stopWatch.stop();
                log.info("reindex end, time {}ms, response = {}", stopWatch.getLastTaskTimeMillis(), response.toString());
            }
            @Override
            public void onFailure(Exception e) {
                log.error("reindexAsync error! ", e);
            }
        });
        ThreadUtil.sleep(5, TimeUnit.MINUTES);
        // 执行同步 reindex 操作
        /*BulkByScrollResponse response = restHighLevelClient.reindex(request, RequestOptions.DEFAULT);
        stopWatch.stop();
        log.info("reindex end, time {}ms, response = {}", stopWatch.getLastTaskTimeMillis(), response.toString());*/
    }

}
