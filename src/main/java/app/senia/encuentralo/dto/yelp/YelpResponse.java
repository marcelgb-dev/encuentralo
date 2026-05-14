package app.senia.encuentralo.dto.yelp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YelpResponse(
        List<BusinessDTO> businesses
) {}