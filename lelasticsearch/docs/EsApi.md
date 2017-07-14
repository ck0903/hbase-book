# Elasticsearch JAVA API
## 1,��Ҫ��ӵ�������
```
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>transport</artifactId>
    <version>5.5.0</version>
</dependency>
```
## 2�� �ռ���Ϣ������
```
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.8.2</version>
</dependency>

resources �����log4j2.properties
appender.console.type = Console
appender.console.name = console
appender.console.layout.type = PatternLayout
rootLogger.level = info
rootLogger.appenderRef.console.ref = console
```
## 3��ͨ��TransportClient��ES��Ⱥ��������
```java
//  �����뵽��Ⱥ����
//  ��ȡ����Client
TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
    .addTransportAddress(new InetSocketTransportAddress(
            InetAddress.getByName("host1"), 9300))
    .addTransportAddress(new InetSocketTransportAddress(
            InetAddress.getByName("host2"), 9300));
//  shutdown
client.close();
//client ���Զ���̽��Ⱥ���ݽڵ�Ĺ��ܣ�Ĭ�ϲ�������
Settings settings = Settings.builder()
    .put("client.transport.sniff", true).build();
TransportClient client = new PreBuiltTransportClient(settings);
```
## 4���򵥵�API
### 4.1 ���������ļ�api CRUD
#### 4.1.1 �������� IndexRespose��
```
����Ҫ����һ��Json �ĵ��������¼��ְ취��������Jason �ĵ���
I���ֶ����죺
    String json = "{" +
    "\"user\":\"kimchy\"," +
    "\"postDate\":\"2013-01-30\"," +
    "\"message\":\"trying out Elasticsearch\"" +
    "}";

II��ʹ��Map ����ʽ��
    Map<String, Object> json = new HashMap<String, Object>();
    json.put("user","kimchy");
    json.put("postDate",new Date());
    json.put("message","trying out Elasticsearch");

III�� ʹ��jackson, ������jar ��
    import com.fasterxml.jackson.databind.*;
    // instance a json mapper
    ObjectMapper mapper = new ObjectMapper(); // create once, reuse
    // generate json
    byte[] json = mapper.writeValueAsBytes(yourbeaninstance);

IV��ʹ���ڲ���ES API:(����ֱ�Ӳ鿴���ɵ�json ����String json = builder.string();)
    import static org.elasticsearch.common.xcontent.XContentFactory.*;
    XContentBuilder builder = jsonBuilder()
    .startObject()
    .field("user", "kimchy")
    .field("postDate", new Date())
    .field("message", "trying out Elasticsearch")
    .endObject()
���µ���������һ��json�ĵ���һ������twitter �ģ�����Ϊtweet ��ֵΪ1��������
    import static org.elasticsearch.common.xcontent.XContentFactory.*;

    IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
    .setSource(jsonBuilder()
    .startObject()
    .field("user", "kimchy")
    .field("postDate", new Date())
    .field("message", "trying out Elasticsearch")
    .endObject()
    )
    .get();  // ��˱���԰�����������ES ��
����ͨ���������ݻ�ȡ���������͡�id����
// Index name
String _index = response.getIndex();
// Type name
String _type = response.getType();
// Document ID (generated or not)
String _id = response.getId();
// Version (if it's the first time you index this document, you will get: 1)
long _version = response.getVersion();
// status has stored current instance statement.
ͨ�����·������鿴�Ƿ�ִ�гɹ���
RestStatus status = response.status();
```
#### 4.1.2, ��ȡ�����Լ���������GetResponse
```java
GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
ͨ��response.isExists() �ж������Ƿ����
```
#### 4.1.3�� ɾ��������DeleteResponse
```java
DeleteResponse response = client.prepareDelete("twitter", "tweet", "1").get();
ͨ��response.status �鿴����״̬
```
#### 4.1.4�� ͨ���Բ�ѯ���Ľ������ɾ��
```java
���µ�ִ��ʱָ��������persons ��������Ա�ΪŮ�ģ���ɾ����
BulkByScrollResponse response =
    DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
    .filter(QueryBuilders.matchQuery("gender", "male"))   // ��ѯ
    .source("persons")     // ����
    .get();          // ִ�в���                                   
long deleted = response.getDeleted();  // �鿴����ɾ���˼�������    

�����ǣ������첽ɾ���Ĳ�����
DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
    .filter(QueryBuilders.matchQuery("gender", "male"))                  
    .source("persons")
    .execute(new ActionListener<BulkIndexByScrollResponse>() {           
        @Override
        public void onResponse(BulkIndexByScrollResponse response) {
            long deleted = response.getDeleted();                        
        }
        @Override
        public void onFailure(Exception e) {
        // Handle the exception
    }
    });
```
#### 4.1.5�����µ�API
```java
1�� ��UpdateRequest ����client ���и���
UpdateRequest updateRequest = new UpdateRequest();
updateRequest.index("index");
updateRequest.type("type");
updateRequest.id("1");
updateRequest.doc(jsonBuilder()
    .startObject()
        .field("gender", "male")
    .endObject());
client.update(updateRequest).get();
��ͬ���£�
UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
    .doc(jsonBuilder()
    .startObject()
    .field("gender", "male")
    .endObject());
    client.update(updateRequest).get();

UpdateRequest Ҳ�������ó�Script ģʽ��
UpdateRequest updateRequest = new UpdateRequest("ttl", "doc", "1")
    .script(new Script("ctx._source.gender = \"male\""));
    client.update(updateRequest).get();

2��ʹ��client ��prepareUpdate ������
A��client.prepareUpdate("ttl", "doc", "1")
    .setScript(new Script("ctx._source.gender = \"male\""  
    , ScriptService.ScriptType.INLINE, null, null))
    .get();
    // ������ļ�������ͨ������SriptService.SriptType.File, ������Script �ļ�
B��
client.prepareUpdate("ttl", "doc", "1")
    .setDoc(jsonBuilder()
    .startObject()
    .field("gender", "male")
    .endObject())
    .get();


3������һ��RequestIndex �����û���򲻸���
�����������RequestIndex�е�Joe Smith, �����³�
 Joe Smith��������Mac Smith��, female,
��������ڣ�����ǲ���Request Index �е��ˣ�
IndexRequest indexRequest = new IndexRequest("index", "type", "1")
    .source(jsonBuilder()
    .startObject()
    .field("name", "Joe Smith")
    .field("gender", "male")
    .endObject());
UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
    .doc(jsonBuilder()
    .startObject()
    .field("gender", "female")
    .endObject())
    .upsert(indexRequest);
    client.update(updateRequest).get();
```
#### 4.1.6, ��ȡ���������ݣ�
```java
MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
    .add("twitter", "tweet", "1")           // ��ȡһ��
    .add("twitter", "tweet", "2", "3", "4")   // ��ȡ����
    .add("another", "type", "foo")          // ����������ֵ
    .get();

    for (MultiGetItemResponse itemResponse : multiGetItemResponses) { 
        GetResponse response = itemResponse.getResponse();
        if (response.isExists()) {                      
        String json = response.getSourceAsString(); 
    }
}
```
#### 4.1.7���������ɾ����һ������
```java
import static org.elasticsearch.common.xcontent.XContentFactory.*;
BulkRequestBuilder bulkRequest = client.prepareBulk();
// either use client#prepare, or use Requests 
//# to directly build index/delete requests
bulkRequest.add(client.prepareIndex("twitter", "tweet", "1")
    .setSource(jsonBuilder()
        .startObject()
        .field("user", "kimchy")
        .field("postDate", new Date())
        .field("message", "trying out Elasticsearch")
        .endObject()
    )
);

bulkRequest.add(client.prepareIndex("twitter", "tweet", "2")
    .setSource(jsonBuilder()
        .startObject()
        .field("user", "kimchy")
        .field("postDate", new Date())
        .field("message", "another post")
        .endObject()
    )
);

BulkResponse bulkResponse = bulkRequest.get();
if (bulkResponse.hasFailures()) {
    // process failures by iterating through each bulk response item
}
```

