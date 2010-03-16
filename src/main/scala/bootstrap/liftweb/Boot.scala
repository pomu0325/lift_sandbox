package bootstrap.liftweb

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider.HTTPSession
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import com.google.appengine.api.datastore._
import java.io._
import net.pomu.lift_sandbox.lib._

/**
  * A class that's instantiated early and run.  It allows the application
  * to modify lift's environment
  */
class Boot {
	lazy val DSS = DatastoreServiceFactory.getDatastoreService()
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
				Menu(Loc("SessionTest", List("sessionTest"), "SessionTest")) ::
				Nil
	    LiftRules.setSiteMap(SiteMap(entries:_*))

			// Delete expired session entity from datastore.
			LiftRules.statelessDispatchTable.append {
				case Req("cron" :: "sessionCleaner" :: Nil, _, _) => () => SessionCleaner.execute()
			}

			/* test code for LiftSession on GAE
			// LiftSession serialization
			val sessionKey = "$lift_session_key$"
			LiftRules.sessionCreator = {case (httpSession, contextPath) =>
				(try {
					//  deserialize session from datastore
					println("try deserialize session from datastore")
					val id = httpSession.sessionId
					println(id)
					val s = DSS.get(KeyFactory.createKey("_ah_SESSION", "_ahs" + id))
					println(s)
					val b = s.getProperty("_values")
					println(b)
					b match {
						case b: Blob =>
							val ois = new ObjectInputStream(new ByteArrayInputStream(b.getBytes));
							val o = ois.readObject()
							println(o.getClass)
							o match {	//
								case m: java.util.Map[_, _] =>
									m.get(sessionKey) match {
										case s: GAELiftSession =>
											println("is GAELiftSession")
											Full(s)
											//val session = new LiftSession("", id, Full(s))
											//println(session)
										case x => println(x)
											Empty
								}
							}
					}
				} catch {
					case e => e.printStackTrace()
					// if not found, create new session
					println("session not found on datastore")
					Empty
				}) openOr new GAELiftSession(contextPath, httpSession.sessionId, Full(httpSession))
			}

			// serialize liftSession into datastore
			def serializeSession(s: LiftSession, req: Req, res: Box[LiftResponse]): Unit = _serializeSession(req, res)
			def _serializeSession(req: Req, res: Box[LiftResponse]): Unit = logTime("onEndServicing"){
				try {
				S.session.foreach(s => {
					val id = s.uniqueId
					println("storing liftsession into datastore:" + id + "=" + s)
					val e = DSS.get(KeyFactory.createKey("_ah_SESSION", "_ahs" + id))
					println("got entity " + e)
					val baos = new ByteArrayOutputStream()
					println("new baos")
					val oos = new ObjectOutputStream(baos)
					println("new oos")
					oos.writeObject(s)
					println("written object")
					e.setProperty(sessionKey, new Blob(baos.toByteArray))
					println("set property")
					DSS.put(e)
					println("succeed to put")
				})
				} catch {
					case e => println(e.getMessage); println(e.getClass); e.printStackTrace
				}
			}

			LiftSession.onEndServicing = serializeSession _ :: LiftSession.onEndServicing
			LiftRules.onEndServicing.append(_serializeSession _)
			*/
    }
  }
}
