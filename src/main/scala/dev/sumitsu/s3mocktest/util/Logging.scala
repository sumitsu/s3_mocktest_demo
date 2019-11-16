package dev.sumitsu.s3mocktest.util

import org.apache.log4j.{LogManager, Logger}

trait Logging {
  @transient protected val log: Logger = LogManager.getLogger(getClass.getName.stripSuffix("$"))
}
