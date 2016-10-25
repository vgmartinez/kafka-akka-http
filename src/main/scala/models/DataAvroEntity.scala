package models

import scala.util.parsing.json.JSONObject

/**
  * Created by victorgarcia on 25/10/16.
  */
case class DataAvroEntity(value_schema: JSONObject, records: List[JSONObject])