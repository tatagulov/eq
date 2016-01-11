package io.github.tatagulov.test.testData.entity.db.public_;

import static io.github.tatagulov.test.testData.entity.Db.*;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.SQLTable;
import java.lang.Short;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.sql.JoinType;
import java.lang.String;

public class PersonType extends SQLTable<PersonType> {
	public PersonType() { super(db.public_.personType); }

	public class PersonTypeIdColumn extends SQLColumn<PersonType,Short> {
		public PersonTypeIdColumn(PersonType table, Column<Short, ?> column) { super(table, column); }
		private Person innerPersonTable;
		public Person innerPerson(){
			if (innerPersonTable == null) {
				innerPersonTable = new Person();
				join(JoinType.inner,this, innerPersonTable.person_type_id);
			}
			return innerPersonTable;
		}
		private Person leftPersonTable;
		public Person leftPerson(){
			if (leftPersonTable == null) {
				leftPersonTable = new Person();
				join(JoinType.left,this, leftPersonTable.person_type_id);
			}
			return leftPersonTable;
		}
	}

	public final PersonTypeIdColumn person_type_id = new PersonTypeIdColumn(this, db.public_.personType.person_type_id);
	public final SQLColumn<PersonType,String> person_type_name = new SQLColumn<PersonType,String>(this, db.public_.personType.person_type_name);
}
