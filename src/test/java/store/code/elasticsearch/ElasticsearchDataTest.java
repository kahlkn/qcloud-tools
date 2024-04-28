package store.code.elasticsearch;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.mock.MockUtils;
import artoria.test.pojo.entity.system.User;
import artoria.util.RandomUtils;
import artoria.util.StringUtils;
import cn.hutool.json.JSONUtil;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

import static artoria.common.Constants.ZERO;

@Ignore
public class ElasticsearchDataTest {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchDataTest.class);
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
        CreateIndexRequest request = new CreateIndexRequest("test_user");
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
        // 准备创建映射的请求
        PutMappingRequest request = new PutMappingRequest("test_user");
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
                "    \"properties\": {\n" +
                "    \"id\": {\n" +
                "      \"type\":\"long\"\n" +
                "    },\n" +
                "    \"nickname\": {\n" +
                "      \"type\":\"keyword\"\n" +
                "    },\n" +
                "    \"avatar\": {\n" +
                "      \"type\":\"keyword\"\n" +
                "    },\n" +
                "    \"name\": {\n" +
                "      \"type\":\"keyword\"\n" +
                "    },\n" +
                "    \"gender\": {\n" +
                "      \"type\":\"keyword\"\n" +
                "    },\n" +
                "    \"age\": {\n" +
                "      \"type\":\"integer\"\n" +
                "    },\n" +
                "    \"birthday\": {\n" +
                "      \"type\":\"date\"\n" +
                "    },\n" +
                "    \"height\": {\n" +
                "      \"type\":\"float\"\n" +
                "    },\n" +
                "    \"weigh\": {\n" +
                "      \"type\":\"float\"\n" +
                "    },\n" +
                "    \"phoneNumber\": {\n" +
                "      \"type\":\"keyword\"\n" +
                "    },\n" +
                "    \"introduce\": {\n" +
                "      \"type\":\"text\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        log.info("mapping: \n{}", mappingSource);
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
        for (int i = 118199 ; i < 10000000; i++) {
            User user = MockUtils.mock(User.class);
            user.setId(Integer.valueOf(i).longValue());
            user.setGender(RandomUtils.nextInt(2)>1?"男":"女");
            user.setAge(RandomUtils.nextInt(70) + 10);
            user.setBirthday(new Date());
            user.setHeight(String.valueOf(RandomUtils.nextInt(50) + 150));
            user.setWeigh(String.valueOf(RandomUtils.nextInt(20) + 40));
            user.setIntroduce(RandomUtils.nextString(RandomUtils.nextInt(1000)+2000));
            // 准备插入数据的请求
            IndexRequest request = new IndexRequest("test_user");
            request.source(JSONUtil.toJsonStr(user), XContentType.JSON);
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

}
