package pl.edu.agh.two.abrs.model.global;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
public class GlobalSchemaTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GlobalSchemaColumn> columns;

    @OneToMany
    private LinkedList<GlobalSchemaRecord> records = new LinkedList<>();

    private String name;

    public GlobalSchemaTable() {
    }

    public GlobalSchemaTable(String name, List<GlobalSchemaColumn> columns) {
        if (columns == null || name == null) {
            throw new IllegalArgumentException();
        }
        this.columns = columns;
        this.name = name;
    }

    public List<GlobalSchemaColumn> getColumns() {
        return new ArrayList<>(columns);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<GlobalSchemaRecord> getRecords() {
        return new ArrayList<GlobalSchemaRecord>(records);
    }

    public void addRecord(GlobalSchemaRecord record){
        if(record==null){
            throw new IllegalArgumentException();
        }
        records.add(record);
    }
}
