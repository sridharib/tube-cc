package com.siyer.tubecc.service;

import com.siyer.tubecc.dto.SearchResultDTO;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

public interface TubeCCService {
    Flux<SearchResultDTO> search(String query);

    Flux<DataBuffer> load(SearchResultDTO searchResultDTO);
}
