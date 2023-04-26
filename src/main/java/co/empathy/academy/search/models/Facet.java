package co.empathy.academy.search.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class Facet {
    String facet;
    String type;
    List<FacetValues> values;
}
