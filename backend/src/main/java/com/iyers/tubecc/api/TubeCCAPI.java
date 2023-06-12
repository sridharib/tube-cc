package com.iyers.tubecc.api;

import com.iyers.tubecc.dto.SearchResultDTO;
import com.iyers.tubecc.service.TubeCCService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@CrossOrigin(origins = "http://localhost:4200")
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
