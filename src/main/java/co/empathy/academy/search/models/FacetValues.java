package co.empathy.academy.search.models;

import co.elastic.clients.elasticsearch._types.FieldValue;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class FacetValues {
    String id;
    String value;
    long count;
    String filter;

    public FacetValues(String id, String value, long count, String filter) {
        this.id = id;
        this.value = value;
        this.count = count;
        this.filter = filter;
    }
}
