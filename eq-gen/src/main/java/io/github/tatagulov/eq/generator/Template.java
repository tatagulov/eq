package io.github.tatagulov.eq.generator;

import java.util.HashSet;
import java.util.Set;

public class Template {

    private final StringBuilder sb = new StringBuilder();
    protected final Set<String> classNames = new HashSet<String>();



    public void add(String format, Object ... args) {
        sb.append(String.format(format,args));
    }

    public String getText() {
        return sb.toString();
    }
    public String getClassName(Class clazz) {
        classNames.add(clazz.getName());
        return clazz.getSimpleName();
    }

    public String getImportSection() {
        StringBuilder sb = new StringBuilder();
        for (String className : classNames) {
            sb.append("import ").append(className).append(";\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
