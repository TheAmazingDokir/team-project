package use_case.update_matches;

import entity.UserProfile;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UpdateMatchesDataTest {

    @Test
    void testInputData_GettersAndSetters() {
        // Test Constructor 1
        UpdateMatchesInputData input1 = new UpdateMatchesInputData(10);
        assertEquals(10, input1.getCurrentId());

        // Test Constructor 2
        UpdateMatchesInputData input2 = new UpdateMatchesInputData(100, 200);
        assertEquals(100, input2.getUser1Id());
        assertEquals(200, input2.getUser2Id());

        // Test Constructor 3
        UpdateMatchesInputData input3 = new UpdateMatchesInputData("Alice");
        assertEquals("Alice", input3.getCurrentName());

        // Test Setters
        input1.setUser1Id(5);
        input1.setUser2Id(6);
        input1.setCurrentName("Bob");

        assertEquals(5, input1.getUser1Id());
        assertEquals(6, input1.getUser2Id());
        assertEquals("Bob", input1.getCurrentName());
    }

    @Test
    void testOutputData_Getters() {
        // Test Constructor 1 (List of profiles)
        ArrayList<UserProfile> profiles = new ArrayList<>();
        UpdateMatchesOutputData output1 = new UpdateMatchesOutputData(profiles);
        assertEquals(profiles, output1.getUsers());
        assertNull(output1.getCurrentName());

        // Test Constructor 2 (Name)
        UpdateMatchesOutputData output2 = new UpdateMatchesOutputData("Charlie");
        assertEquals("Charlie", output2.getCurrentName());
    }
}