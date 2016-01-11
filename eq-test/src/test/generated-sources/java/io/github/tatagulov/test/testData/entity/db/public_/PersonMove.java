package io.github.tatagulov.test.testData.entity.db.public_;

import static io.github.tatagulov.test.testData.entity.Db.*;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.SQLTable;
import io.github.tatagulov.eq.metadata.api.Column;
import java.lang.Short;
import io.github.tatagulov.eq.metadata.sql.JoinType;
import java.lang.Integer;
import java.sql.Date;

public class PersonMove extends SQLTable<PersonMove> {
	public PersonMove() { super(db.public_.personMove); }

	public class PersonIdColumn extends SQLColumn<PersonMove,Integer> {
		public PersonIdColumn(PersonMove table, Column<Integer, ?> column) { super(table, column); }
		private Person innerPersonTable;
		public Person innerPerson(){
			if (innerPersonTable == null) {
				innerPersonTable = new Person();
				join(JoinType.inner,this, innerPersonTable.person_id);
			}
			return innerPersonTable;
		}
	}

	public class FromDepIdColumn extends SQLColumn<PersonMove,Short> {
		public FromDepIdColumn(PersonMove table, Column<Short, ?> column) { super(table, column); }
		private Dep innerDepTable;
		public Dep innerDep(){
			if (innerDepTable == null) {
				innerDepTable = new Dep();
				join(JoinType.inner,this, innerDepTable.dep_id);
			}
			return innerDepTable;
		}
	}

	public class ToDepIdColumn extends SQLColumn<PersonMove,Short> {
		public ToDepIdColumn(PersonMove table, Column<Short, ?> column) { super(table, column); }
		private Dep innerDepTable;
		public Dep innerDep(){
			if (innerDepTable == null) {
				innerDepTable = new Dep();
				join(JoinType.inner,this, innerDepTable.dep_id);
			}
			return innerDepTable;
		}
	}

	public final SQLColumn<PersonMove,Integer> person_move_id = new SQLColumn<PersonMove,Integer>(this, db.public_.personMove.person_move_id);
	public final PersonIdColumn person_id = new PersonIdColumn(this, db.public_.personMove.person_id);
	public final FromDepIdColumn from_dep_id = new FromDepIdColumn(this, db.public_.personMove.from_dep_id);
	public final ToDepIdColumn to_dep_id = new ToDepIdColumn(this, db.public_.personMove.to_dep_id);
	public final SQLColumn<PersonMove,Date> move_date = new SQLColumn<PersonMove,Date>(this, db.public_.personMove.move_date);
}
