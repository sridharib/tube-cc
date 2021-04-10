package com.siyer.tubecc.service;

import com.siyer.tubecc.dto.SearchResultDTO;
import io.netty.handler.logging.LogLevel;
import lombok.SneakyThrows;
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

    @Override
    @SneakyThrows
    public Flux<DataBuffer> load(SearchResultDTO searchResultDTO) {

        WebClient client = WebClient.builder()
                .baseUrl(searchResultDTO.getSubDownloadLink().replaceAll(".gz", ".srt"))
                .build();
        return client.get().retrieve().bodyToFlux(DataBuffer.class);
    }

}
