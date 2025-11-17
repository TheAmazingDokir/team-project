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

        final String resumeSummary = changeProfileInputData.getResumeSummary();
        final String resumeFull = changeProfileInputData.getResumeFull();

        final String companyName = changeProfileInputData.getCompanyName();
        final String jobApplicationSummary = changeProfileInputData.getJobApplicationSummary();
        final String jobApplicationFull = changeProfileInputData.getJobApplicationFull();

        if (!changeProfileDataAccessObject.CheckProfileExists(profileID)){
            changeProfileOutputBoundry.prepareFailView("Profile does not exist");
        }
        else {
            if (changeProfileInputData.isJobSeeker()){
                JobSeekerProfile alteredProfile = new JobSeekerProfile(profileID,email,phoneNumber,profileUsername);
                alteredProfile.setResumeSummary(resumeSummary);
                alteredProfile.setResumeFull(resumeFull);

                changeProfileDataAccessObject.ChangeProfileData(alteredProfile);
            }
            if (changeProfileInputData.isEmployer()){
                EmployerProfile alteredProfile = new EmployerProfile(profileID,email,phoneNumber,profileUsername);
                alteredProfile.setJobApplicationSummary(jobApplicationSummary);
                alteredProfile.setJobApplicationFull(jobApplicationFull);
                alteredProfile.setCompanyName(companyName);

                changeProfileDataAccessObject.ChangeProfileData(alteredProfile);
            }
        }
    }
}
