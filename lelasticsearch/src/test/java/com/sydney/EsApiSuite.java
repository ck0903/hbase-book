package com.sydney;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class EsApiSuite {

    public  TransportClient client = null;
    @Before
    public void inintClientEnv() throws UnknownHostException {
        System.out.println("====================call before testing===============================");
        Settings settings = Settings.builder()
                .put("cluster.name", "my-cluster").build();
        client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("s200"), 9300));
    }
    @After
    public void closeClientOrOtherEnv(){
        System.out.println("====================call after testing===============================");
        client.close();
    }

    // IndexAPI
    @Test
    public void testIndexResponse() throws IOException {
//        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("s200"), 9300));
        IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(jsonBuilder()
                    .startObject()
                        .field("user" ,"sydney")
                        .field("postDate", new Date())
                        .field("message", "trying out ElasticSearch")
                    .endObject()).get();

        String _index = response.getIndex();
        String _type = response.getType();
        String _id = response.getId();
        long _version = response.getVersion();
        RestStatus status = response.status();
        System.out.println("_index: " + _index  + ", _type: " + _type + ", _id： " + _id + "_version: " + _version  + "status: " + status );
    }

    @Test
    public void testGetResponse() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name","my-cluster").build();
        GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
//        System.out.println("user： " + response.getField("user") + " ,message： "
//                + response.getField("message") + " , postDate: " + response.getField("postDate"));
        System.out.println(response.isExists());
        Map<String, Object> source = response.getSource();
        Set<String> set = source.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            System.out.println("key: " + key + ", value: " + source.get(key));
        }
    }

    @Test
    public void testDeleteResponse01() throws UnknownHostException {
        DeleteResponse response = client.prepareDelete("index", "type", "1").get();
//        IndicesExistsRequest existsRequest = new IndicesExistsRequest("")
        System.out.println(response.status());
    }

    @Test
    public  void testUpdate01() throws ExecutionException, InterruptedException, IOException {
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
    }

}
