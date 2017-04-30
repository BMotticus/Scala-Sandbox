import java.time._
import org.joda.time.{LocalDate => JodaLocalDate, DateTime}

val x = DateTime.now()
x.getMonthOfYear

val zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"))
val localDateTime = LocalDateTime.now
