package store.code.generator.id.way1;

/**
 * Identifier generator.
 * @author Kahle
 */
public interface IdentifierGenerator {

    /**
     * Randomly generate the next identifier.
     * @return Next identifier
     */
    Object nextIdentifier();

}
