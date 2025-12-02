package use_case.change_profile_info;

import entity.JobSeekerProfile;
import entity.EmployerProfile;
import entity.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Change Profile Interactor.
 */
public class ChangeProfileInteractor implements ChangeProfileInputBoundary {
    private final ChangeProfileDataAccessInterface changeProfileDataAccessObject;
    private final ChangeProfileRecommendationAccess changeProfileRecommendationAccess;
    private final ChangeProfileOutputBoundary changeProfileOutputBoundary;

    public ChangeProfileInteractor(ChangeProfileDataAccessInterface changeProfileDataAccessObject,
                                   ChangeProfileRecommendationAccess changeProfileRecommendationAccess,
                                   ChangeProfileOutputBoundary changeProfileOutputBoundary){
        this.changeProfileDataAccessObject = changeProfileDataAccessObject;
        this.changeProfileRecommendationAccess = changeProfileRecommendationAccess;
        this.changeProfileOutputBoundary = changeProfileOutputBoundary;
    }

    @Override
    public void execute(ChangeProfileInputData changeProfileInputData) {
        final String email = changeProfileInputData.getEmail();
        final String phoneNumber = changeProfileInputData.getPhoneNumber();
        final Integer profileID = changeProfileInputData.getProfileID();
        final String profileUsername = changeProfileInputData.getProfileUsername();
        final boolean isEmployer = changeProfileInputData.getIsEmployer();
        final String summary = changeProfileInputData.getSummary();
        final String profile = changeProfileInputData.getProfile();
        final String companyName = "Random Company";

        final boolean profileExists = changeProfileDataAccessObject.CheckProfileExists(profileID);

        UserProfile alteredProfile;
        if (!isEmployer) {
            JobSeekerProfile p = new JobSeekerProfile(profileID, email, phoneNumber, profileUsername);
            p.setResumeSummary(summary);
            p.setResumeFull(profile);
            alteredProfile = p;
        } else {
            EmployerProfile p = new EmployerProfile(profileID, email, phoneNumber, profileUsername);
            p.setJobApplicationSummary(summary);
            p.setJobApplicationFull(profile);
            p.setCompanyName(companyName);
            alteredProfile = p;
        }

        try {
            if (profileExists) {
                changeProfileDataAccessObject.ChangeProfileData(alteredProfile);
            } else {
                changeProfileDataAccessObject.UploadProfileData(alteredProfile);
            }
            changeProfileRecommendationAccess.upsertProfiles(new ArrayList<>(Arrays.asList(alteredProfile)));
            changeProfileOutputBoundary.prepareSuccessView(new ChangeProfileOutputData(profileID));
        } catch (RuntimeException e) {
            changeProfileOutputBoundary.prepareFailView("Unable to Update Profile");
        }
    }
}
