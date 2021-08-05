package store.code.message.way1;

/**
 * The callback interface for asynchronous invokes.
 * @author Kahle
 */
public interface AsyncCallback<T> {

    /**
     * Called when the asynchronous invoke succeeds.
     * @param result The result returned by the asynchronous invoke
     */
    void onSuccess(T result);

    /**
     * Called when the asynchronous invoke fails.
     * @param th An exception that occurs in an asynchronous invoke
     */
    void onFailure(Throwable th);

}
