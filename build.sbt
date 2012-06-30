scalacOptions in Compile ++= Seq(
  "-deprecation"
)

resolvers += "repo.codahale.com" at "http://repo.codahale.com"

libraryDependencies ++= Seq(
  "com.codahale" %% "jerkson" % "0.5.0",
  "commons-io" % "commons-io" % "2.3"
)

