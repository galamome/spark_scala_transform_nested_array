package com.galamome

import org.apache.spark.sql.SparkSession


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
      .json("data/exemple.json")

    df.show(false)
  }
}

