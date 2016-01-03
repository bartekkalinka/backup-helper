package pl.bka

object Api {
  def get(path: String): Node = Node(path)

  def jsonwrite(tree: Node, jsonFilePath: String = "./tree.json"): Unit = {
    JsonOps.writeToJsonFile(tree, jsonFilePath)
    println("Done")
  }

  def jsonread(jsonFilePath: String = "./tree.json"): Node = JsonOps.readFromJsonFile(jsonFilePath)
}

