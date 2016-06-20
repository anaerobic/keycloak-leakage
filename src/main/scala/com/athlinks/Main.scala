package com.athlinks

import com.typesafe.scalalogging.LazyLogging

object Main extends App with LazyLogging {
  val threadCount = Runtime.getRuntime.availableProcessors()

  val threads = for {
    t <- 0 until threadCount
    thread = new CreateUsersThread()
  } yield thread

  threads.foreach(_.start())
  threads.foreach(_.join())
}
