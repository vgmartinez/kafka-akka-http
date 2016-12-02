package models

/**
  * Created by victorgarcia on 14/11/16.
  */
case class Metadata(timestamp: Long, topic: String, partition: Int)

case class Topics(timestamp: Long, topics: List[String])

case class ResultTopics(statusCode: Int, code: String, message: String, data: Option[Topics] = None)
case class ResultMetadata(statusCode: Int, code: String, message: String, data: Option[Metadata] = None)
