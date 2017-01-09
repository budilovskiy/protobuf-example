lazy val protocol = project.in(file("protocol"))
  .settings(commonSettings: _*)

lazy val ingest = project.in(file("ingest"))
  .dependsOn(protocol)
  .settings(commonSettings: _*)
  .settings(protobufSettings(protocol))

def commonSettings: Seq[Setting[_]] = Def.settings(
  version := "1.0-SNAPSHOT",
  organization in ThisBuild := "me.alexray",
  scalaVersion in ThisBuild := "2.11.8"
)

def protobufSettings(protocol: Project): Seq[Setting[_]] = Def.settings(
  PB.targets in Compile := Seq(
    scalapb.gen(flatPackage = true, javaConversions = true) -> (sourceDirectory in Compile)(_ / "generated").value,
    PB.gens.java -> (sourceDirectory in Compile)(_ / "generated").value
  ),
  PB.externalIncludePath := ((classDirectory in protocol) in Compile).value
)

//def protobufSettings(protocol: Project) = PB.protobufSettings ++ Seq(
//  version in PB.protobufConfig := "2.6.1",
//  PB.runProtoc in PB.protobufConfig := (args => com.github.os72.protocjar.Protoc.runProtoc("-v261" +: args.toArray)),
//  javaSource in PB.protobufConfig <<= (sourceDirectory in Compile)(_ / "generated"),
//  scalaSource in PB.protobufConfig <<= (sourceDirectory in Compile)(_ / "generated"),
//  PB.flatPackage in PB.protobufConfig := true,
//  PB.externalIncludePath in PB.protobufConfig := ((classDirectory in protocol) in Compile).value,
//  sourceDirectories in PB.protobufConfig <+= PB.externalIncludePath in PB.protobufConfig
//)