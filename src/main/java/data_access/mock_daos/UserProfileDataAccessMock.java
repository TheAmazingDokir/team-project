package data_access.mock_daos;

import entity.EmployerProfile;
import entity.JobSeekerProfile;
import entity.UserProfile;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.recommend_profile.RecommendProfileProfileDataAccessInterface;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserProfileDataAccessMock implements RecommendProfileProfileDataAccessInterface {
    private static final String JOB_SEEKER_PROFILES_FILE = "src/main/java/miscilaneous/mock_data/MockJobSeekerProfiles.json";
    private static final String EMPLOYER_PROFILES_FILE = "src/main/java/miscilaneous/mock_data/MockEmployerProfiles.json";

    private Map<Integer, UserProfile> userProfiles;

    public UserProfileDataAccessMock(){
        userProfiles = new HashMap<>();
        loadProfiles();
    }

    public UserProfile getProfileById(int profileId){
        return userProfiles.get(profileId);
    }

    public List<UserProfile> getAllUserProfiles(){
        return userProfiles.values().stream().collect(Collectors.toList());
    }

    private void loadProfiles() {
        try {
            // ---- Load Job Seekers ----
            String seekersJson = Files.readString(Paths.get(JOB_SEEKER_PROFILES_FILE));
            JSONArray seekersArray = new JSONArray(seekersJson);

            for (int i = 0; i < seekersArray.length(); i++) {
                JSONObject js = seekersArray.getJSONObject(i);

                int userID = js.getInt("userId");
                String email  = js.getString("email");
                String phoneNumber = js.getString("phoneNumber");
                String profileUsername = js.getString("profileUsername");

                JobSeekerProfile seeker = new JobSeekerProfile(userID, email, phoneNumber, profileUsername);

                seeker.setResumeSummary(js.getString("resumeSummary"));
                seeker.setResumeFull(js.getString("resumeFull"));

                userProfiles.put(seeker.getUserId(), seeker);
            }

            // ---- Load Employers ----
            String employersJson = Files.readString(Paths.get(EMPLOYER_PROFILES_FILE));
            JSONArray employersArray = new JSONArray(employersJson);

            for (int i = 0; i < employersArray.length(); i++) {
                JSONObject emp = employersArray.getJSONObject(i);

                int userID = emp.getInt("userId");
                String email  = emp.getString("email");
                String phoneNumber = emp.getString("phoneNumber");
                String profileUsername = emp.getString("profileUsername");

                EmployerProfile employer = new EmployerProfile(userID, email, phoneNumber, profileUsername);

                employer.setCompanyName(emp.getString("companyName"));
                employer.setJobApplicationSummary(emp.getString("jobApplicationSummary"));
                employer.setJobApplicationFull(emp.getString("jobApplicationFull"));



                userProfiles.put(employer.getUserId(), employer);
            }

            System.out.println("Loaded " + userProfiles.size() + " user profiles.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
