package dev.sumitsu.s3mocktest.util

import org.apache.log4j.{LogManager, Logger}

trait Logging {
  protected val LogClassName: String = getClass.getName.stripSuffix("$")
  @transient protected val log: Logger = LogManager.getLogger(LogClassName)
}
