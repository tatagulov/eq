package io.github.tatagulov.test.testData.entity.db.public_;

import static io.github.tatagulov.test.testData.entity.Db.*;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.SQLTable;
import io.github.tatagulov.eq.metadata.api.Column;
import java.lang.Short;
import io.github.tatagulov.eq.metadata.sql.JoinType;
import java.lang.String;
import java.lang.Integer;
import java.sql.Date;

public class Person extends SQLTable<Person> {
	public Person() { super(db.public_.person); }

	public class PersonIdColumn extends SQLColumn<Person,Integer> {
		public PersonIdColumn(Person table, Column<Integer, ?> column) { super(table, column); }
		private PersonMove innerPersonMoveTable;
		public PersonMove innerPersonMove(){
			if (innerPersonMoveTable == null) {
				innerPersonMoveTable = new PersonMove();
				join(JoinType.inner,this, innerPersonMoveTable.person_id);
			}
			return innerPersonMoveTable;
		}
		private PersonMove leftPersonMoveTable;
		public PersonMove leftPersonMove(){
			if (leftPersonMoveTable == null) {
				leftPersonMoveTable = new PersonMove();
				join(JoinType.left,this, leftPersonMoveTable.person_id);
			}
			return leftPersonMoveTable;
		}
	}

	public class PersonTypeIdColumn extends SQLColumn<Person,Short> {
		public PersonTypeIdColumn(Person table, Column<Short, ?> column) { super(table, column); }
		private PersonType innerPersonTypeTable;
		public PersonType innerPersonType(){
			if (innerPersonTypeTable == null) {
				innerPersonTypeTable = new PersonType();
				join(JoinType.inner,this, innerPersonTypeTable.person_type_id);
			}
			return innerPersonTypeTable;
		}
	}

	public final PersonIdColumn person_id = new PersonIdColumn(this, db.public_.person.person_id);
	public final PersonTypeIdColumn person_type_id = new PersonTypeIdColumn(this, db.public_.person.person_type_id);
	public final SQLColumn<Person,String> first_name = new SQLColumn<Person,String>(this, db.public_.person.first_name);
	public final SQLColumn<Person,String> last_name = new SQLColumn<Person,String>(this, db.public_.person.last_name);
	public final SQLColumn<Person,Date> birth_date = new SQLColumn<Person,Date>(this, db.public_.person.birth_date);
}
