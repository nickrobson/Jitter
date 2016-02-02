package xyz.nickr.jitter;

public class JitterBuilder {

    private String token;
    private String api_url = "https://api.gitter.im/v1";

    public Jitter build() {
        return new Jitter(token, api_url);
    }

    public JitterBuilder token(String token) {
        this.token = token;
        return this;
    }

    public JitterBuilder api(String api_url) {
        this.api_url = api_url;
        return this;
    }

}
