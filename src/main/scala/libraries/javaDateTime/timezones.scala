package libraries.javaDateTime

import java.time.ZoneId

/**
  * List all available ZoneIds 
  */
object timezones extends App {
  ZoneId.getAvailableZoneIds.toArray.toList.foreach(println(_))
}
