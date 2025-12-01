package com.galamome

import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
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
      typedNormalize(col("address"))
    )

    result.show(false)

    /*
    val dfWithExperiencePadded = df.withColumn("skills", setExperienceUdf(col("skills")))

    dfWithExperiencePadded.show(false)
     */
  }

  /*
  private val addAllSkillsUDF = udf((skills: Seq[Row]) => {
    val allSkillNames = skills.map(_.getAs[String]("name"))
    skills.map { s =>
      Map(
        "name" -> s.getAs[String]("name"),
        "level" -> s.getAs[String]("level"),
        "all_skills" -> allSkillNames
      )
    }
  })

  private def setExperienceUdf: UserDefinedFunction = udf((items: Seq[Row]) => {
    if (items == null)
    {
      Seq.empty[Row]
    }
    else
    {
      setExperience(items)
    }
  }, skillArrayType)

  private def setExperience(skills: Seq[Row]): Seq[GenericRowWithSchema] = {
    val allSkills = new ListBuffer[GenericRowWithSchema]()

    skills.foreach(skill => {
      if (skill.getAs[String](EXPERIENCE) != null) {
        val currentSkill = Map(
          NAME -> skill.getAs[String](NAME),
          LEVEL -> skill.getAs[String](LEVEL),
          EXPERIENCE -> skill.getAs[String](EXPERIENCE)
        )
        allSkills += makeRow(skillSchema, currentSkill)
      }
      else {
        val skillDefaultExperience = Map(
          NAME -> skill.getAs[String](NAME),
          LEVEL -> skill.getAs[String](LEVEL),
          EXPERIENCE -> "default experience"
        )
        allSkills += makeRow(skillSchema, skillDefaultExperience)
      }
    })
    allSkills.sortBy(_.getAs[String](NAME))
  }
*/

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

  val typedNormalize: UserDefinedFunction =
    udf[Address, Address](normalizeAddress)


}

