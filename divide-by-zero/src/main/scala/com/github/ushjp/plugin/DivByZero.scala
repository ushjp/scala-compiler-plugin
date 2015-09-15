package com.github.ushjp.plugin

import scala.tools.nsc.{Global, Phase}
import scala.tools.nsc.Global
import scala.tools.nsc.plugins.{Plugin, PluginComponent}

class DivByZero(val global: Global) extends Plugin {
  val name: String = "divbyzero"
  val description: String = "checks for division by zero"
  val components: List[PluginComponent] = List(Component)

  private object Component extends PluginComponent {
    val global: DivByZero.this.global.type = DivByZero.this.global
    import global._

    val runsAfter: List[String] = List("refchecks")

    val phaseName: String = DivByZero.this.name
    def newPhase(prev: Phase): Phase = new DivByZeroPhase(prev)

    class DivByZeroPhase(prev: Phase) extends StdPhase(prev) {
      override def name: String = DivByZero.this.name
      def apply(unit: CompilationUnit): Unit = {
        for {
          tree @ Apply(Select(rcvr, nme.DIV), List(Literal(Constant(0)))) <- unit.body
          if rcvr.tpe <:< definitions.IntClass.tpe
        } {
          global.reporter.error(tree.pos, "definitely division by zero")
//          unit.error(tree.pos, "definitely division by zero")
        }
      }
    }
  }
}
