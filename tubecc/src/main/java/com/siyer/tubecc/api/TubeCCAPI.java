package com.siyer.tubecc.api;

import com.siyer.tubecc.dto.SearchResultDTO;
import com.siyer.tubecc.service.TubeCCService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
public class TubeCCAPI {

    private TubeCCService service;

    public TubeCCAPI(TubeCCService service) {
        this.service = service;
    }

    @GetMapping("${api.path}/search")
    public Flux<SearchResultDTO> search(@RequestParam String query) {
        return service.search(query);
    }

    @PostMapping("${api.path}/loadSubtitle")
    public Flux<DataBuffer> loadSubtitle(@RequestBody SearchResultDTO searchResultDTO) {
        return service.load(searchResultDTO);
    }

}
