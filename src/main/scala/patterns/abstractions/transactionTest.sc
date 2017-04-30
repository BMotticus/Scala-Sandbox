import patterns.abstractions.{FileTransactor, MemoryTransactor, FileDatabase, MemoryDatabase, Persister, History}

val fileStringtransactor = new FileTransactor[String]
val memoryInttransactor = new MemoryTransactor[Int]

fileStringtransactor.transact("Something")
fileStringtransactor.transact("Something else")

memoryInttransactor.transact(100)
memoryInttransactor.transact(123)


