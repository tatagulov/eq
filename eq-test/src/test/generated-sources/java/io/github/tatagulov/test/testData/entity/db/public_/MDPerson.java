package io.github.tatagulov.test.testData.entity.db.public_;

import static io.github.tatagulov.test.testData.entity.Db.*;
import io.github.tatagulov.eq.metadata.api.columns.string.StringColumn;
import io.github.tatagulov.eq.metadata.api.columns.number.IntegerColumn;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.columns.number.ShortColumn;
import io.github.tatagulov.eq.metadata.api.columns.datetime.DateColumn;
import io.github.tatagulov.eq.metadata.api.Table;

public class MDPerson extends Table<MDPerson> {
	public MDPerson(Schema schema) { super(schema, "person"); }
	public final IntegerColumn<MDPerson> person_id = new IntegerColumn<MDPerson>(this,"person_id",true,false,10,0,false);
	public final ShortColumn<MDPerson> person_type_id = new ShortColumn<MDPerson>(this,"person_type_id",false,false,5,0,false);
	public final StringColumn<MDPerson> first_name = new StringColumn<MDPerson>(this,"first_name",false,false,255,0,false);
	public final StringColumn<MDPerson> last_name = new StringColumn<MDPerson>(this,"last_name",false,true,255,0,false);
	public final DateColumn<MDPerson> birth_date = new DateColumn<MDPerson>(this,"birth_date",false,true,13,0,false);
}
