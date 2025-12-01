package com.galamome

import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, Row, SparkSession, functions}
import Row.makeRow

import org.apache.spark.sql.catalyst.dsl.expressions.StringToAttributeConversionHelper
import org.apache.spark.sql.functions.{col, lit, map_keys, udf}
import org.apache.spark.sql.types.{ArrayType, IntegerType, StringType, StructField, StructType}

import scala.collection.mutable.ListBuffer


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("load-json")
      .master("local[*]")
      .config("spark.driver.bindAddress", "127.0.0.1")
      .getOrCreate()

    val df = spark.read
      .option("multiline", value = true)
      .option("mode", "PERMISSIVE")
      //.schema(schema)
      .json("data/skills_premier_niveau.json")

    df.show(false)
    df.printSchema()

    val result = df.withColumn(
      "address",
      typedNormalizeAddress(col("address"))
    )

    val result2 = result.withColumn("skills",
      functions.transform(col("skills"), s => typedNormalizeSkill(s)))

    result2.show(false)
  }

  // https://furcypin.github.io/spark-frame/use_cases/working_with_nested_data/
  /*
  def addDeeplyNestedColumn(df: DataFrame): DataFrame = {
  df.withColumn("skills",
  transform(col("skills"),
    item => item.withField("skillList", addAllSkillsUDF(col("skills")))))
  }

   */
  def normalizeAddress(a: Address): Address = {
    a.copy(city = a.city.toUpperCase)
  }

  val DEFAULT_EXPERIENCE = 50

  def normalizeSkill(s: Skill): Skill = {
    s.copy(level = s.level.toUpperCase, experience = if (s.experience != null) s.experience else DEFAULT_EXPERIENCE)
  }

  val typedNormalizeAddress: UserDefinedFunction =
    udf[Address, Address](normalizeAddress)

  val typedNormalizeSkill: UserDefinedFunction =
    udf[Skill, Skill](normalizeSkill)


}

