// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.tortoise

import org.nlogo.{ api, headless, nvm },
  nvm.FrontEndInterface.{ ProceduresMap, NoProcedures },
  headless.lang, lang._,
  org.nlogo.util.Femto,
  org.scalatest.Assertions._

trait TortoiseFinder extends lang.Finder {
  def freebies: Map[String, String]
  override def shouldRun(t: LanguageTest, mode: TestMode) =
    mode == NormalMode && super.shouldRun(t, mode)
  override def withFixture[T](name: String)(body: AbstractFixture => T): T =
    freebies.get(name.stripSuffix(" (NormalMode)")) match {
      case None =>
        body(new TortoiseFixture(name))
      case Some("TOO SLOW") =>
        cancel("TOO SLOW")
      case Some(excuse) =>
        try body(new TortoiseFixture(name))
        catch { case ex: Exception =>
          cancel(ex + ": LAME EXCUSE: " + excuse)
        }
        fail("LAME EXCUSE WASN'T NEEDED: " + excuse)
    }
}

class TestReporters extends lang.TestReporters with TortoiseFinder {
  override val freebies =
    Map(
      // significant
      "Equality::Equal2" -> "int vs. double confuses equality",
      // obscure
      "Lists::Sort2" -> "sorting heterogeneous lists doesn't work",
      "Lists::Sort3" -> "sorting heterogeneous lists doesn't work",
      "Lists::Sort5" -> "sorting heterogeneous lists doesn't work"
    )
}

