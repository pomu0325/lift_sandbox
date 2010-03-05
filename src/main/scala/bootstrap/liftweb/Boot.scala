package bootstrap.liftweb

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._

/**
  * A class that's instantiated early and run.  It allows the application
  * to modify lift's environment
  */
class Boot {
  def boot {
	logTime("boot"){
	    // where to search snippet
	    LiftRules.addToPackages("net.pomu.lift_sandbox")

	    // Build SiteMap
	    val entries = Menu(Loc("Home", List("index"), "Home")) ::
				Menu(Loc("LiftForm", List("liftForm"), "LiftForm")) ::
				Menu(Loc("MyForm", List("myForm"), "MyForm")) ::
				Menu(Loc("LiftSayHello", List("liftSayHello"), "LiftSayHello", Hidden)) ::
				Menu(Loc("MySayHello", List("mySayHello"), "MySayHello", Hidden)) ::
				Nil
	    LiftRules.setSiteMap(SiteMap(entries:_*))
    }
  }
}

