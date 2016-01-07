package pl.bka

object Api {
  var tree: Node = null
  var directory: DirNode = null

  private def setDirectory(node: Node) = node match { case d: DirNode => directory = d; case _ => () }

  private def setTree(newTree: Node): Node = {
    tree = newTree
    setDirectory(tree)
    tree
  }

  def get(path: String): Node = setTree(Node(path))

  def ser(tree: Node = tree, jsonFilePath: String = "./tree.json"): Unit = {
    JsonOps.writeToJsonFile(tree, jsonFilePath)
    println("Done")
  }

  def deser(jsonFilePath: String = "./tree.json"): Node =
    setTree(JsonOps.readFromJsonFile(jsonFilePath))

  def dup(tree:Node = tree): Seq[Seq[Node]] = Duplicates.findDuplicates(tree)

  def report(duplicates: Seq[Seq[Node]], reportFilePath: String = "./duplicates.txt") = {
    FileOps.writeFile(reportFilePath, Duplicates.report(duplicates))
    println("Done")
  }

  def ls(directory: DirNode = directory) = {
    println(directory.attributes.path)
    directory.children.foreach(c => println(c.toStringForLS))
  }

  def cd(dirName: String) = {
    directory.children.find(c => c.attributes.name == dirName).foreach { d => setDirectory(d) }
    ls()
  }
}

