package xyz.nickr.jitter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

/**
 * A utility class for managing HTTP requests on behalf of a Jitter object.
 *
 * @author Nick Robson
 */
public class JitterRequests {

    private final Jitter jitter;

    JitterRequests(Jitter jitter) {
        this.jitter = jitter;
    }

    /**
     * The providing {@link Jitter} object.
     *
     * @return The provider.
     */
    public Jitter jitter() {
        return jitter;
    }

    /**
     * Resolves a URL in comparison to the Jitter object's API URL.
     * <br><br>
     * If the URL starts with a '/':
     * <ul>
     *   <li>True: Prepend the API URL.</li>
     *   <li>False: Don't change the URL.</li>
     * </ul>
     *
     * @param url The URL.
     *
     * @return The resolved URL.
     */
    public String resolve(String url) {
        if (url.startsWith("/")) {
            url = jitter.api_url + url;
        }
        return url;
    }

    /**
     * Creates a GET request that already contains the required authentication headers.
     *
     * @param url The URL to GET.
     *
     * @return A Unirest GET request object.
     */
    public GetRequest get(String url) {
        return Unirest.get(resolve(url))
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + jitter.token);
    }

    /**
     * Creates a POST request that already contains the required authentication headers.
     *
     * @param url The URL to POST.
     *
     * @return A Unirest POST request object.
     */
    public HttpRequestWithBody post(String url) {
        return Unirest.post(resolve(url))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + jitter.token);
    }

    /**
     * Creates a PUT request that already contains the required authentication headers.
     *
     * @param url The URL to PUT.
     *
     * @return A Unirest PUT request object.
     */
    public HttpRequestWithBody put(String url) {
        return Unirest.put(resolve(url))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + jitter.token);
    }

    /**
     * Creates a DELETE request that already contains the required authentication headers.
     *
     * @param url The URL to DELETE.
     *
     * @return A Unirest DELETE request object.
     */
    public HttpRequestWithBody delete(String url) {
        return Unirest.delete(resolve(url))
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + jitter.token);
    }

    /**
     * Gets the given URL's response body.
     * Primarily used for the Gitter Streaming API.
     *
     * @param url The URL.
     *
     * @return The response body.
     *
     * @throws IOException If an error occurs.
     */
    public InputStream stream(String url) throws IOException {
        if (url.startsWith("/"))
            url = "https://stream.gitter.im/v1" + url;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.addRequestProperty("Accept", "application/json");
        conn.addRequestProperty("Authorization", "Bearer " + jitter.token);
        conn.setReadTimeout(0);
        return conn.getInputStream();
    }

}
