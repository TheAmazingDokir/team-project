package use_case.change_profile_info;

import entity.JobSeekerProfile;
import entity.EmployerProfile;

/**
 * The Change Profile Interactor.
 */
public class ChangeProfileInteractor implements ChangeProfileInputBoundry{
    private final ChangeProfileDataAccessInterface changeProfileDataAccessObject;
    private final ChangeProfileOutputBoundry changeProfileOutputBoundry;

    public ChangeProfileInteractor(ChangeProfileDataAccessInterface changeProfileDataAccessObject,
                                   ChangeProfileOutputBoundry changeProfileOutputBoundry){
        this.changeProfileDataAccessObject = changeProfileDataAccessObject;
        this.changeProfileOutputBoundry = changeProfileOutputBoundry;
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


        if (!changeProfileDataAccessObject.CheckProfileExists(profileID)){
            if (!isEmployer){
                JobSeekerProfile alteredProfile = new JobSeekerProfile(profileID,email,phoneNumber,profileUsername);
                alteredProfile.setResumeSummary(summary);
                alteredProfile.setResumeFull(profile);

                changeProfileDataAccessObject.UploadProfileData(alteredProfile);
                changeProfileOutputBoundry.prepareSuccessView(new ChangeProfileOutputData(profileID));
            }
            if (isEmployer){
                EmployerProfile alteredProfile = new EmployerProfile(profileID,email,phoneNumber,profileUsername);
                alteredProfile.setJobApplicationSummary(summary);
                alteredProfile.setJobApplicationFull(profile);
                alteredProfile.setCompanyName(companyName);

                changeProfileDataAccessObject.UploadProfileData(alteredProfile);
                changeProfileOutputBoundry.prepareSuccessView(new ChangeProfileOutputData(profileID));
            }
        }
        else {
            if (!isEmployer){
                JobSeekerProfile alteredProfile = new JobSeekerProfile(profileID,email,phoneNumber,profileUsername);
                alteredProfile.setResumeSummary(summary);
                alteredProfile.setResumeFull(profile);

                changeProfileDataAccessObject.ChangeProfileData(alteredProfile);
                changeProfileOutputBoundry.prepareSuccessView(new ChangeProfileOutputData(profileID));
            }
            if (isEmployer){
                EmployerProfile alteredProfile = new EmployerProfile(profileID,email,phoneNumber,profileUsername);
                alteredProfile.setJobApplicationSummary(summary);
                alteredProfile.setJobApplicationFull(profile);
                alteredProfile.setCompanyName(companyName);

                changeProfileDataAccessObject.ChangeProfileData(alteredProfile);
                changeProfileOutputBoundry.prepareSuccessView(new ChangeProfileOutputData(profileID));
            }

            else{
                changeProfileOutputBoundry.prepareFailView("Error: Database Update Unsuccessful");
            }
        }
    }
}
