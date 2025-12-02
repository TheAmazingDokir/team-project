package data_access;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import entity.*;
import org.bson.Document;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;

/**
 * The DAO for user data.
 */
public class MongoDBUserDataAccessObject implements SignupUserDataAccessInterface,
                                               LoginUserDataAccessInterface,
                                               ChangePasswordUserDataAccessInterface,
                                               LogoutUserDataAccessInterface {
    private static final String CONNECTION_STRING = getApiKey();
    private static final String DB_NAME = "Main_Database";
    private static final String COLLECTION_NAME = "Users";

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> userCollection;
    private final UserFactory userFactory;

    private static MongoDBUserDataAccessObject instance;

    private static String getApiKey(){
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("src/secrets/config.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props.getProperty("mongodb.api.key");
    }

    private MongoDBUserDataAccessObject(UserFactory userFactory) {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build();

        this.mongoClient = MongoClients.create(settings);
        this.mongoDatabase = mongoClient.getDatabase(DB_NAME);
        this.userCollection = mongoDatabase.getCollection(COLLECTION_NAME);

        try {this.mongoDatabase.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
        } catch (MongoException e) {
            throw new RuntimeException("Failed to ping MongoDB Servers", e);
        }

        this.userFactory = userFactory;
    }

    public static MongoDBUserDataAccessObject getInstance(UserFactory userFactory){
        if (instance == null){
            instance = new MongoDBUserDataAccessObject(userFactory);
        }
        return instance;
    }

    public User get(int userId) {
        // Make an API call to get the user object.
        try {
            Document userDocument = userCollection
                    .find(Filters.eq("_id", userId))
                    .first();
            return documentToUser(userDocument);
        } catch (MongoException e) {
            throw new RuntimeException("Failed to get user from MongoDB", e);
        }
    }

    @Override
    public int getNextUserId(){
        Document lastUserAdded = userCollection.find()
                .projection(include("_id"))
                .sort(descending("_id"))
                .limit(1)
                .first();

        if (lastUserAdded == null) {
            return 0;
        }
        return lastUserAdded.getInteger("_id") + 1;
    }

    @Override
    public int getUserIdByNameAndPassword(String name, String password) {
        try {
            Document userDocument = userCollection
                    .find(Filters.and(Filters.eq("name", name), Filters.eq("password", password)))
                    .first();
            if (userDocument == null) {
                return -1;
            }
            return userDocument.getInteger("_id");
        } catch (MongoException e) {
            throw new RuntimeException("Failed to check user existence in MongoDB", e);
        }
    }

    @Override
    public boolean existsByNameAndPassword(String name, String password) {
        return getUserIdByNameAndPassword(name, password) != -1;
    }

    @Override
    public void save(User user) {
        Document userDocument = userToDocument(user);

        try {
            userCollection.insertOne(userDocument);
        } catch (MongoException e) {
            throw new RuntimeException("Failed to insert user into MongoDB", e);
        }
    }

    @Override
    public void changePassword(User user) {
        changeUserData(user);
    }

    private void changeUserData(User user){
        Document profileDocument = userToDocument(user);

        try {
            UpdateResult result = userCollection.replaceOne
                    (Filters.eq("_id", user.getUserId()), profileDocument);
            if (result.getMatchedCount() == 0) {
                throw new RuntimeException("No existing user with id " + user.getUserId());
            }
        } catch (MongoException e) {
            throw new RuntimeException("Failed to update user in MongoDB", e);
        }
    }

    private Document userToDocument(User user){
        return new Document()
                .append("_id", user.getUserId())
                .append("name", user.getName())
                .append("password", user.getPassword());
    }

    private User documentToUser(Document userDocument){
        int userId = userDocument.getInteger("_id");
        String name = userDocument.getString("name");
        String password = userDocument.getString("password");
        return userFactory.create(userId, name, password);
    }
}
