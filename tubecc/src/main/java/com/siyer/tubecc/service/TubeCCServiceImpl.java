package com.siyer.tubecc.service;

import com.siyer.tubecc.dto.SearchResultDTO;
import io.netty.handler.logging.LogLevel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;

@Service
public class TubeCCServiceImpl implements TubeCCService {

    private String openSubtitleURL;

    public TubeCCServiceImpl(@Value("${opensubtitle.search-url}") String openSubtitleURL) {
        this.openSubtitleURL = openSubtitleURL;
    }

    @Override
    public Flux<SearchResultDTO> search(String query) {

        HttpClient httpClient = HttpClient.create()
                .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        WebClient client = WebClient.builder()
                .baseUrl(openSubtitleURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configure -> configure.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
                .build();
        return client.get()
                .uri("/query-" + query)
                .header("X-User-Agent", "TemporaryUserAgent")
                .retrieve()
                .bodyToFlux(SearchResultDTO.class);
    }

    public void load(SearchResultDTO searchResultDTO) throws IOException {

        Path source = Files.createTempFile("temp", ".gz");
        Path target = Files.createTempFile("temp", ".srt");
        WebClient client = WebClient.builder()
                .baseUrl(searchResultDTO.getSubDownloadLink())
                .build();
        client.get().retrieve().bodyToFlux(DataBuffer.class).subscribe(dataBuffer -> {
            DataBufferUtils.write(Flux.just(dataBuffer), source, StandardOpenOption.CREATE);
            decompressGzip(source, target);
        });
    }

    @SneakyThrows
    private void decompressGzip(Path source, Path target) {

        try (GZIPInputStream gis = new GZIPInputStream(
                new FileInputStream(source.toFile()));
             FileOutputStream fos = new FileOutputStream(target.toFile())) {
            // copy GZIPInputStream to FileOutputStream
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }

}
