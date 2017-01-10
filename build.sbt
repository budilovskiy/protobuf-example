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
    // generate case classes into
    scalapb.gen(flatPackage = true) -> (sourceDirectory in Compile)(_ / "generated").value
  ),

  // where proto files are extracted to:
  PB.externalIncludePath := ((classDirectory in protocol) in Compile).value,

  // where to look for protos to compile
  PB.protoSources in Compile := Seq(PB.externalIncludePath.value),

  // When compiling in Windows, Python is used to bridge protoc and this JVM.
  // To set the path for Python.exe:
  PB.pythonExe := "C:\\Python27\\Python.exe"
)