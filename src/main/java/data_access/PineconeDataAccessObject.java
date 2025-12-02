package data_access;
import entity.EmployerProfile;
import entity.JobSeekerProfile;
import entity.UserProfile;
import entity.ProfileRecommendation;
import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import io.pinecone.proto.DescribeIndexStatsResponse;
import org.openapitools.db_data.client.model.Hit;
import org.openapitools.db_data.client.model.SearchRecordsResponse;
import use_case.change_profile_info.ChangeProfileRecommendationAccess;
import use_case.recommend_profile.RecommendProfileRecommendationDataAccessInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PineconeDataAccessObject implements RecommendProfileRecommendationDataAccessInterface,
        ChangeProfileRecommendationAccess {
    private static final String NAMESPACE = "main";
    private final Index index;

    private static PineconeDataAccessObject instance;

    private PineconeDataAccessObject() {
        String apiKey = getApiKey();
        Pinecone pinecone = new Pinecone.Builder(apiKey).build();
        this.index = pinecone.getIndexConnection("hirematch");
    }

    public static PineconeDataAccessObject getInstance(){
        if (instance == null){
            instance = new PineconeDataAccessObject();
        }
        return instance;
    }

    private static String getApiKey(){
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("src/secrets/config.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props.getProperty("pinecone.api.key");
    }

    public void upsertProfiles(List<UserProfile> profiles) {
        ArrayList<Map<String, String>> records = new ArrayList<>();
        for  (UserProfile profile : profiles) {
            HashMap<String, String> record = new HashMap<>();
            record.put("_id", String.valueOf(profile.getUserId()));
            record.put("text", profile.getSummaryProfileAsString());

            boolean isEmployer = profile instanceof EmployerProfile;
            if (isEmployer) {
                record.put("role", "employer");
            }
            boolean isJobSeeker = profile instanceof JobSeekerProfile;
            if (isJobSeeker) {
                record.put("role", "jobseeker");
            }
            records.add(record);
        }

        try{
            index.upsertRecords(NAMESPACE, records);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void deleteProfilesByIds( List<String> ids) throws org.openapitools.db_data.client.ApiException {
        index.deleteByIds(ids, NAMESPACE);
    }

    public ProfileRecommendation searchProfilesBySimilarity(String query, String role, int numberOfRecords) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("role", role);
        try{
            SearchRecordsResponse recordsResponse = index.searchRecordsByText(query, NAMESPACE, null, numberOfRecords, filter, null);
            List<Hit> records = recordsResponse.getResult().getHits();

            List<Integer> recommendedProfileIds = new ArrayList<>();
            for  (Hit record : records) {
                int profileId = Integer.parseInt(record.getId());
                recommendedProfileIds.add(profileId);
            }

            return new ProfileRecommendation(recommendedProfileIds);
        }catch(Exception e){
            return null;
        }
    }


    public void logIndexStats(){
        DescribeIndexStatsResponse indexStatsResponse = index.describeIndexStats();
        System.out.println(indexStatsResponse);
    }
}