class TestCommands extends lang.TestCommands with TortoiseFinder {
  override val freebies = Map[String, String](
    // to be investigated
    "AgentsetBuilding::EmptyPatchSet" -> "???",
    "AgentsetBuilding::PatchSetNestedLists" -> "???",
    "Agentsets::AgentSetEquality" -> "???",
    "Agentsets::LinkAgentsetDeadLinks" -> "???",
    "Agentsets::RemoveDuplicates" -> "???",
    "AnyAll::All3" -> "???",
    "Ask::AskRNG" -> "???",
    "Ask::recursion-over-ask" -> "???",
    "Breeds::TestIsBreed" -> "???",
    "ComparingAgents::ComparingPatches" -> "???",
    "ControlStructures::Foreach1" -> "???",
    "ControlStructures::Foreach2" -> "???",
    "ControlStructures::Run4" -> "???",
    "ControlStructures::Run5" -> "???",
    "DeadTurtles::DeadTurtles9" -> "???",
    "Diffuse::DiffuseTorus" -> "???",
    "Equality::lists-of-agentsets-can-be-equal" -> "???",
    "Equality::two-dead-turtles-are-equal" -> "???",
    "Equality::dead-turtle-equals-nobody" -> "???",
    "Equality::two-dead-links-are-equal" -> "???",
    "Equality::dead-link-equals-nobody" -> "???",
    "Face::Face1" -> "???",
    "Face::Face2" -> "???",
    "Face::FaceXY1" -> "???",
    "Face::FaceXY2" -> "???",
    "InCone::InConeCornerOrigin3" -> "???",
    "Initialization::InitialEnvironment" -> "???",
    "Interaction::Interaction2" -> "???",
    "Interaction::Interaction3a" -> "???",
    "Interaction::Interaction5" -> "???",
    "Let::LetWithWith" -> "???",
    "Let::LetTwiceInDifferentBlocks" -> "???",
    "Let::LetInsideForeach1" -> "???",
    "Let::LetAndTemplateVariables" -> "???",
    "Let::LetOfVarToItself1" -> "???",
    "Let::LetOfVarToItself2" -> "???",
    "Let::LetOfVarToItself3" -> "???",
    "Let::LetOfVarToItselfInsideAsk" -> "???",
    "Links::Links1" -> "???",
    "Links::Links2" -> "???",
    "Links::LinkFromToWith1" -> "???",
    "Links::LinkCantChangeBreeds" -> "???",
    "Math::NotEqualBugsIFoundWhileTryingToPlayFrogger" -> "???",
    "Neighbors::Neighbors2Torus" -> "???",
    "OneOf::OneOfDyingTurtles" -> "???",
    "PatchAhead::PatchRightAndAhead" -> "???",
    "PatchAhead::PatchLeftAndAhead" -> "???",
    "PatchAt::PatchAt" -> "???",
    "Random::Random1" -> "???",
    "Random::Random2" -> "???",
    "Random::Random3" -> "???",
    "Random::RandomFloat1" -> "???",
    "Random::RandomFloat2" -> "???",
    "Random::RandomNOfIsFairForPatches" -> "???",
    "RandomOrderInitialization::TestRandomOrderInitializationCreateLinksWith" -> "???",
    "RandomOrderInitialization::TestRandomOrderInitializationCreateLinksTo" -> "???",
    "RandomOrderInitialization::TestRandomOrderInitializationCreateLinksFrom" -> "???",
    "SelfMyself::Myself1" -> "???",
    "Stop::StopFromForeach1" -> "???",
    "Sum::MaxMinMeanSumPatchTurtleBreedVars" -> "???",
    "Tie::Tie2Nonrigid" -> "???",
    "Turtles::Turtles2" -> "???",
    "Turtles::Turtles3" -> "???",
    "Turtles::Turtles7" -> "???",
    "TurtlesHere::TurtlesHere1" -> "???",
    "TurtlesHere::PatchDoesOtherTurtlesHere" -> "???",
    "TurtlesHere::PatchDoesOtherBreedHere" -> "???",
    "TypeChecking::SetVariable" -> "???",
    "UpAndDownhill::UpAndDownhill1" -> "???",
    "UpAndDownhill::Uphill3" -> "???",
    // egregious
    //   ...
    // significant
    "ImportWorld::AllBreeds" -> "'special' agentsets not supported",
    "Agentsets::Agentsets1" -> "'special' agentsets not supported",
    "Agentsets::Agentsets4" -> "TOO SLOW",
    "Links::LinksInitBlock" -> "TOO SLOW",
    "Random::RandomNOfIsFairForTurtles" -> "TOO SLOW",
    "Random::RandomNOfIsFairForLinks" -> "TOO SLOW",
    "Random::RandomNOfIsFairForABreed" -> "TOO SLOW",
    "Random::RandomNOfIsFairForAnAgentsetConstructedOnTheFly" -> "TOO SLOW",
    "Random::RandomNOfIsFairForAList" -> "TOO SLOW",
    "Agentsets::AgentsetEquality" -> "'special' agentsets not supported",
    // obscure
    //   ...
    // missing feature: command tasks
    "CommandTasks::*ToString3" -> "command tasks are unsupported",
    "CommandTasks::*ToString4" -> "command tasks are unsupported",
    "CommandTasks::*ToString5" -> "command tasks are unsupported",
    "CommandTasks::*ToString6" -> "command tasks are unsupported",
    "CommandTasks::CallTask" -> "command tasks are unsupported",
    "CommandTasks::NestedCallTask" -> "command tasks are unsupported",
    "CommandTasks::CallTaskAgainAndAgain" -> "command tasks are unsupported",
    "CommandTasks::EasyForeach" -> "command tasks are unsupported",
    "CommandTasks::HarderForeach" -> "command tasks are unsupported",
    "CommandTasks::NestedForeach1" -> "command tasks are unsupported",
    "CommandTasks::NestedForeach2" -> "command tasks are unsupported",
    "CommandTasks::NestedForeach3" -> "command tasks are unsupported",
    "CommandTasks::CloseOverUnoptimizedLet" -> "command tasks are unsupported",
    "CommandTasks::CloseOverOptimizedLet" -> "command tasks are unsupported",
    "CommandTasks::turtle-executing-command-task" -> "command tasks are unsupported",
    "CommandTasks::turtle-executing-command-task-2" -> "command tasks are unsupported",
    "CommandTasks::turtle-executing-command-task-3" -> "command tasks are unsupported",
    "CommandTasks::patch-executing-command-task" -> "command tasks are unsupported",
    "CommandTasks::link-executing-command-task" -> "command tasks are unsupported",
    "CommandTasks::dont-close-over-current-agent" -> "command tasks are unsupported",
    "CommandTasks::concise-syntax-1" -> "command tasks are unsupported",
    "CommandTasks::concise-syntax-2" -> "command tasks are unsupported",
    "CommandTasks::concise-syntax-3" -> "command tasks are unsupported",
    "CommandTasks::concise-syntax-4" -> "command tasks are unsupported",
    "CommandTasks::concise-syntax-5" -> "command tasks are unsupported",
    "CommandTasks::concise-syntax-6" -> "command tasks are unsupported",
    "CommandTasks::concise-syntax-7" -> "command tasks are unsupported",
    "CommandTasks::concise-syntax 8" -> "command tasks are unsupported",
    "CommandTasks::the-killing-task" -> "command tasks are unsupported",
    "CommandTasks::empty-command-task-1" -> "command tasks are unsupported",
    "CommandTasks::empty-command-task-2" -> "command tasks are unsupported",
    "CommandTasks::command-task-body-may-have-different-agent-type" -> "command tasks are unsupported",
    "CommandTasks::command-task-body-gets-agent-type-check" -> "command tasks are unsupported",
    "CommandTasks::command-task-closes-over-let-inside-task-of-different-agent" -> "command tasks are unsupported"
  )
}

