package io.github.tatagulov.test.testData.entity.db.public_;

import static io.github.tatagulov.test.testData.entity.Db.*;
import io.github.tatagulov.eq.metadata.api.columns.number.IntegerColumn;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.columns.number.ShortColumn;
import io.github.tatagulov.eq.metadata.api.columns.datetime.DateColumn;
import io.github.tatagulov.eq.metadata.api.Table;

public class MDPersonMove extends Table<MDPersonMove> {
	public MDPersonMove(Schema schema) { super(schema, "person_move"); }
	public final IntegerColumn<MDPersonMove> person_move_id = new IntegerColumn<MDPersonMove>(this,"person_move_id",true,false,10,0,false);
	public final IntegerColumn<MDPersonMove> person_id = new IntegerColumn<MDPersonMove>(this,"person_id",false,false,10,0,false);
	public final ShortColumn<MDPersonMove> from_dep_id = new ShortColumn<MDPersonMove>(this,"from_dep_id",false,false,5,0,false);
	public final ShortColumn<MDPersonMove> to_dep_id = new ShortColumn<MDPersonMove>(this,"to_dep_id",false,false,5,0,false);
	public final DateColumn<MDPersonMove> move_date = new DateColumn<MDPersonMove>(this,"move_date",false,true,13,0,false);
}
