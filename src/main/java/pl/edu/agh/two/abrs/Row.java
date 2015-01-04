package pl.edu.agh.two.abrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Row {

    private final List<RowItem> fields;

    public Row(List<RowItem> fields) {
        this.fields = Collections.unmodifiableList(new ArrayList<>(fields));
    }

    public RowItem get(int index) {
        return fields.get(index);
    }

    public int length() {
        return fields.size();
    }

    public List<RowItem> getFields() {
        return fields;
    }
}
