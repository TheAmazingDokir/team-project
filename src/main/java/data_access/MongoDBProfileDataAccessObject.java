package data_access;

import com.mongodb.client.MongoCollection;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.result.DeleteResult;

import use_case.change_profile_info.ChangeProfileDataAccessInterface;

import entity.EmployerProfile;
import entity.JobSeekerProfile;
import entity.UserProfile;
import use_case.login.LoginProfileDataAccessInterface;
import use_case.recommend_profile.RecommendProfileProfileDataAccessInterface;
import use_case.signup.SignupProfileDataAccessInterface;

import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;


public class MongoDBProfileDataAccessObject implements ChangeProfileDataAccessInterface,
        LoginProfileDataAccessInterface,
        RecommendProfileProfileDataAccessInterface,
        SignupProfileDataAccessInterface {

    private static final String CONNECTION_STRING =
            "mongodb+srv://TESTER:testpassword31@profiles.zbxygns.mongodb.net/?retryWrites=true&w=majority&appName=profiles";
    private static final String DB_NAME = "Main_Database";
    private static final String COLLECTION_NAME = "Profiles";

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> profilesCollection;

    public MongoDBProfileDataAccessObject() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build();

        this.mongoClient = MongoClients.create(settings);
        this.mongoDatabase = mongoClient.getDatabase(DB_NAME);
        this.profilesCollection = mongoDatabase.getCollection(COLLECTION_NAME);

            try {this.mongoDatabase.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                throw new RuntimeException("Failed to ping MongoDB Servers", e);
        }
    }

    @Override
    public UserProfile getProfileById(int profileId){
        Document profileDocument = profilesCollection
                .find(Filters.eq("_id", profileId))
                .first();
        if (profileDocument == null){
            return null;
        }

        return documentToUserProfile(profileDocument);
    }

    @Override
    public int getNextProfileId(){
        Document lastProfileAdded = profilesCollection.find()
                .projection(include("_id"))
                .sort(descending("_id"))
                .limit(1)
                .first();

        if (lastProfileAdded == null) {
            return 0;
        }
        return lastProfileAdded.getInteger("_id") + 1;
    }

    @Override
    public void uploadProfileData(UserProfile userProfile) {
        Document profileDocument = userProfiletoDocument(userProfile);

        try {
            profilesCollection.insertOne(profileDocument);
        } catch (MongoException e) {
            throw new RuntimeException("Failed to insert profile into MongoDB", e);
        }
    }

    @Override
    public void changeProfileData(UserProfile userProfile){
        Document profileDocument = userProfiletoDocument(userProfile);

        try {
            UpdateResult result = profilesCollection.replaceOne
                    (Filters.eq("_id", userProfile.getUserId()), profileDocument);
            if (result.getMatchedCount() == 0) {
                throw new RuntimeException("No existing profile with id " + userProfile.getUserId());
            }
        } catch (MongoException e) {
            throw new RuntimeException("Failed to update profile in MongoDB", e);
        }
    }

    public Document userProfiletoDocument(UserProfile userProfile){
        Document profileDocument = new Document()
                .append("_id", userProfile.getUserId())
                .append("email", userProfile.getEmail())
                .append("phoneNumber", userProfile.getPhoneNumber())
                .append("profileUsername", userProfile.getProfileUsername());

        if (userProfile instanceof EmployerProfile) {
            EmployerProfile employer = (EmployerProfile)userProfile;
            profileDocument.append("type", "EmployerProfile");
            profileDocument.append("company", employer.getCompanyName());
            profileDocument.append("applicationSummary", employer.getSummaryProfileAsString());
            profileDocument.append("applicationFull", employer.getFullProfileAsString());
        } else if (userProfile instanceof JobSeekerProfile) {
            JobSeekerProfile jobSeeker = (JobSeekerProfile)userProfile;
            profileDocument.append("type", "JobSeekerProfile");
            profileDocument.append("resumeSummary", jobSeeker.getSummaryProfileAsString());
            profileDocument.append("resumeFull", jobSeeker.getFullProfileAsString());
        }
        return profileDocument;
    }

    public UserProfile documentToUserProfile(Document profileDocument){
        int userId = profileDocument.getInteger("_id");
        String email = profileDocument.getString("email");
        String phoneNumber = profileDocument.getString("phoneNumber");
        String profileUsername = profileDocument.getString("profileUsername");

        String type = profileDocument.getString("type");
        if (type.equals("EmployerProfile")) {
            String companyName = profileDocument.getString("companyName");
            String applicationSummary = profileDocument.getString("applicationSummary");
            String applicationFull = profileDocument.getString("applicationFull");

            EmployerProfile userProfile = new EmployerProfile(userId, email, phoneNumber, profileUsername);
            userProfile.setCompanyName(companyName);
            userProfile.setJobApplicationSummary(applicationSummary);
            userProfile.setJobApplicationFull(applicationFull);

            return userProfile;
        } else if (type.equals("JobSeekerProfile")) {
            String resumeSummary = profileDocument.getString("resumeSummary");
            String resumeFull = profileDocument.getString("resumeFull");

            JobSeekerProfile userProfile = new JobSeekerProfile(userId, email, phoneNumber, profileUsername);
            userProfile.setResumeSummary(resumeSummary);
            userProfile.setResumeFull(resumeFull);

            return userProfile;
        }
        return null;
    }

    @Override
    public boolean checkProfileExists(Integer userID) {
        try {
            Document profileDocument = profilesCollection
                    .find(Filters.eq("_id", userID))
                    .first();
            return profileDocument != null;
        } catch (MongoException e) {
            throw new RuntimeException("Failed to check profile existence in MongoDB", e);
        }
    }

    public void deleteProfile(UserProfile userProfile) {
        try {
            DeleteResult result = profilesCollection.deleteOne(
                    Filters.eq("_id", userProfile.getUserId())
            );

            if (result.getDeletedCount() == 0) {
                throw new RuntimeException("No profile found with id " + userProfile.getUserId());
            }
        } catch (MongoException e) {
            throw new RuntimeException("Failed to delete profile from MongoDB", e);
        }
    }
}
