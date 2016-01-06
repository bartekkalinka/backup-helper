package pl.bka

import spray.json._

object JsonOps extends DefaultJsonProtocol {

  implicit object NodeJsonFormat extends RootJsonFormat[Node] {
    def write(n: Node) = n match {
      case f: FileNode => f.toJson
      case d: DirNode => d.toJson
      case i: IgnoredNode => i.toJson
    }

    def read(value: JsValue) = value match {
      case obj: JsObject =>
        if(obj.fields.contains("children"))
          obj.convertTo[DirNode]
        else if(obj.fields.contains("path") )
          obj.convertTo[IgnoredNode]
        else obj.convertTo[FileNode]
      case _ => deserializationError("Node expected")
    }
  }
  implicit val nodeAttributesFormat = jsonFormat2(NodeAttributes)
  implicit val fileNodeFormat = jsonFormat(FileNode, "attributes", "size")
  implicit val dirNodeFormat = jsonFormat(DirNode, "attributes", "size", "children")
  implicit val ignoredNodeFormat = jsonFormat(IgnoredNode, "path", "exception")

  def writeToJson(tree: Node): String = tree.toJson.compactPrint

  def readFromJson(json: String): Node = json.parseJson.convertTo[Node]

  def writeToJsonFile(tree: Node, jsonPersistFile: String) = {
    FileOps.writeFile(jsonPersistFile, writeToJson(tree))
  }

  def readFromJsonFile(jsonPersistFile: String): Node = FileOps.readFile(jsonPersistFile).map(readFromJson).getOrElse(Node(jsonPersistFile))
}