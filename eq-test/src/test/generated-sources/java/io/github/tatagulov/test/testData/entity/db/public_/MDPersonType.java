package io.github.tatagulov.test.testData.entity.db.public_;

import static io.github.tatagulov.test.testData.entity.Db.*;
import io.github.tatagulov.eq.metadata.api.columns.string.StringColumn;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.columns.number.ShortColumn;
import io.github.tatagulov.eq.metadata.api.Table;

public class MDPersonType extends Table<MDPersonType> {
	public MDPersonType(Schema schema) { super(schema, "person_type"); }
	public final ShortColumn<MDPersonType> person_type_id = new ShortColumn<MDPersonType>(this,"person_type_id",true,false,5,0,false);
	public final StringColumn<MDPersonType> person_type_name = new StringColumn<MDPersonType>(this,"person_type_name",false,false,255,0,false);
}
