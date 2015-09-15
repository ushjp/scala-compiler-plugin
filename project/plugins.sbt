logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Classpaths.sbtPluginReleases
resolvers += Resolver.sonatypeRepo("releases")

// for Eclipse
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")
