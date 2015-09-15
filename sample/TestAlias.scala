

object TestAlias {

  type XId = Int
  type YId = Int
  type XXId = XId
  def xId2Int(xid: XId): Int = xid
  def yId2Int(yid: YId): Int = yid

  def test1() = {
    println(xId2Int(1: XId))
    println(xId2Int(2: XXId))
    println(xId2Int(3)) // should be compile error
    println(xId2Int(4: YId)) // should be compile error
    val xid1: XId = 123: XId
    val xid2: XId = 123: XXId
    val xid3: XId = 123 // should be compile error
    val xid4: XId = 123: YId // should be compile error

    var xid: XId = 123: XId
    xid = 4: XId
    xid = 5: XXId
    xid = 6 // should be compile error
    xid = 7: YId // should be compile error
    println(xid)

    // XXX List[Int]がscala.collection.immutable.List[Int]へのaliasなのでerrorになる。確かに。どう対応しよう？
    // val s1: List[Int] = List(1, 2, 3, 4)
    // println(s1)
    val s2: scala.collection.immutable.List[Int] = List(1, 2: XId, 3: XXId, 4: YId)
    println(s2)
    val s3: scala.collection.immutable.List[XId] = List(1, 2: XId, 3: XXId, 4: YId) // XXX should be compile error だけど通ってしまう
    println(s3)
    val s4: scala.collection.immutable.List[XId] = List[XId](1, 2: XId, 3: XXId, 4: YId) // XXX should be compile error だけど通ってしまう
    println(s4)
  }

  type MyOption[A] = Option[A]
  def myGet[A](op: MyOption[A]): A = op.get
  def test2() = {
    println(myGet(Option(1))) // shoule be compile error
    println(myGet(Option(2): MyOption[Int]))
    println(myGet(Option("HOGE"))) // shoule be compile error
    println(myGet(Option("FUGA"): MyOption[String]))
  }

  class C1
  class C2 extends C1
  type T1 = C1
  type T2 = C2
  def fC1(v: C1): String = v.toString
  def fC2(v: C2): String = v.toString
  def fT1(v: T1): String = v.toString
  def fT2(v: T2): String = v.toString
  def test3() = {
    println(fC1(new C1))
    println(fC1(new C1: T1))
    println(fC1(new C2))
    println(fC1(new C2: C1))
    println(fC1(new C2: T1))
    println(fC1(new C2: T2))
    println(fT1(new C1)) // should be compile error
    println(fT1(new C1: T1))
    println(fT1(new C2)) // should be compile error
    println(fT1(new C2: C1)) // should be compile error
    println(fT1(new C2: T1))
    println(fT1(new C2: T2)) // should be compile error

    println(fC2(new C2))
    println(fC2(new C2: T2))
    println(fT2(new C2)) // should be compile error
    println(fT2(new C2: T2))
  }

  def main(args: Array[String]): Unit = {
    println("TestAlias.main begin")
    test1()
    test2()
    test3()
    println("TestAlias.main end")
  }
}
