// How to create simple dates and intervals
// The java.time package includes many new classes to help you: 
// LocalDate, LocalTime, LocalDateTime, Instant, Duration, and Period.
import java.time._
import java.time.temporal.ChronoField
import java.time.format._
 
/** Using LocalDate
An instance of `LocalDate` is an immutable object representing a plain date 
without the time of day. It doesn’t carry any info about the time zone.

Create a LocalDate instance using the `of(...)` static factory method. 
A LocalDate instance provides many methods to read its most commonly 
used values such as `year`, `month`, `day of the week`, and so on.
*/
val date: LocalDate = LocalDate.of(2016, 7, 26)
val year: Int = date.getYear
val month: Month = date.getMonth
val day: Int = date.getDayOfMonth
val dayOfWeek: DayOfWeek = date.getDayOfWeek
val lengthOfMonth: Int = date.lengthOfMonth()
val isLeapYear: Boolean = date.isLeapYear

// Get the current date from the system clock using the now() factory method:
val today: LocalDate = LocalDate.now() //all other classes have a similar factory method

/** Using ChronoField
You can also access the same information by passing a `TemporalField` to the 
`get` method. The `TemporalField` is an `interface` defining how to access 
the value of a specific field of a `temporal object`.

The `ChronoField` enumeration implements `TemporalField` interface, so you can 
conveniently use an element of that enumeration with the get method.
*/
 
val currentYear: Int = today.get(ChronoField.YEAR)
val currentMonth: Int = today.get(ChronoField.MONTH_OF_YEAR)
val currentDay: Int = today.get(ChronoField.DAY_OF_MONTH)

/** Using LocalTime
You can create instances of `LocalTime` using two overloaded static factory 
methods named `of(...)`. The first one accepts an hour and a minute and the 
second one also accepts a second. Just like the LocalDate class, 
the LocalTime class provides some getter methods to access its values. 
*/
val currentTime: LocalTime = LocalTime.now
val time: LocalTime = LocalTime.of(13, 45, 20)
val hour: Int = time.getHour
val minute: Int = time.getMinute
val second: Int = time.getSecond

/** Using `LocalDate.parse` and `LocalTime.parse` 
Both `LocalDate` and `LocalTime` can be created by `parsing a String` 
representing them. You can achieve this using their `parse` static methods:
*/

val dateParse: LocalDate = LocalDate.parse("2016-07-26")
val timeParse: LocalTime = LocalTime.parse("13:45:20")
val timeNoSeconds: LocalTime = LocalTime.parse("13:45")

/** Using DateTimeFormatter
Note: It’s possible to pass a `DateTimeFormatter` to the `parse` method.

An instance of this class specifies how to format a date and/or a time object.
Also note that these `parse` methods both throw a `DateTimeParseException`.
*/
val localDateParse: LocalDate = LocalDate.from( DateTimeFormatter.ISO_LOCAL_DATE.parse("2016-02-03") )
val localDateFormat: String = DateTimeFormatter.ISO_LOCAL_DATE.format(localDateParse)

/** Using LocalDateTime
The composite class called `LocalDateTime` pairs a `LocalDate` and a `LocalTime`. 
It represents both a date and a time, <b>without a time zone</b>, and can be created 
either DIRECTLY or by COMBINING a date and time. 
*/

//Creating a LocalDateTime DIRECTLY 
val dtDirectly: LocalDateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20)
//Creating a LocalDateTime by COMBINING a date and a time
val dtCombination1: LocalDateTime = LocalDateTime.of(date, time)

/** Using `atTime()` and `atDate()` methods
It’s possible to create a `LocalDateTime` by passing a `time` to a `LocalDate`, 
or conversely a `date` to a `LocalTime`, using respectively their 
`atTime()` or `atDate()` methods.
 */
val dtCombination2: LocalDateTime = date.atTime(13, 45, 20)
val dtCombination3: LocalDateTime = date.atTime(time)
val dtCombination4: LocalDateTime = time.atDate(date)

/**
  * You can also EXTRACT the `LocalDate` or `LocalTime` component from a `LocalDateTime` 
  * using the `toLocalDate` and `toLocalTime` methods:
  */
val localDate: LocalDate = dtDirectly.toLocalDate
val localTime: LocalTime = dtCombination1.toLocalTime

val jsDateFormat = DateTimeFormatter.ofPattern("'new Date('yyyy, M'-1', d, H, m, s)").format(dtDirectly)