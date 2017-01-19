package io.github.tatagulov.eq.test;

import io.github.tatagulov.eq.metadata.exp.EQColumn;
import io.github.tatagulov.eq.metadata.exp.EQTable;
import io.github.tatagulov.test.testData.entity.db.public_.Person;

@EQTable(value = "person",clazz = Person.class)
public class AnnotationObject {

    @EQColumn("person_id")
    public Integer personId;
}
