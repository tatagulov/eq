package io.github.tatagulov.test.testData.entity.db;

import io.github.tatagulov.eq.metadata.api.Sequence;
import io.github.tatagulov.test.testData.entity.db.public_.MDPerson;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.DataBase;
import io.github.tatagulov.test.testData.entity.db.public_.MDDep;
import io.github.tatagulov.test.testData.entity.db.public_.MDPersonMove;
import io.github.tatagulov.test.testData.entity.db.public_.MDPersonType;

public class Public extends Schema {
	public Public(DataBase dataBase) { super(dataBase, "public");}
	public final MDDep dep = new MDDep(this);
	public final MDPerson person = new MDPerson(this);
	public final MDPersonMove personMove = new MDPersonMove(this);
	public final MDPersonType personType = new MDPersonType(this);

}
