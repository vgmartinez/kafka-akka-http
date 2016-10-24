package utils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

object URLUtil {
  private val safeCharacters = scala.collection.immutable.BitSet.empty
  private val hexadecimal: Array[Char] = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

  def encodePath(path: String): String = {
    val maxBytesPerChar: Int = 10
    val rewrittenPath: StringBuffer = new StringBuffer(path.length)
    val buf: ByteArrayOutputStream = new ByteArrayOutputStream(maxBytesPerChar)
    var writer: OutputStreamWriter = null
    try {
      writer = new OutputStreamWriter(buf, "UTF8")
    }
    catch {
      case e: Exception => {
        e.printStackTrace
        writer = new OutputStreamWriter(buf)
      }
    }
    var i: Int = 0
    while (i < path.length) {
      {
        val c: Int = path.charAt(i)
        if (safeCharacters(c)) {
          rewrittenPath.append(c.toChar)
        }
        else {
          try {
            writer.write(c)
            writer.flush
          }
          catch {
            case e: IOException => {
              buf.reset
            }
          }
          val ba: Array[Byte] = buf.toByteArray
          var j: Int = 0
          while (j < ba.length) {
            {
              val toEncode: Byte = ba(j)
              rewrittenPath.append('%')
              val low: Int = (toEncode & 0x0f)
              val high: Int = ((toEncode & 0xf0) >> 4)
              rewrittenPath.append(hexadecimal(high))
              rewrittenPath.append(hexadecimal(low))
            }
            j += 1
            j - 1
          }
          buf.reset
        }
      }
      i += 1
      i - 1
    }
    rewrittenPath.toString
  }
}