package com.siyer.tubecc.service;

import com.siyer.tubecc.dto.SearchResultDTO;
import reactor.core.publisher.Flux;

public interface TubeCCService {
    Flux<SearchResultDTO> search(String query);
}
