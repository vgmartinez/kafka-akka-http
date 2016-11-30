
package com.kst.services.users

import java.util.Properties
import javax.naming.{Context, NamingEnumeration}
import javax.naming.directory.{InitialDirContext, SearchControls, SearchResult}

/**
  * Created by victorgarcia on 15/11/16.
  */
object LDAPValidations {

  def validateForLDAP(username: String, passcode: String): String = {
    try {
      val props = new Properties
      props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory")
      props.put(Context.PROVIDER_URL, "ldap://ldap.forumsys.com:389")
      props.put(Context.SECURITY_AUTHENTICATION, "simple")
      props.put(Context.SECURITY_PRINCIPAL, s"uid=$username, dc=example, dc=com")
      props.put(Context.SECURITY_CREDENTIALS, passcode)

      val context: InitialDirContext = new InitialDirContext(props)

      val controls: SearchControls = new SearchControls
      controls.setReturningAttributes(Array[String]("mail", "uid"))
      controls.setSearchScope(SearchControls.SUBTREE_SCOPE)

      val answers: NamingEnumeration[SearchResult] = context.search("dc=example, dc=com", s"uid=$username", controls)
      val result: SearchResult = answers.nextElement
      result.getName
    }
    catch {
      case e: Exception => {
        null
      }
    }
  }
}
