package com.iyers.tubecc.service;

import com.iyers.tubecc.dto.SearchResultDTO;
import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
public class TubeCCService {

    private String openSubtitleURL;

    public TubeCCService(@Value("${opensubtitle.search-url}") String openSubtitleURL) {
        this.openSubtitleURL = openSubtitleURL;
    }

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

    public Flux<DataBuffer> load(SearchResultDTO searchResultDTO) {
        WebClient client = WebClient.builder()
                .baseUrl(searchResultDTO.getSubDownloadLink().replaceAll(".gz", ".srt"))
                .build();
        return client.get().retrieve().bodyToFlux(DataBuffer.class);
    }

}
