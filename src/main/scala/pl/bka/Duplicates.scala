package pl.bka

object Duplicates {
  def unfilterSiblings(nodes: List[Node]): List[Node] = {
    def internalUnfilter(nodes: List[Node], acc: List[Node]): List[Node] = nodes match {
      case Nil => acc
      case h :: t => if (acc.headOption.exists(acch => h.isSiblingOf(acch))) internalUnfilter(t, acc) else internalUnfilter(t, h :: acc.toList)
    }
    internalUnfilter(nodes.sortBy(_.attributes.path), List[Node]())
  }

  def findDuplicates(tree: Node): Seq[Seq[Node]] = {
    println("0")
    val nodes = tree.listNodes
    println("1 nodes.length " + nodes.length)
    val pathsMap: Map[String, Node] = nodes.groupBy(n => n.attributes.path).mapValues(_.head)
    println("2 pathsMap.toSeq.length " + pathsMap.toSeq.length)
    val buckets: Map[(String, Long), List[Node]] = nodes
      .groupBy(n => (n.attributes.name, n.totalSize))
    println("3 buckets.toSeq.length " + buckets.toSeq.length)
    val dupBuckets = buckets.filter(_._2.length > 1)
    println("4 dupBuckets.toSeq.length " + dupBuckets.toSeq.length)
    val pathsToBuckets = dupBuckets.flatMap { case bucket@(_, grouppedNodes) => grouppedNodes.map(n => (n.attributes.path, bucket)) }
    println("5")
    val dupNodes = pathsToBuckets.keys.toSeq.distinct.map(path => pathsMap.get(path).get)
    println("6 dupNodes.length " + dupNodes.length)
    val dupPathsToLeave = unfilterSiblings(dupNodes.toList).map(_.attributes.path).groupBy(identity)
    println("7 dupPathsToLeave.length " + dupPathsToLeave.keys.toSeq.length)
    val result = pathsToBuckets.filterKeys(dupPathsToLeave.contains).values.toMap.values.toSeq
    println("8 result.length " + result.length)
    result
  }

  def report(duplicates: Seq[Seq[Node]]): String =
    duplicates.map(group => "\n" + group.map(node => "\n" + node.toString).reduce(_ + _)).reduce(_ + _)
}

