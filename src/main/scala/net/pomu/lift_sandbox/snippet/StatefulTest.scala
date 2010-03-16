/*
 * StatefulTest.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.pomu.lift_sandbox.snippet

import scala.xml._
import net.liftweb.http._
import net.liftweb.util._
import Helpers._
import S._

class StatefulTest extends StatefulSnippet {
	val dispatch: DispatchIt = {
		case "liftForm" => liftForm _
		case "myForm" => myForm _
		case "mySayHello" => mySayHello _
	}

	def liftForm(in: NodeSeq): NodeSeq = {
		println(this)
		var name = ""
		def sayHello() = {
			println(this)
			S.notice("Hello, " + name + ". I'm " + this)
			redirectTo("/liftSayHello")
		}
		bind("e", in,
			"instance" -> Text("I'm " + this),
			"input" -> SHtml.text(name, i => name = i),
			"submit" -> SHtml.submit("say hello", sayHello)
		)
	}

	def myForm(in: NodeSeq): NodeSeq = {
		println(this)
		S.fmapFunc((a: List[String]) => {registerThisSnippet()})(key => {
			bind("e", in,
				"key" -> <input type="hidden" name={key} value="_"/>,
				"instance" -> Text("I'm " + this),
				"input" -> <input type="text" name="name"/>,
				"submit" -> <input type="submit" value="say hello"/>
			)
		})
	}

	def mySayHello(in: NodeSeq): NodeSeq = {
		println(this)
		val name = S.param("name") openOr ""
		S.notice("Hello, " + name + ". I'm " + this)
		in
	}
}
