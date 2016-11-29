package models

/**
  * Created by victorgarcia on 14/11/16.
  */
case class Metadata(timestamp: Long, topic: String, partition: Int)

case class Topics(timestamp: Long, topics: List[String])

case class Result(statusCode: Int, code: String, message: String, data: Option[Any] = None)
