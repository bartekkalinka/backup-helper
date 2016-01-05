package pl.bka

object Api {
  var curtree: Node = null

  def get(path: String): Node = {
    curtree = Node(path)
    curtree
  }

  def ser(tree: Node = curtree, jsonFilePath: String = "./tree.json"): Unit = {
    JsonOps.writeToJsonFile(tree, jsonFilePath)
    println("Done")
  }

  def deser(jsonFilePath: String = "./tree.json"): Node = {
    curtree = JsonOps.readFromJsonFile(jsonFilePath)
    curtree
  }

  def dup(tree:Node = curtree): Seq[Seq[Node]] = Duplicates.findDuplicates(tree)
}

