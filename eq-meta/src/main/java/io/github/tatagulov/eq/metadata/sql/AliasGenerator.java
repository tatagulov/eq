package io.github.tatagulov.eq.metadata.sql;

public class AliasGenerator {
    private final String prefix;
    private int index = 0;

    public AliasGenerator(String prefix) {
        this.prefix = prefix;
    }

    public AliasGenerator() {
        this.prefix = "t";
    }

    public String generate() {
        return prefix + String.valueOf(index++);
    }

}
