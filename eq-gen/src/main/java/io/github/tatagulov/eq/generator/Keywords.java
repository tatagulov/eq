package io.github.tatagulov.eq.generator;

import java.util.LinkedList;
import java.util.List;

public class Keywords {

    public static final List<String> keywords = new LinkedList<String>();

    static {
        keywords.add("abstract");
        keywords.add("continue");
        keywords.add("for");
        keywords.add("new");
        keywords.add("switch");
        keywords.add("assert");
        keywords.add("default");
        keywords.add("goto");
        keywords.add("package");
        keywords.add("synchronized");
        keywords.add("boolean");
        keywords.add("do");
        keywords.add("if");
        keywords.add("private");
        keywords.add("this");
        keywords.add("break");
        keywords.add("double");
        keywords.add("implements");
        keywords.add("protected");
        keywords.add("throw");
        keywords.add("byte");
        keywords.add("else");
        keywords.add("import");
        keywords.add("public");
        keywords.add("throws");
        keywords.add("case");
        keywords.add("enum");
        keywords.add("instanceof");
        keywords.add("return");
        keywords.add("transient");
        keywords.add("catch");
        keywords.add("extends");
        keywords.add("int");
        keywords.add("short");
        keywords.add("try");
        keywords.add("char");
        keywords.add("final");
        keywords.add("interface");
        keywords.add("static");
        keywords.add("void");
        keywords.add("class");
        keywords.add("finally");
        keywords.add("long");
        keywords.add("strictfp");
        keywords.add("volatile");
        keywords.add("const");
        keywords.add("float");
        keywords.add("native");
        keywords.add("super");
        keywords.add("while");
        keywords.add("const");
        keywords.add("goto");
        keywords.add("true");
        keywords.add("false");
        keywords.add("null");
    }

    public static String getCheckedName(String name) {
        return keywords.contains(name) ? name+"_" : name;
    }
}
