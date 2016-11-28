import java.time._
import java.time.format._
import java.time.temporal.TemporalAdjusters._
import java.time.temporal._

lazy val dateHourFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH")
val datetime = LocalDateTime.now()
val birthday = LocalDate.of(1988,2,3).atTime(LocalTime.now())
val dateHourStr = dateHourFormat.format(datetime)

val instant = LocalDateTime.from(
  dateHourFormat.parse(dateHourStr)
).atOffset(ZoneOffset.UTC).toInstant

val endOfMonth = LocalDateTime.now `with` TemporalAdjusters.lastDayOfMonth()

Period.between(
  LocalDate.from(birthday `with` lastDayOfMonth()),
  LocalDate.from(datetime `with` firstDayOfMonth())
).toTotalMonths

val duration = Duration.between(LocalTime.MIDNIGHT, LocalTime.of(13,9))
val hours = duration.toHours
val minutes = duration.minusHours(hours).toMinutes

val time = LocalTime.of(duration.toHours.toInt, duration.minusHours(duration.toHours).toMinutes.toInt)

// format instant
DateTimeFormatter.ISO_INSTANT.format(instant)

def roundZonedDateTime (dt: ZonedDateTime, minutesIncrement: Int): ZonedDateTime = {
  import java.time.temporal.ChronoField
  val minuteOfHour = dt.get(ChronoField.MINUTE_OF_HOUR)
  val ratio = minuteOfHour.toDouble / minutesIncrement.toDouble
  val round = Math.round(ratio);
  val i = round * minutesIncrement.toLong

  if (i == 60L) {
    dt.plusHours(1L).`with`(ChronoField.MINUTE_OF_HOUR, 0L)
  } else {
    dt.`with`(ChronoField.MINUTE_OF_HOUR, i)
  }
}
 
// 1 - 14 = 19:00
//15 - 43 = 19:30 
//45 - 59 = 20:00
roundZonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")).`with`(ChronoField.MINUTE_OF_HOUR, 59), 30)

val x = LocalDateTime.from(LocalDateTime.now `with` TemporalAdjusters.lastDayOfMonth()).atZone(ZoneId.of("UTC")).toInstant

val startDate = birthday.atZone(ZoneId.of("UTC"))

val myLife = Duration.between(
startDate, 
ZonedDateTime.from( ZonedDateTime.now(startDate.getZone) `with` TemporalAdjusters.lastDayOfMonth() ) 
)
