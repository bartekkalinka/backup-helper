package pl.bka

import java.io.File

case class NodeAttributes(path: String, name: String)

sealed trait Node { self =>
  val attributes: NodeAttributes
  val isFile: Boolean
  val size: Long
  def listNodes: List[Node]
  def totalSize: Long
  def numFiles: Long
  def isSiblingOf(ancestor: Node): Boolean = self.attributes.path.startsWith(ancestor.attributes.path)
}

object Node {
  def apply(path: String): Node = {
    val file = new File(path)
    if(file.isFile) {
      FileNode(NodeAttributes(file.getPath, file.getName), file.length)
    }
    else {
      DirNode(NodeAttributes(file.getPath, file.getName), file.length,
        file.listFiles.map(subFile => Node(subFile.getPath)).toList)
    }
  }

  def prettySize(size: Long): String = {
    def sizeVersion(unitPref: String, index: Int) = {
      val sizeDiv = (size.toDouble * 10d / Math.pow(1000d, index.toDouble)).toLong.toDouble / 10d
      (sizeDiv.toLong, sizeDiv + " " + unitPref + "B")
    }
    val unitPrefs = List("", "K", "M", "G", "T")
    if(size > 0L)
      unitPrefs.zipWithIndex
        .map { case (u, i) => sizeVersion(u, i) }
        .filter(v => v._1 >= 1L && v._1 < 1000L).map(_._2).head
    else "0 B"
  }
}

case class FileNode(attributes: NodeAttributes, size: Long) extends Node {
  val isFile = true
  def listNodes = List(this)
  def totalSize = size
  def numFiles = 1L
  override def toString = "FileNode(" + attributes.path + ", " + Node.prettySize(size) + ")"
}

case class DirNode(attributes: NodeAttributes, size: Long, children: List[Node]) extends Node {
  val isFile = false
  def listNodes = this :: children.foldLeft(List[Node]())(
    (acc, subnode) => acc ++ subnode.listNodes
  )
  def totalSize = size + children.foldLeft(0L)((acc, child) => acc + child.totalSize)
  def numFiles = children.foldLeft(0L)((acc, child) => acc + child.numFiles)
  override def toString = "DirNode(" + attributes.path + ", " + Node.prettySize(totalSize) + ", " + numFiles + ")"
}
