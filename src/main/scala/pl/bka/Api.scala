package pl.bka

object Api {
  def get(path: String): Node = Node(path)

  def jsonwrite(tree: Node): Unit = jsonwrite(tree, "./tree.json")

  def jsonwrite(tree: Node, jsonFilePath: String): Unit = {
    JsonOps.writeToJsonFile(tree, jsonFilePath)
    println("Done")
  }

  def jsonread: Node = jsonread("./tree.json")

  def jsonread(jsonFilePath: String = "./tree.json"): Node = JsonOps.readFromJsonFile(jsonFilePath)
}

