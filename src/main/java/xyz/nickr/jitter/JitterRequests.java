package xyz.nickr.jitter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class JitterRequests {

    private final Jitter jitter;

    JitterRequests(Jitter jitter) {
        this.jitter = jitter;
    }

    public Jitter jitter() {
        return jitter;
    }

    public String resolve(String url) {
        if (url.startsWith("/")) {
            url = jitter.api_url + url;
        }
        return url;
    }

    public GetRequest get(String url) {
        return Unirest.get(resolve(url))
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + jitter.token);
    }

    public HttpRequestWithBody post(String url) {
        return Unirest.post(resolve(url))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + jitter.token);
    }

    public HttpRequestWithBody put(String url) {
        return Unirest.put(resolve(url))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + jitter.token);
    }

    public HttpRequestWithBody delete(String url) {
        return Unirest.delete(resolve(url))
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + jitter.token);
    }

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
