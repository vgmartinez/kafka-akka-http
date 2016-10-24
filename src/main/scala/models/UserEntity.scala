package models

case class UserEntity(id: Option[UserId], username: String, password: String, age: Int, gender: Int) {
  require(!username.isEmpty, "username.empty")
  require(!password.isEmpty, "password.empty")
}

case class UserEntityUpdate(username: Option[String] = None, password: Option[String] = None, age: Option[Int] = None, gender: Option[Int] = None) {
  def merge(user: UserEntity): UserEntity = {
    UserEntity(user.id, username.getOrElse(user.username), password.getOrElse(user.password), age.getOrElse(user.age), gender.getOrElse(user.gender))
  }
}
