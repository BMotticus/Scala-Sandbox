// From a machine point of view, the most natural format to model time is 
// with a SINGLE large number representing a point on a continuous timeline.
// Instant: a date and time for machines
import java.time._
import java.time.temporal._
import java.time.format._

/**
  * `Instant` is for machine use, It's not intended to be read by humans.
  * Convert an Instant to either `ZonedDateTime` or `OffsetDateTime` to make 
  * instances in time readable for humans.
  */
val instant = Instant.now

//Instant to ZonedDateTime
val zonedDateTime = instant.atZone(ZoneId.of("UTC"))

//Instant to OffsetDateTime
val offsetDateTime = instant.atOffset(ZoneOffset.UTC)

/**
From `OffsetDateTime` you can EXTRACT `ZonedDateTime`, 
`LocalDateTime`, `LocalDate`, `LocalTime`, `ZoneOffset`, 
`OffsetTime` and convert to an `Instant` or the number of 
seconds since 1970-01-01T00:00:00Z.
You can also extract DayOfWeek, Month and any other field.
  */
val localDateTime = offsetDateTime.toLocalDateTime
val localDate = offsetDateTime.toLocalDate
val localTime = offsetDateTime.toLocalTime
val offset = offsetDateTime.getOffset
val instant0 = offsetDateTime.toInstant
val epochSecond = offsetDateTime.toEpochSecond
val zonedDateTime0 = offsetDateTime.toZonedDateTime


/**
From `ZonedDateTime` you can EXTRACT `OffsetDateTime`, 
`LocalDateTime`, `LocalDate`, `LocalTime`, `ZoneOffset`, 
`OffsetTime` and convert to an `Instant` or the number of 
seconds since 1970-01-01T00:00:00Z.
You can also extract DayOfWeek, Month and several other fields.
  */
val localDateTime1 = zonedDateTime.toLocalDateTime
val localDate1 = zonedDateTime.toLocalDate
val localTime1 = zonedDateTime.toLocalTime
val zoneId = zonedDateTime.getZone
val offset1 = zonedDateTime.getOffset
val instant1 = zonedDateTime.toInstant
val epochSecond1 = zonedDateTime.toEpochSecond
val offsetDateTime1 = zonedDateTime.toOffsetDateTime

/**
  * You can create an instance of Instant by passing the number of `seconds`
  * to its `ofEpochSecond()` static factory method.
  *
  * In addition, the Instant class supports `nanosecond` precision.
  *
  * The overloaded version of the `ofEpochSecond()` static factory method 
  * that accepts a second argument that’s a nanosecond adjustment.
  *
  * This overloaded version adjusts the nanosecond argument, ensuring that 
  * the stored nanosecond fraction is between 0 and 999,999,999.
  */
// All three return the exact same Instant: 
val i1 = Instant.ofEpochSecond(3)
val i2 = Instant.ofEpochSecond(3, 0)
val i3 = Instant.ofEpochSecond(2, 1000000000)
val i4 = Instant.ofEpochSecond(4, -1000000000) 

/**
  * The Instant class also supports another static factory method named now,
  * which allows you to capture a timestamp of the current moment. 
  *
  * Remember an Instant is intended for use only by a machine. 
  * 
  * You can work with Instants by using the Duration and Period classes.
  */
val startDate: String = "1988-02-03"
val birthday: LocalDate = scala.util.Try(
  LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(startDate))
).toOption.getOrElse(LocalDate.now.`with`(TemporalAdjusters.firstDayOfMonth()))

//Failed parse
val aniversary = scala.util.Try(
  LocalDate.from( DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("") )
).toOption.getOrElse(LocalDate.now)
/**
All the classes you’ve seen so far implement the `Temporal` interface, 
which defines how to read and manipulate the values of an object modeling 
a generic point in time.

You can create a `Duration` between two temporal objects. 
You can COMBINE two temporal instances of the same type.
 */

//Duration from LocalTimes
val time1 = LocalTime.of(6, 30, 0)
val time2 = LocalTime.now
val duration1 = Duration.between(time1, time2)

//Duration from LocalDateTimes
val date1 = LocalDate.of(1988, 2, 3)
val date2 = LocalDate.now
val duration2 = Duration.between(date1.atTime(time1), date2.atTime(time2))

//Duration from Instants
val instant5 =  Instant.now
val duration3 = Duration.between(instant1, instant5)
/**
  * The `Duration` class is used to represent 
  * <b> an amount of time measured in `seconds` and eventually `nanoseconds` </b>
  * Note: You CAN'T pass a `LocalDate` to the `Duration.between()` method. 
  */

/** 
  * The `Period` class is used to represent 
  * <b> an amount of time in terms of `years`, `months`, and `days`. </b>
  * 
  * When you need to model an amount of time in terms of `years`, `months`, and `days`,
  * you can use the `Period` class.
  */
val myLifespan = Period.between(LocalDate.of(1988, 2, 3), LocalDate.now)

val tenDays1 = Period.between(LocalDate.of(2014, 3, 8), LocalDate.of(2014, 3, 18))

/**
Both the `Duration` and `Period` classes have other convenient factory methods
to create instances of them DIRECTLY
 */
val threeMinutes1 = Duration.ofMinutes(3)
val threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES)
val tenDays2 = Period.ofDays(10)
val threeWeeks = Period.ofWeeks(3)
val twoYearsSixMonthsOneDay = Period.of(2, 6, 1)
// Both the `Duration` and `Period` classes share many similar methods.