#### 4.1.8��ͨ��BulkProccessor����������������
```java
�����û��浽���ٺ��ύ���Լ�ʹ֮�೤ʱ��ˢ���ύ�����ö��ٸ�������ύ��
1�����ȴ���һ��BulkProccessor
// һ��ֻҪ�����������������ﵽ���ٺ�ˢ���ύ��
// ʱ�䲻���ã���������Ϊ1�� �������Ե���ʱ��50ms, 8 �Σ��ܹ�5.1 s
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

BulkProcessor bulkProcessor = BulkProcessor.builder(
    client,   // �ͻ���
    new BulkProcessor.Listener() {
        @Override
        public void beforeBulk(long executionId,
        BulkRequest request) { 
            request.numberOfActions();//��ȡ����ĸ���
        }

        @Override
        public void afterBulk(long executionId,
        BulkRequest request,BulkResponse response) { 
            response.hasFailures(); // ��ȡ���ٸ�������ʧ�ܵ�
        }

        @Override
        public void afterBulk(long executionId,
        BulkRequest request,Throwable failure) {  
            // ��ʧ�ܣ������쳣��ʱ��Ĳ�ŷ��
        }
    })
    .setBulkActions(10000)  // ��������
    .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))  // �����С 
    .setFlushInterval(TimeValue.timeValueSeconds(5))  // ������Σ�5s ��һ��ˢ��
    .setConcurrentRequests(1) // ֵΪ0 ��ʾָ���Ե�������ֵΪ1 ��ʾ������һ������������
    .setBackoffPolicy(
        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(50), 8))  
         // ������Դ�ٵ���������ʧ�ܣ��ظ����Σ���һ��100ms���ڶ���100*100
    .build();
2, ��������
    bulkProcessor.add(new IndexRequest("twitter", "tweet", "1")
    .source(/* your doc here */));
    bulkProcessor.add(new DeleteRequest("twitter", "tweet", "2"));

3���ر�bulkProcessor
    bulkProcessor.awaitClose(10, TimeUnit.MINUTES); // �ȴ�����������ɲ��˳�
    bulkProcessor.close();  // ֱ���˳������ȴ���������
4��������ڲ��������У�bulkProcessor �Ĳ�������Ӧ������Ϊ0
�������£�
    BulkProcessor bulkProcessor = BulkProcessor.builder(client, 
    new BulkProcessor.Listener() { /* Listener methods */ })
    .setBulkActions(10000)
    .setConcurrentRequests(0)
    .build();
    // Add your requests
    bulkProcessor.add(/* Your requests */);
    // Flush any remaining requests
    bulkProcessor.flush();
    // Or close the bulkProcessor if you don't need it anymore
    bulkProcessor.close();
    // Refresh your indices
    client.admin().indices().prepareRefresh().get();
    // Now you can start searching!
    client.prepareSearch().get();
```
## 5����ѯAPI
### 5.1��ʹ��QueryBuilder ��ѯ������
```java
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders.*;

SearchResponse response = client.prepareSearch("index1", "index2")
    .setTypes("type1", "type2")
    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)   
    // �����ڲ�ʵ�֣���Ӧ����API ��ָ��
    .setQuery(QueryBuilders.termQuery("multi", "test"))   // Query
    .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))// Filter
    .setFrom(0).setSize(60).setExplain(true)
    .get();
ȫ����ѯ��
// MatchAll on the whole cluster with all default options
    SearchResponse response = client.prepareSearch().get();
```
