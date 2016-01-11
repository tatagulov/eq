package io.github.tatagulov.test.testData.entity;

import io.github.tatagulov.eq.metadata.api.CompositeColumnReference;
import io.github.tatagulov.test.testData.entity.db.Public;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.DataBase;
import io.github.tatagulov.eq.metadata.api.ColumnReference;

public class Db extends DataBase {
	public static final Db db = new Db();
	public final Public public_ = new Public(this);
	private Db() {
		super("db");
		references.add(new ColumnReference(public_.personType.person_type_id,public_.person.person_type_id,true));
		references.add(new ColumnReference(public_.dep.dep_id,public_.personMove.from_dep_id,true));
		references.add(new ColumnReference(public_.dep.dep_id,public_.personMove.to_dep_id,true));
		references.add(new ColumnReference(public_.person.person_id,public_.personMove.person_id,true));
	}
}
