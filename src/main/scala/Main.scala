package com.galamome

import org.apache.spark.sql.{DataFrame, SparkSession, Row}
import org.apache.spark.sql.functions.{col, lit, udf}


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
      .json("data/skills_premier_niveau.json")

    df.show(false)


    //val updatedDf = addDeeplyNestedColumn(df)
      /*
      .withColumn(
      "profile",
      col("profile").withField(
        "professional",
        col("profile.professional").withField(
          "skills",
          addAllSkillsUDF(col("profile.professional.skills"))
        )
      )
    )
    */
    /*
    val updatedDf = df.withColumn(
      "skills",
      col("skills")
        .withField("skills", addAllSkillsUDF(col("skills")))
    )
    */

    /*
    updatedDf.printSchema()
    updatedDf.show(false)
*/
    /*
    val transformedDf = addDeeplyNestedColumn(df)
    transformedDf.printSchema()
    transformedDf.show(false)
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

  // https://furcypin.github.io/spark-frame/use_cases/working_with_nested_data/
  def addDeeplyNestedColumn(df: DataFrame): DataFrame = {
  df.withColumn("skills",
  transform(col("skills"),
    item => item.withField("skillList", addAllSkillsUDF(col("skills")))))
  }
  */
}

