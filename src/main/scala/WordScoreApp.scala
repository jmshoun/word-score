import scala.io.Source

object WordScoreApp {
  type OptionMap = Map[Symbol, Any]
  val defaultOptions: OptionMap = Map('maxSize -> 7, 'showWords -> false)

  def main(args: Array[String]) = {
    val argList = args.toList
    val options = parseOptions(argList)
    if (!options.contains('dictFile)) {
      println("Dictionary file must be supplied!")
      System.exit(1)
    }

    val wordList = loadWordList(options('dictFile).asInstanceOf[String])
    val (bestSet, bestCount) = BestSet.findBestSet(wordList, options('maxSize).asInstanceOf[Int])
    println("Best set: " + bestSet)
    println("Word count: " + bestCount)
    if (options('showWords).asInstanceOf[Boolean]) {
      val validWords = wordList.filter(_.matchesLetterSet(bestSet))
      println("Words:")
      println("====================")
      for (word <- validWords) println(word.spelling)
    }
  }

  def parseOptions(argList: List[String]): OptionMap = {
    def parseNextOption(map: OptionMap, argList: List[String]): OptionMap = {
      def isSwitch(s: String) = s(0) == '-'

      argList match {
        case Nil => map
        case "--words" :: tail => parseNextOption(map ++ Map('showWords -> true), tail)
        case "--size" :: value :: tail => parseNextOption(map ++ Map('maxSize -> value.toInt), tail)
        case value :: flag :: tail if isSwitch(flag) =>
          parseNextOption(map ++ Map('dictFile -> value), argList.tail)
        case value :: Nil => map ++ Map('dictFile -> value)
        case option :: tail =>
          println("Unknown option " + option)
          map
      }
    }

    parseNextOption(defaultOptions, argList)
  }

  def loadWordList(filename: String): List[Word] = {
    val source = Source.fromFile(filename)
    val words = source.getLines.toList
    val validWords = words.filter(word => word.matches("[a-z]+"))
    validWords.map(word => new Word(word))
  }

  def printWordList(wordList: List[Word]): Unit = {
    for (word <- wordList) println(word.spelling)
  }
}
