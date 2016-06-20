package com.athlinks

import java.util.concurrent.atomic.AtomicLong

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.UserRepresentation

import scala.util.Random

class CreateUsersThread() extends Thread with LazyLogging {
  val workerID = CreateUsersThread.workerID.incrementAndGet()

  // Parse the config
  val config = ConfigFactory.load()

  val serverUrl = config.getString("serverUrl")
  val realm = config.getString("realm")
  val clientId = config.getString("clientId")
  val clientSecret = config.getString("clientSecret")
  val username = config.getString("username")
  val password = config.getString("password")

  // Configure the keycloak client
  val resteasyClient = new ResteasyClientBuilder()
    .maxPooledPerRoute(1)
    .connectionPoolSize(1)
    .build()

  logger.info(s"Connecting to keycloak at : $serverUrl")

  val keycloak = KeycloakBuilder.builder()
    .serverUrl(serverUrl)
    .realm(realm)
    .username(username)
    .password(password)
    .clientId(clientId)
    .clientSecret(clientSecret)
    .resteasyClient(resteasyClient)
    .build()

  val realmUsers = keycloak.realm(realm).users()

  override def run(): Unit = {
    logger.info(s"Starting worker thread $workerID")
    while(true) {
      // Create a random user
      val random = Random.alphanumeric.take(16).mkString

      val userRep = new UserRepresentation()
      userRep.setEmail(random)
      userRep.setUsername(random)
      userRep.setFirstName(random)
      userRep.setLastName(random)
      userRep.setEnabled(true)
      userRep.setEmailVerified(true)

      val response = realmUsers.create(userRep)
      val responseStatus = response.getStatus

      response.close()

      if (responseStatus < 200 || responseStatus >= 300) {
        logger.error("Create failed")
      }

      // Track some stats
      val created = CreateUsersThread.totalCreated.incrementAndGet()
      if (created % 1000 == 0) {
        val currTime = System.currentTimeMillis()
        val timeDiff = currTime - CreateUsersThread.lastThousand.get()
        val perSecond = 1000f / (timeDiff / 1000f)
        logger.info(s"Total created : $created.  Per second : ${Math.round(perSecond)}")
        CreateUsersThread.lastThousand.set(currTime)
      }
    }
  }
}

// Companion object with some tracking variables
object CreateUsersThread {
  val workerID = new AtomicLong(0)
  val totalCreated = new AtomicLong(0)
  val lastThousand = new AtomicLong(System.currentTimeMillis())
}