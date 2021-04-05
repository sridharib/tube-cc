package com.siyer.tubecc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchResultDTO {

    @JsonProperty("SubFileName")
    private String subFileName;

    @JsonProperty("SubDownloadLink")
    private String subDownloadLink;

    @JsonProperty("SubFormat")
    private String subFormat;

    @JsonProperty("LanguageName")
    private String languageName;

}
