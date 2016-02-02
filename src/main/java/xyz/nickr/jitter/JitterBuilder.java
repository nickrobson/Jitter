package xyz.nickr.jitter;

/**
 * A builder object for {@link Jitter}.
 *
 * @author Nick Robson
 */
public class JitterBuilder {

    /**
     * The default API URL. You should only need to change this if the API URL changes.
     */
    public static final String DEFAULT_API_URL = "https://api.gitter.im/v1";

    private String token;
    private String api_url = DEFAULT_API_URL;

    /**
     * Builds a {@link Jitter} object.
     *
     * @return The object.
     */
    public Jitter build() {
        return new Jitter(token, api_url);
    }

    /**
     * Sets the Gitter API token.
     *
     * @param token The token.
     *
     * @return This builder.
     */
    public JitterBuilder token(String token) {
        this.token = token;
        return this;
    }

    /**
     * Sets the Gitter API URL.
     * <em>Not normally required, default is</em> {@value #DEFAULT_API_URL}
     *
     * @param api_url The API URL.
     *
     * @return This builder.
     */
    public JitterBuilder api(String api_url) {
        this.api_url = api_url;
        return this;
    }

}
