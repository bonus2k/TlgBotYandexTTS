package tlgBotTTS.speechkit;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;

/**
 * Request Web-запрос
 */
public class Request {
    private Task task;
    protected byte[] send(String url, String data, Map<String, String> headers, Task task) throws URISyntaxException, IOException, InterruptedException {
        this.task=task;

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest.Builder builder = HttpRequest.newBuilder();
        if (headers != null) {
            headers.forEach((k, v) -> builder.header(k, v));
        }

        HttpRequest request;


        if (task!=null && this.task.getURL().contains("recognize")) {
            request = builder.uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(task.getParam())))
                    .build();
        } else {
            request = builder.uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();
        }

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        return response.body();
    }

    protected byte[] send(String url, String data) throws URISyntaxException, IOException, InterruptedException {
        return send(url, data, null, null);
    }
}
