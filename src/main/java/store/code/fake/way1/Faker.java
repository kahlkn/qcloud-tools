package store.code.fake.way1;

/**
 * Faker.
 * @author Kahle
 */
public interface Faker {

    int can(String express, Class<?> clazz);

    <T> T fake(String express, Class<T> clazz);

}
