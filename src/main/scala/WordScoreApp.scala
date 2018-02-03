import scala.io.Source

/** Primary entry point for the application. */
object WordScoreApp {
  type OptionMap = Map[Symbol, Any]
  val optionDefaults: OptionMap = Map(
    'maxSize -> 7,
    'showWords -> false,
    'showPoints -> false,
    'pointsMethod -> 'default
  )

  def main(args: Array[String]) = {
    // Parse arguments
    val argList = args.toList
    val options = parseOptions(argList)
    if (!options.contains('dictFile)) {
      println("Dictionary file must be supplied!")
      System.exit(1)
    }

    val wordList = loadWordList(options('dictFile).asInstanceOf[String],
      options('pointsMethod).asInstanceOf[Symbol])
    val bestSet = BestSet.findBestSet(wordList, options('maxSize).asInstanceOf[Int])
    val validWords = wordList
      .filter(_.matchesLetterSet(bestSet))
      .filter(_.points > 0)
    println("Best set: " + bestSet)
    println("Point count: " + validWords.map(_.points).sum)
    println("Word count: " + validWords.size)
    if (options('showWords).asInstanceOf[Boolean]) {
      println("Words:")
      println("====================")
      for (word <- validWords) {
        if (options('showPoints).asInstanceOf[Boolean]) print(word.points + " | ")
        println(word.spelling)
      }
    }
  }

  /** Parses an argument list into a map of options. */
  def parseOptions(argList: List[String]): OptionMap = {
    /** Recursively parse a single option. */
    def parseNextOption(map: OptionMap, argList: List[String]): OptionMap = {
      def isSwitch(s: String) = s(0) == '-'

      argList match {
        // Point scoring methods
        case "--boggle" :: tail => parseNextOption(map ++ Map('pointsMethod -> 'boggle), tail)
        case "--scrabble" :: tail => parseNextOption(map ++ Map('pointsMethod -> 'scrabble), tail)
        case "--wwf" :: tail => parseNextOption(map ++ Map('pointsMethod -> 'wordsWithFriends), tail)
        // Output options
        case "--words" :: tail => parseNextOption(map ++ Map('showWords -> true), tail)
        case "--word-points" :: tail => parseNextOption(map ++ Map('showPoints -> true, 'showWords -> true), tail)
        // Letter set size
        case "--size" :: value :: tail => parseNextOption(map ++ Map('maxSize -> value.toInt), tail)
        // Dictionary file
        case value :: flag :: tail if isSwitch(flag) =>
          parseNextOption(map ++ Map('dictFile -> value), argList.tail)
        case value :: Nil => map ++ Map('dictFile -> value)
        // Base cases
        case Nil => map
        case option :: tail =>
          println("Unknown option " + option)
          map
      }
    }

    parseNextOption(optionDefaults, argList)
  }

  /** Loads a newline-delimited list of lowercase words from a file. Invalid words are filtered. */
  def loadWordList(filename: String, pointsMethod: Symbol = 'default): List[Word] = {
    val source = Source.fromFile(filename)
    val words = source.getLines.toList
    val validWords = words.filter(word => word.matches("[a-z]+"))
    validWords.map(word => new Word(word, pointsMethod))
  }

  /** Prints a list of words. */
  def printWordList(wordList: List[Word]): Unit = {
    for (word <- wordList) println(word.spelling)
  }
}
