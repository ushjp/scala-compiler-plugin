# scala-compiler-plugin

### 動機
```
type UserId = Int
def f(uid: UserId) = ???
val u1 = f(123) // こっちはコンパイルエラーになってくれると嬉しい
val u2 = f(456: UserId)
```

### 検査内容
期待される型が ```T1``` で、検出された型が ```T2``` の時、以下の条件を満たしたらコンパイルエラーとした。

- (```T1``` がtype alias) かつ (```T2```をbetaReduceしていっても```T1```にならない)

これで上の例をコンパイルエラーにできる。
逆に、この条件だとIntが求められている部分にUserIdを渡すのはコンパイルエラーにならない。その方が嬉しい場合が多いと考えたため。


※型パラメタがtype aliasの場合に未対応です。他にも実装の漏れはかなりあります...

### 使い方
```
$ sbt stict-type-alias/package
$ scalac -Xplugin:strict-type-alias/target/scala-2.11/strict-type-alias_2.11-0.1.0.jar sample/TestAlias.scala
```

### 考察
REPLで試したかったが、

```
$ scala -Xplugin:strict-type-alias/target/scala-2.11/strict-type-alias_2.11-0.1.0.jar
Welcome to Scala version 2.11.7 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_45).
Type in expressions to have them evaluated.
Type :help for more information.

scala> <console>:6: error: <String> cannot be reduced to <String>
  lazy val $print: String =  {
           ^
```

となってしまう。これは ```scala.Predef``` で ```type String = java.lang.String``` してる辺りの問題のはず。

また、同様の問題として ```type List[+A] = scala.collection.immutable.List[A]``` があるので

```
val s1: List[Int] = List(1, 2, 3, 4) // ここがコンパイルエラーになってしまう
val s2: scala.collection.immutable.List[Int] = List(1, 2, 3, 4) // こっちは通る
```

この辺りを解決するには、検査内容をもっときっちり定めないとならないはず。アノテーション付けた型だけ検査する、とできると良いかも。
