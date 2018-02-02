import scala.io.Source

object WordScoreApp {
  type OptionMap = Map[Symbol, Any]
  val defaultOptions: OptionMap = Map(
    'maxSize -> 7,
    'showWords -> false,
    'pointsMethod -> 'default
  )

  def main(args: Array[String]) = {
    val argList = args.toList
    val options = parseOptions(argList)
    if (!options.contains('dictFile)) {
      println("Dictionary file must be supplied!")
      System.exit(1)
    }

    val wordList = loadWordList(options('dictFile).asInstanceOf[String],
      options('pointsMethod).asInstanceOf[Symbol])
    val (bestSet, bestPoints) = BestSet.findBestSet(wordList, options('maxSize).asInstanceOf[Int])
    val validWords = wordList
      .filter(_.matchesLetterSet(bestSet))
      .filter(_.points > 0)
    println("Best set: " + bestSet)
    println("Point count: " + bestPoints)
    println("Word count: " + validWords.size)
    if (options('showWords).asInstanceOf[Boolean]) {
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
        case "--boggle" :: tail => parseNextOption(map ++ Map('pointsMethod -> 'boggle), tail)
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

  def loadWordList(filename: String, pointsMethod: Symbol = 'default): List[Word] = {
    val source = Source.fromFile(filename)
    val words = source.getLines.toList
    val validWords = words.filter(word => word.matches("[a-z]+"))
    validWords.map(word => new Word(word, pointsMethod))
  }

  def printWordList(wordList: List[Word]): Unit = {
    for (word <- wordList) println(word.spelling)
  }
}
