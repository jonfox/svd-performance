
enablePlugins(JavaAppPackaging)

name := "svd-performance"

version := "0.2"

scalaVersion := "2.13.10"

val netlibV = "3.0.3"

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-math3" % "3.6.1",
  "com.googlecode.matrix-toolkits-java" % "mtj" % "1.0.4",
  "com.github.fommil" % "java-logging" % "1.1",
  "org.ojalgo" % "ojalgo" % "52.0.1",
  "dev.ludovic.netlib" % "blas" % netlibV,
  "dev.ludovic.netlib" % "lapack" % netlibV,
  "dev.ludovic.netlib" % "arpack" % netlibV,
)
