package com.github.ushjp.plugin

import scala.tools.nsc.{Global, Phase}
import scala.tools.nsc.Global
import scala.tools.nsc.plugins.{Plugin, PluginComponent}

class StrictTypeAlias(val global: Global) extends Plugin {
  override val name: String = "stricttypealias"
  override val description: String = "checks for strict type alias"
  override val components: List[PluginComponent] = List(Component)

  private object Component extends PluginComponent {
    override val global: StrictTypeAlias.this.global.type = StrictTypeAlias.this.global
    import global._

    override val runsAfter: List[String] = List("refchecks")
    override val runsBefore: List[String] = List("uncurry")

    override val phaseName: String = StrictTypeAlias.this.name
    override def newPhase(prev: Phase): Phase = new StrictTypeAliasPhase(prev)

    class StrictTypeAliasPhase(prev: Phase) extends StdPhase(prev) {
      override val name: String = StrictTypeAlias.this.name
      def apply(unit: CompilationUnit): Unit = {
//        for(tree @ Apply(x1, x2) <- unit.body) {
//          println(s"${tree.pos}")
//          println(s"x1 = $x1")
//          println(s"x2 = $x2")
//          println(s"x1.tpe = ${x1.tpe}")
//          println(s"x1.tpe.params = ${x1.tpe.params}")
//          for(aaa <- x1.tpe.params) {
//            println(s"${aaa.tpe} : isAlias = ${isAlias(aaa.tpe)}")
//          }
//        }

        unit.body.foreach {
          case tree @ Apply(f, vs) =>
            for((x, v) <- f.tpe.params.zip(vs))
              testStrictAlias(tree.pos, x.typeSignature, v.tpe)
          case tree @ Assign(x, v) =>
            testStrictAlias(tree.pos, x.tpe, v.tpe)
          case tree @ ValDef(_, x, t, v) if (!v.isEmpty) =>
            testStrictAlias(tree.pos, t.tpe, v.tpe)
          case _ =>
        }
      }

      def testStrictAlias(pos: Position, required: Type, found: Type): Unit = {
        if (isAlias(required) && !canReducedTo(found, required)) {
          reporter.error(pos, s"<$found> cannot be reduced to <$required>")
        }
      }

      def isAlias(t: Type): Boolean = {
        t.betaReduce != t
      }

      def canReducedTo(t1: Type, t2: Type): Boolean = {
        if (t1 == t2)
          true
        else if (isAlias(t1))
          canReducedTo(t1.betaReduce, t2)
        else
          false
      }
    }
  }
}
