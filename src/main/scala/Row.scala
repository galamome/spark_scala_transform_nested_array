import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.types.{ArrayType, StructType}

object Row {
  /**
   * Recursivly converts a map into a GenericRowWithSchema from a schema
   * @param schema StructType
   * @param data Map[String, Any]
   * @return a GenericRowWithSchema, allows to retrieve an attribute by its name, for example `getAs[String]("Type")`
   */
  def makeRow(schema: StructType, data: Map[String, Any]): GenericRowWithSchema = {
    val values = schema.fields.map { field =>
      val value = data.getOrElse(field.name, null)
      field.dataType match {
        case st: StructType if value != null =>
          makeRow(st, value.asInstanceOf[Map[String, Any]]) // recurse for nested struct
        case ArrayType(st: StructType, _) if value != null =>
          value.asInstanceOf[Seq[Map[String, Any]]].map(v => makeRow(st, v)) // array of structs
        case ArrayType(_, _) if value != null =>
          value.asInstanceOf[Seq[Any]]
        case _ =>
          value
      }
    }
    new GenericRowWithSchema(values, schema)
  }
}
