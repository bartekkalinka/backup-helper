# backup-helper
reading and processing file system trees in sbt console

Usage
=====

    sbt console

read filesystem tree into scala objects graph (current tree):

    scala> get("/home/bka/github")
    res0: pl.bka.Node = DirNode(/home/bka/github, 5.7 MB, 331 subnodes)

analyze tree:

    scala> tree.listNodes.filter(_.attributes.name.endsWith("scala")).length
    res1: Int = 70

serialize it to json file (default file is ./tree.json):

    scala> ser()
    Done

deserialize back to a tree:

    scala> deser()
    res3: pl.bka.Node = DirNode(/home/bka/github, 5.7 MB, 331 subnodes)

find duplicates:

    scala> dup()
    res4: Seq[Seq[pl.bka.Node]] = ...

write report of duplicates to file (default file is ./duplicates.txt):

    scala> report(res4)
    Done

navigate through current tree:

    scala> ls()
    /home/bka/github
    DirNode(caves, 4.5 MB, 882 subnodes)
    DirNode(backup-helper, 149.0 MB, 322 subnodes)

Remarks
======

For large trees, serializing needs more memory, so use for example:
    
    sbt -J-Xmx2G console


