package io.github.tatagulov.test.testData.entity.db.public_;

import static io.github.tatagulov.test.testData.entity.Db.*;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.SQLTable;
import java.lang.Short;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.sql.JoinType;
import java.lang.String;

public class Dep extends SQLTable<Dep> {
	public Dep() { super(db.public_.dep); }

	public class DepIdColumn extends SQLColumn<Dep,Short> {
		public DepIdColumn(Dep table, Column<Short, ?> column) { super(table, column); }
		private PersonMove innerPersonMoveByFromDepIdTable;
		public PersonMove innerPersonMoveByFromDepId(){
			if (innerPersonMoveByFromDepIdTable == null) {
				innerPersonMoveByFromDepIdTable = new PersonMove();
				join(JoinType.inner,this, innerPersonMoveByFromDepIdTable.from_dep_id);
			}
			return innerPersonMoveByFromDepIdTable;
		}
		private PersonMove leftPersonMoveByFromDepIdTable;
		public PersonMove leftPersonMoveByFromDepId(){
			if (leftPersonMoveByFromDepIdTable == null) {
				leftPersonMoveByFromDepIdTable = new PersonMove();
				join(JoinType.left,this, leftPersonMoveByFromDepIdTable.from_dep_id);
			}
			return leftPersonMoveByFromDepIdTable;
		}
		private PersonMove innerPersonMoveByToDepIdTable;
		public PersonMove innerPersonMoveByToDepId(){
			if (innerPersonMoveByToDepIdTable == null) {
				innerPersonMoveByToDepIdTable = new PersonMove();
				join(JoinType.inner,this, innerPersonMoveByToDepIdTable.to_dep_id);
			}
			return innerPersonMoveByToDepIdTable;
		}
		private PersonMove leftPersonMoveByToDepIdTable;
		public PersonMove leftPersonMoveByToDepId(){
			if (leftPersonMoveByToDepIdTable == null) {
				leftPersonMoveByToDepIdTable = new PersonMove();
				join(JoinType.left,this, leftPersonMoveByToDepIdTable.to_dep_id);
			}
			return leftPersonMoveByToDepIdTable;
		}
	}

	public final DepIdColumn dep_id = new DepIdColumn(this, db.public_.dep.dep_id);
	public final SQLColumn<Dep,String> dep_name = new SQLColumn<Dep,String>(this, db.public_.dep.dep_name);
}
