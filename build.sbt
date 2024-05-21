name := "com-voltage-udf"

version := "0.1"

scalaVersion := "2.12.19"

publishMavenStyle := true

assemblyMergeStrategy in assembly := {   
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard   
  case x => MergeStrategy.first 
}

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.4.1" % Provided

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.4.1" % Provided


unmanagedJars in Compile += file("lib/vibesimplejava.jar")

run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated

scalacOptions in (Compile, doc) += "-groups"