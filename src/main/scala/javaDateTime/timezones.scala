package javaDateTime

import java.time._

/**
  * List all available ZoneIds 
  */
object timezones extends App {
  ZoneId.getAvailableZoneIds.toArray.toList.foreach(println(_))
}
