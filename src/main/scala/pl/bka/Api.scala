package pl.bka

object Api {
  var tree: Node = null

  def get(path: String): Node = {
    tree = Node(path)
    tree
  }

  def ser(tree: Node = tree, jsonFilePath: String = "./tree.json"): Unit = {
    JsonOps.writeToJsonFile(tree, jsonFilePath)
    println("Done")
  }

  def deser(jsonFilePath: String = "./tree.json"): Node = {
    tree = JsonOps.readFromJsonFile(jsonFilePath)
    tree
  }

  def dup(tree:Node = tree): Seq[Seq[Node]] = Duplicates.findDuplicates(tree)
}

