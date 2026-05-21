package app.senia.encuentralo.dto.yelp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BusinessDTO(
        String name,
        double rating,
        @JsonProperty("review_count")
        int reviewCount,
        String url,
        @JsonProperty("display_phone")
        String displayPhone,
        LocationDTO location,
        CoordinatesDTO coordinates,
        double distance,
        List<CategoryDTO> categories
) {
        public List<String> getCategoryTitles() {
                return categories.stream()
                        .map(CategoryDTO::title)
                        .toList();
        }
}

