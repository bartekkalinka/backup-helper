package pl.bka

object Api {
  def get(path: String): Node = Node(path)

  def ser(tree: Node, jsonFilePath: String = "./tree.json"): Unit = {
    JsonOps.writeToJsonFile(tree, jsonFilePath)
    println("Done")
  }

  def deser(jsonFilePath: String = "./tree.json"): Node = JsonOps.readFromJsonFile(jsonFilePath)

  def dup(tree:Node): Seq[Seq[Node]] = Duplicates.findDuplicates(tree)
}

