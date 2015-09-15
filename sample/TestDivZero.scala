

object TestDivZero {
  def main(args: Array[String]): Unit = {
    println("Test.main begin")
    val x = 123 / 0
    println(x)
    println("Test.main end")
  }
}