class TortoiseFixture(name: String)
extends AbstractFixture {

  override def defaultDimensions = api.WorldDimensions.square(0)
  val rhino = new org.nlogo.tortoise.rhino.Rhino
  var program: api.Program = api.Program.empty
  var procs: ProceduresMap = NoProcedures

  override def declare(source: String, dimensions: api.WorldDimensions) {
    val (js, p, m) =
      try Compiler.compileProcedures(source, dimensions = dimensions)
      catch catcher
    program = p
    procs = m
    rhino.eval(js)
  }

  override def readFromString(literal: String): AnyRef =
    try rhino.eval(Compiler.compileReporter(literal))
    catch catcher

  override def open(path: String) = ???

  override def open(model: headless.ModelCreator.Model) {
    declare(model.code)
  }

  override def runCommand(command: Command, mode: TestMode) {
    val wrappedCommand = command.kind match {
      case api.AgentKind.Observer =>
        command.command
      case api.AgentKind.Turtle =>
        "ask turtles [ " + command.command + "\n]"
      case api.AgentKind.Patch =>
        "ask patches [ " + command.command + "\n]"
      case api.AgentKind.Link =>
        "ask links [ " + command.command + "\n]"
    }
    def js = Compiler.compileCommands(wrappedCommand, procs, program)
    command.result match {
      case Success(_) =>
        try rhino.run(js)
        catch catcher
      case CompileError(msg) =>
        expectCompileError(js, msg)
      case r =>
        cancel("unknown result type: " + r.getClass.getSimpleName)
    }
  }

  private def expectCompileError(js: => String, msg: String) {
    try {
      try js catch catcher
      fail("no CompilerException occurred")
    }
    catch {
      case ex: api.CompilerException =>
        assertResult(msg)(ex.getMessage)
    }
  }

  override def runReporter(reporter: Reporter, mode: TestMode) {
    def js = Compiler.compileReporter(reporter.reporter, procs, program)
    reporter.result match {
      case Success(expectedResult) =>
        val actualResult =
          try rhino.eval(js)
          catch catcher
        checkResult(mode, reporter.reporter, expectedResult, actualResult)
      case CompileError(msg) =>
        expectCompileError(js, msg)
      case r =>
        cancel("unknown result type: " + r.getClass.getSimpleName)
    }
  }

  // kludginess ahead - ST 8/28/13

  val cancelers = Seq(
    "unknown primitive: ",
    "unknown settable: ",
    "unknown language feature: ")

  val catcher: PartialFunction[Throwable, Nothing] = {
    case ex: IllegalArgumentException
          if cancelers.exists(ex.getMessage.startsWith) =>
        cancel(ex.getMessage)
  }

}
