package com.siyer.tubecc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class SearchResultDTO {

    @JsonAlias({"SubFileName"})
    private String subFileName;

    @JsonAlias({"SubDownloadLink"})
    private String subDownloadLink;

    @JsonAlias({"SubFormat"})
    private String subFormat;

    @JsonAlias({"LanguageName"})
    private String languageName;

}
