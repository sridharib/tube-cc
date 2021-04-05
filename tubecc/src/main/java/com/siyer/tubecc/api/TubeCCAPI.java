package com.siyer.tubecc.api;

import com.siyer.tubecc.dto.SearchResultDTO;
import com.siyer.tubecc.service.TubeCCService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping("${api.path}/load")
    public Mono<String> load(@RequestBody SearchResultDTO searchResultDTO) {
        return Mono.just("");
    }

}
