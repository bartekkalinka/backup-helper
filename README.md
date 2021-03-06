# backup-helper
reading and processing file system trees in sbt console

Usage
=====

    sbt console

read filesystem tree into scala objects graph (current tree):

    scala> get("/home/bka/github")
    res0: pl.bka.Node = DirNode(/home/bka/github, 5.7 MB, 331 subnodes)

analyze tree:

    scala> tree.nodes.filter(_.attributes.name.endsWith("scala")).length
    res1: Int = 70

serialize it to json file (default file is ./tree.json):

    scala> tree.ser()
    Done

deserialize back to a tree:

    scala> deser()
    res3: pl.bka.Node = DirNode(/home/bka/github, 5.7 MB, 331 subnodes)

find duplicates:

    scala> tree.dup()
    res4: Seq[Seq[pl.bka.Node]] = ...

write report of duplicates to file (default file is ./duplicates.txt):

    scala> report(res4)
    Done

navigate through current tree:

    scala> ls()
    /home/bka/github
    DirNode(caves, 4.5 MB, 882 subnodes)
    DirNode(backup-helper, 149.0 MB, 322 subnodes)
    ...

    scala> cd("caves")
    /home/bka/github/caves
    DirNode(src, 52.4 KB, 13 subnodes)
    FileNode(.gitignore~, 195.0 B)
    ...

join trees:

    scala> join(res0, res1)
    res2: pl.bka.DirNode = DirNode(join, 47.8 GB, 5589 subnodes)

Remarks
======

For large trees, serializing needs more memory, so use for example:
    
    sbt -J-Xmx2G console


