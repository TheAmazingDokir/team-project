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

import entity.UserMatches;

import java.util.ArrayList;
import java.util.List;


public class MongoDBUserMatchesDataAccessObject {

    private static final String CONNECTION_STRING =
            "mongodb+srv://Eren:<db_password>@profiles.zbxygns.mongodb.net/?appName=profiles";
    private static final String DB_NAME = "Main_Database";
    private static final String COLLECTION_NAME = "Matches";

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> matchesCollection;

    public MongoDBUserMatchesDataAccessObject() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build();

        this.mongoClient = MongoClients.create(settings);
        this.mongoDatabase = mongoClient.getDatabase(DB_NAME);
        this.matchesCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        try {this.mongoDatabase.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
        } catch (MongoException e) {
            throw new RuntimeException("Failed to ping MongoDB Servers", e);
        }
    }


    public void uploadUserMatchesData(UserMatches userMatches) {
        Document userMatchesDocument = userMatchestoDocument(userMatches);

        try {
            matchesCollection.insertOne(userMatchesDocument);
        } catch (MongoException e) {
            throw new RuntimeException("Failed to insert user matches into MongoDB", e);
        }
    }


    public void changeUserMatchesData(UserMatches userMatches){
        Document userMatchesDocument = userMatchestoDocument(userMatches);

        try {
            UpdateResult result = matchesCollection.replaceOne
                    (Filters.eq("_id", userMatches.getUserId()), userMatchesDocument);
            if (result.getMatchedCount() == 0) {
                throw new RuntimeException("No existing user matches with id " + userMatches.getUserId());
            }
        } catch (MongoException e) {
            throw new RuntimeException("Failed to update user matches in MongoDB", e);
        }
    }


    public Document userMatchestoDocument(UserMatches userMatches){
        Document userMatchesDocument = new Document()
                .append("_id", userMatches.getUserId())
                .append("matchIds", userMatches.getMatches())
                .append("approvedIds", userMatches.getApproved())
                .append("rejectedIds", userMatches.getRejected());

        return userMatchesDocument;
    }


    public boolean checkUserMatchesExists(Integer userID) {
        try {
            Document matchesDocument = matchesCollection
                    .find(Filters.eq("_id", userID))
                    .first();
            return matchesDocument != null;
        } catch (MongoException e) {
            throw new RuntimeException("Failed to check user matches existence in MongoDB", e);
        }
    }

    public void deleteUserMatches(UserMatches userMatches) {
        try {
            DeleteResult result = matchesCollection.deleteOne(
                    Filters.eq("_id", userMatches.getUserId())
            );

            if (result.getDeletedCount() == 0) {
                throw new RuntimeException("No user matches found with id " + userMatches.getUserId());
            }
        } catch (MongoException e) {
            throw new RuntimeException("Failed to delete user matches from MongoDB", e);
        }
    }

    public UserMatches getUserMatchesbyId(Integer userId) {
        try {
            Document userDocument = matchesCollection
                    .find(Filters.eq("_id", userId))
                    .first();
            return documentToUserMatches(userDocument);
        } catch (MongoException e) {
            throw new RuntimeException("Failed to get user matches from MongoDB", e);
        }
    }

    private UserMatches documentToUserMatches(Document userDocument){
        int userId = userDocument.getInteger("_id");
        List<Integer> matchIds = userDocument.getList("matchIds", Integer.class);
        List<Integer> approvedIds = userDocument.getList("approvedIds", Integer.class);
        List<Integer> rejectedIds = userDocument.getList("rejectedIds", Integer.class);

        ArrayList<Integer> matchIdsArrayList = new ArrayList<>(matchIds);
        ArrayList<Integer> approvedIdsArrayList = new ArrayList<>(approvedIds);
        ArrayList<Integer> rejectedIdsArrayList = new ArrayList<>(rejectedIds);

        UserMatches userMatches = new UserMatches(userId);
        userMatches.setMatchIds(matchIdsArrayList);
        userMatches.setApprovedIds(approvedIdsArrayList);
        userMatches.setRejectedIds(rejectedIdsArrayList);

        return userMatches;
    }
}