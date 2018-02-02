object BestSet {
  private val firstLetter = 'a'
  private val lastLetter = 'z'

  def findBestSet(words: List[Word], letters: Int): (List[Char], Int) = {
    val (bestSet, bestCount) = iterateBestSet(letters, words, List[Char]())
    (bestSet.reverse, bestCount)
  }

  def iterateBestSet(lettersLeft: Int, words: List[Word], currentSet: List[Char]): (List[Char], Int) = {
    val currentLetter = if (currentSet.nonEmpty) currentSet.head else firstLetter
    val validWords = words.filter(word => word.residue.size <= lettersLeft)
    if (validWords.isEmpty) {
      (currentSet, 0)
    } else if (lettersLeft == 1) {
      val (lastLetter, numWords) = bestSetLastLetter(validWords)
      (lastLetter :: currentSet, numWords)
    } else {
      var bestCount = 0
      var bestSet = List[Char]()
      var currentWords = validWords
      for (letter <- currentLetter to lastLetter) {
        currentWords = currentWords.filter(_.residue.head >= letter)
        if (currentWords.size > bestCount) {
          val (nextWords, matchedWords) = currentWords
            .map(_.matchLetter(letter))
            .partition(_.residue.nonEmpty)
          val (newSet, newCount) = iterateBestSet(lettersLeft - 1, nextWords, letter :: currentSet)
          if (newCount + matchedWords.size > bestCount) {
            bestCount = newCount + matchedWords.size
            bestSet = newSet
          }
        }
      }
      (bestSet, bestCount)
    }
  }

  def bestSetLastLetter(words: List[Word]): (Char, Int) = {
    val lastLetterFrequency = words
      .map(_.residue.head)
      .groupBy(identity)
      .mapValues(_.size)
    lastLetterFrequency.maxBy(_._2)
  }
}
