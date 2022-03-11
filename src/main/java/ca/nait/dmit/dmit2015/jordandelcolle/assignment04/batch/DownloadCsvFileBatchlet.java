package ca.nait.dmit.dmit2015.jordandelcolle.assignment04.batch;

import jakarta.batch.api.AbstractBatchlet;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.runtime.context.JobContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author Jordan Del Colle
 * @version 2022.03.11
 *
 */

@Named("downloadManager")
public class DownloadCsvFileBatchlet extends AbstractBatchlet {

    @Inject
    private JobContext _jobContext;

    @Inject
    @BatchProperty(name = "file_download_path")
    private String _fileDownloadPath;

    @Inject
    @BatchProperty(name = "csv_file_uri")
    private String _csvFileUri;

    @Override
    public String process() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(_csvFileUri))
                .build();
        Path downloadPath = Path.of("/home/user2015/Downloads");
        HttpResponse<Path> response = client.send(request,
                HttpResponse.BodyHandlers.ofFileDownload(downloadPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        System.out.println("Status code: " + response.statusCode());
        System.out.println("\n Body: " + response.body());
        return null;
    }
}
