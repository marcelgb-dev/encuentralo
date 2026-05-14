package app.senia.encuentralo.dto.yelp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
record LocationDTO(
        @JsonProperty("display_address")
        List<String> displayAddress
) {}