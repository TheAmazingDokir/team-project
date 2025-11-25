package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(int userId, String name, String password) {
        return new User(userId, name, password, null);
    }
}
