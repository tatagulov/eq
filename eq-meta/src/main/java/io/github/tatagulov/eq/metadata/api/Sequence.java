package io.github.tatagulov.eq.metadata.api;

public class Sequence {
    public final Schema schema;
    public final String sequenceName;

    public Sequence(Schema schema, String sequenceName) {
        this.schema = schema;
        this.sequenceName = sequenceName;
        this.schema.sequences.add(this);
    }

    public String getFullName() {
        return (schema.schemaName==null ? "" : schema.schemaName+ ".") + "." + sequenceName;
    }
}
