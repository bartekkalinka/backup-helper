package pl.bka

import java.io.{File, PrintWriter}

object FileOps {
  def writeFile(path: String, content: String) = {
    val writer = new PrintWriter(new File(path))
    writer.write(content)
    writer.close()
  }

  def readFile(path: String): Option[String] = {
    val file = new File(path)
    if (file.exists) {
      val testTxtSource = scala.io.Source.fromFile(path)
      val str = testTxtSource.mkString
      testTxtSource.close()
      Some(str)
    } else None
  }
}


