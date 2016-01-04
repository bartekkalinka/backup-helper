# backup-helper
reading and processing file system trees in sbt console

Usage
=====

    sbt console

read filesystem tree into scala objects graph:

    scala> get("/home/bka/github")
    res0: pl.bka.Node = DirNode(/home/bka/github, 5.7 MB, 331)

serialize it to json file (default file is ./tree.json):

    scala> jsonwrite(res0)
    Done

deserialize back to a tree:

    scala> jsonread()
    res2: pl.bka.Node = DirNode(/home/bka/github, 5.7 MB, 331)

find duplicates:

    scala> dup(res2)
    res3: Seq[Seq[pl.bka.Node]] = ...

Remarks
======

For large trees, serializing needs more memory, so use for example:
    
    sbt -J-Xmx2G console


