package store.code.generator.id.way1;

/**
 * String identifier generator.
 * @author Kahle
 */
public interface StringIdentifierGenerator extends IdentifierGenerator {

    /**
     * Randomly generate the next string identifier.
     * @return Next string identifier
     */
    String nextStringIdentifier();

}
