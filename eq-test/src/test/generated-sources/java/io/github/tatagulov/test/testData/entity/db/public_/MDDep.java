package io.github.tatagulov.test.testData.entity.db.public_;

import static io.github.tatagulov.test.testData.entity.Db.*;
import io.github.tatagulov.eq.metadata.api.columns.string.StringColumn;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.columns.number.ShortColumn;
import io.github.tatagulov.eq.metadata.api.Table;

public class MDDep extends Table<MDDep> {
	public MDDep(Schema schema) { super(schema, "dep"); }
	public final ShortColumn<MDDep> dep_id = new ShortColumn<MDDep>(this,"dep_id",true,false,5,0,false);
	public final StringColumn<MDDep> dep_name = new StringColumn<MDDep>(this,"dep_name",false,false,255,0,false);
}
