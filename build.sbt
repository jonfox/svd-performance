
enablePlugins(JavaAppPackaging)

name := "svd-performance"

version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-math3" % "3.6.1",
  "com.googlecode.matrix-toolkits-java" % "mtj" % "1.0.4",
  "com.github.fommil" % "java-logging" % "1.1",
  "org.ojalgo" % "ojalgo" % "51.1.0",
  "dev.ludovic.netlib" % "blas" % "2.2.1",
  "dev.ludovic.netlib" % "lapack" % "2.2.1",
  "dev.ludovic.netlib" % "arpack" % "2.2.1",
)
