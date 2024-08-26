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

            String searchOperationString = searchParts[1];
            SearchOperation searchOperation = SearchOperation.fromString(searchOperationString);
            if (searchOperation == null) {
                throw new InvalidSearchOperationException(searchOperationString);
            }

            searchCriteria.add(new SearchCriteria(searchParts[0], searchOperation, searchParts[2]));
        }
        return searchCriteria;
    }
}
