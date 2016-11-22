package models

/**
  * Created by victorgarcia on 21/11/16.
  */
case class PolicyItems(accesses: List[Accesses], users: List[String], groups: List[String], conditions: List[String], delegateAdmin: Boolean)

case class Accesses(`type`: String, isAllowed: Boolean)

case class Topic(values: List[String], isExcludes: Boolean, isRecursive: Boolean)

case class Resource(topic: Topic)

case class PolicyEntity(id: Int, isEnabled: Boolean, version: Int, service: String, name: String, policyType: Int, isAuditEnabled: Boolean, resources: Resource,
                        policyItems: List[PolicyItems], denyPolicyItems: List[String], allowExceptions: List[String], denyExceptions: List[String], dataMaskPolicyItems: List[String],
                        rowFilterPolicyItems: List[String])

