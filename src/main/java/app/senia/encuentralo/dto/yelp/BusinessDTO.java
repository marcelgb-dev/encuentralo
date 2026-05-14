package app.senia.encuentralo.dto.yelp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
record BusinessDTO(
        String name,
        double rating,
        String url,
        @JsonProperty("display_phone")
        String displayPhone,
        LocationDTO location,
        CoordinatesDTO coordinates,
        List<CategoryDTO> categories
) {}
