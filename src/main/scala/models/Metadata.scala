package models

/**
  * Created by victorgarcia on 14/11/16.
  */
case class Metadata(timestamp: Long, topic: String, partition: Int)

case class Result(statusCode: Int, code: String, message: String, data: Option[Metadata] = None)
