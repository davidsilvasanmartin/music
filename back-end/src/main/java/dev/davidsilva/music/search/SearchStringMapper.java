package dev.davidsilva.music.search;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchStringMapper {
    public List<SearchCriteria> toSearchCriteria(String searchString) {
        List<SearchCriteria> searchCriteria = new ArrayList<>();

        String[] stringCriteria = searchString.split(",");
        for (String stringCriterion : stringCriteria) {
            String[] searchParts = stringCriterion.split(":");
            if (searchParts.length != 3) {
                throw new InvalidSearchFormatException(searchString);
            }
            // TODO validation here
            searchCriteria.add(new SearchCriteria(searchParts[0], SearchOperation.fromString(searchParts[1]), searchParts[2]));
        }
        return searchCriteria;
    }
}
