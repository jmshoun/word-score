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
  val usage =
    """
       |word-score: Find the set of letters that optimizes the sum of scores of words
       |that can be spelled from the set of letters.

       |Usage: word-score [--size num] [--scrabble OR --boggle OR --wwf]
       |   [--words OR --words-points] DICTIONARY

       |Arguments:
       |   --size: The size of the set of letters. Runtime is exponential in this
       |       argument, so be prepared to wait a while for values greater than 7 or so.
       |   --scrabble: Score each word as it would be scored if it were the first play
       |       in a game of Scrabble.
       |   --boggle: Score each word according to its values in a game of Boggle.
       |   --wwf: Score each word as it would be scored if it were the first play in a
       |       game of Words With Friends.
       |   --words: Print a newline-separated list of all of the valid words that can be
       |       spelled from the optimal set.
       |   --words-points: Print a newline-separated list of all of the valid words that
       |       can be spelled, along with the number of points each word is worth.
       |   DICTIONARY: Path to a newline-separated list of words. Any words that are not
       |       composed completely of lowercase letters (i.e., any words with capitals,
       |       apostrophes, spaces, hyphens, or numerals) will be automatically
       |       discarded.
    """.stripMargin

  def main(args: Array[String]) = {
    // Parse arguments
    val argList = args.toList
    if (argList.isEmpty) {
      println(usage)
      System.exit(0)
    }
    val options = parseOptions(argList)
    if (!options.contains('dictFile)) {
      println("Dictionary file must be supplied!")
      System.exit(1)
    }

    // Best best set and valid of valid words
    val wordList = loadWordList(options('dictFile).asInstanceOf[String],
      options('pointsMethod).asInstanceOf[Symbol])
    val bestSet = BestSet.findBestSet(wordList, options('maxSize).asInstanceOf[Int])
    val validWords = wordList
      .filter(_.matchesLetterSet(bestSet))
      .filter(_.points > 0)

    // Print the results
    println("Best set: " + bestSet)
    println("Point count: " + validWords.map(_.points).sum)
    println("Word count: " + validWords.size)
    if (options('showWords).asInstanceOf[Boolean]) {
      printWordList(validWords, options('showPoints).asInstanceOf[Boolean])
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
  def printWordList(wordList: List[Word], showPoints: Boolean): Unit = {
    println("Words:")
    println("====================")
    for (word <- wordList) {
      if (showPoints) print(f"${word.points}%3d | ")
      println(word.spelling)
    }
  }
}
