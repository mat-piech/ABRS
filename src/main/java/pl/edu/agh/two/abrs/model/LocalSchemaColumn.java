package pl.edu.agh.two.abrs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "localSchemaColumn")
@Table(name = "localSchemaColumn")
public class LocalSchemaColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private ColumnType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localSchema")
    private LocalSchema localSchema;

    public LocalSchemaColumn() {
    }

    public LocalSchemaColumn(String name, ColumnType type, LocalSchema localSchema) {
        this.name = name;
        this.type = type;
        this.localSchema = localSchema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public LocalSchema getLocalSchema() {
        return localSchema;
    }

    public void setLocalSchema(LocalSchema localSchema) {
        this.localSchema = localSchema;
    }

    @Override
    public String toString() {
        return "LocalSchemaColumn{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", id=" + id +
                ", localSchema =" + localSchema.getName() +
                '}';
    }
}
