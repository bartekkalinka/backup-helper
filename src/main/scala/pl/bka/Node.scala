package pl.bka

import java.io.File

case class NodeAttributes(path: String, name: String)

sealed trait Node { self =>
  val attributes: NodeAttributes
  val size: Long
  def nodes: List[Node]
  def files = nodes.filter(_.isInstanceOf[FileNode]).map(_.asInstanceOf[FileNode])
  def dirs = nodes.filter(_.isInstanceOf[DirNode]).map(_.asInstanceOf[DirNode])
  def ignored = nodes.filter(_.isInstanceOf[IgnoredNode]).map(_.asInstanceOf[IgnoredNode])
  def totalSize: Long
  def numFiles: Long
  def isSiblingOf(ancestor: Node): Boolean = self.attributes.path.startsWith(ancestor.attributes.path)
  def toStringForLS: String
}

object Node {
  def apply(path: String): Node = {
    val file = new File(path)
    try {
      if(file.isFile) {
        FileNode(NodeAttributes(file.getPath, file.getName), file.length)
      }
      else if (file.isDirectory && file.getCanonicalPath == path) {
        DirNode(NodeAttributes(file.getPath, file.getName), file.length,
          file.listFiles.map(subFile => Node(subFile.getPath)).toList)
      } else IgnoredNode(path, None)
    }
    catch {
      case e:Exception =>
        println(s"problem with building Node on path $path")
        println(s"isFile ${file.isFile} isDirectory ${file.isDirectory} isAbsolute ${file.isAbsolute} isHidden ${file.isHidden} canonicalPath ${file.getCanonicalPath}")
        IgnoredNode(path, Some(e.toString))
    }
  }

  implicit def toNodeApi(tree: Node): NodeApi = NodeApi(tree)

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
  def nodes = List(this)
  def totalSize = size
  def numFiles = 1L
  override def toString = s"FileNode(${attributes.path}, ${Node.prettySize(size)})"
  def toStringForLS = s"FileNode(${attributes.name}, ${Node.prettySize(size)})"
}

case class DirNode(attributes: NodeAttributes, size: Long, children: List[Node]) extends Node {
  def nodes = this :: children.foldLeft(List[Node]())(
    (acc, subnode) => acc ++ subnode.nodes
  )
  def totalSize = size + children.foldLeft(0L)((acc, child) => acc + child.totalSize)
  def numFiles = children.foldLeft(0L)((acc, child) => acc + child.numFiles)
  override def toString = s"DirNode(${attributes.path}, ${Node.prettySize(totalSize)}, $numFiles files)"
  def toStringForLS = s"DirNode(${attributes.name}, ${Node.prettySize(totalSize)}, $numFiles files)"
}

case class IgnoredNode(path: String, exception: Option[String]) extends Node {
  val attributes = NodeAttributes(path, "")
  val size = 0L
  def nodes = List(this)
  def totalSize = 0L
  def numFiles = 0L
  override def toString = s"IgnoredNode($path)"
  def toStringForLS = s"IgnoredNode($path)"
}
