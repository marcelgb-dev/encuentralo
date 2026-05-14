package app.senia.encuentralo.dto.yelp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
record CategoryDTO(
        String title
) {}