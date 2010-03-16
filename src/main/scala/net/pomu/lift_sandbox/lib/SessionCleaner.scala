/*
 * SessionCleaner.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.pomu.lift_sandbox.lib

import net.liftweb.http._
import net.liftweb.common._
import com.google.appengine.api.datastore._

object SessionCleaner {
	implicit def j2s[A](j: java.util.Iterator[A]) = new scala.collection.jcl.MutableIterator.Wrapper[A](j)
	private lazy val DSS = DatastoreServiceFactory.getDatastoreService

	def execute(): Box[LiftResponse] = {
		var count = 0
		try {
			val q = new Query("_ah_SESSION")
			q.addFilter("_expires", Query.FilterOperator.LESS_THAN_OR_EQUAL, System.currentTimeMillis)
			DSS.prepare(q).asIterator.foreach(e => {DSS.delete(e.getKey); count = count + 1})
		} finally {
			println(count + " sessions deleted.")
		}
		Full(OkResponse())
	}
}
