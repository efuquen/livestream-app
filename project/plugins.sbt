resolvers += Resolver.url(
  "sbt-plugin-snapshots",
    new
    URL("http://edftwin.com:8081/nexus/content/groups/public")
    )(Resolver.ivyStylePatterns)

addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8-SNAPSHOT")
