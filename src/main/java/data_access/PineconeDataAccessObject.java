package data_access;
import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import io.pinecone.configs.PineconeConfig;
import io.pinecone.configs.PineconeConnection;
import io.pinecone.proto.DescribeIndexStatsResponse;
import io.pinecone.proto.ListResponse;
import io.pinecone.proto.QueryResponse;
import io.pinecone.proto.Vector;
import org.openapitools.db_control.client.model.*;
import org.openapitools.db_data.client.ApiException;
import org.openapitools.db_data.client.model.SearchRecordsResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PineconeDataAccessObject {
    private final Index index;

    public PineconeDataAccessObject() {
        String apiKey = getApiKey();
        Pinecone pinecone = new Pinecone.Builder(apiKey).build();
        this.index = pinecone.getIndexConnection("hirematch");
    }

    private String getApiKey(){
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props.getProperty("pinecone.api.key");
    }

    public void upsertRecords(ArrayList<Map<String, String>> records, String namespace) throws org.openapitools.db_data.client.ApiException {
        index.upsertRecords(namespace, records);
    }

    public void deleteRecordsByIds(String namespace, List<String> ids) throws org.openapitools.db_data.client.ApiException {
        index.deleteByIds(ids, namespace);
    }

    public SearchRecordsResponse searchBySimilarity(String query, String namespace) throws ApiException {
        SearchRecordsResponse recordsResponse = index.searchRecordsByText(query, namespace, null, 10, null, null);
        return recordsResponse;
    }


    public void logIndexStats(){
        DescribeIndexStatsResponse indexStatsResponse = index.describeIndexStats();
        System.out.println(indexStatsResponse);
    }
}
