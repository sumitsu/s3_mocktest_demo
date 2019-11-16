name := "s3_mocktest_demo"
organization := "net.sumitsu"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.12"

val verSpark = "2.4.4"
val verHadoop = "3.2.1"

libraryDependencies ++= Seq(
  "org.apache.logging.log4j" % "log4j" % "2.12.1",
  "commons-io" % "commons-io" % "2.6",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.639",
  "org.apache.spark" %% "spark-core" % verSpark % "provided",
  "org.apache.spark" %% "spark-sql" % verSpark % "provided",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.apache.hadoop" % "hadoop-common" % verHadoop % "test",
  "org.apache.hadoop" % "hadoop-aws" % verHadoop % "test"
)

// build.sbt timestamp (change to clear ci cache): 20191116205524
