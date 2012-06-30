scalacOptions in Compile ++= Seq(
  "-deprecation"
)

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

resolvers += "repo.codahale.com" at "http://repo.codahale.com"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.codahale" %% "jerkson" % "0.5.0",
  "commons-io" % "commons-io" % "2.3"
)

