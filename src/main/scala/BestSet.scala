object BestSet {
  private val firstLetter = 'a'
  private val lastLetter = 'z'

  def findBestSet(words: List[Word], letters: Int): (List[Char], Int) = {
    val (bestSet, bestCount) = iterateBestSet(letters, words, List[Char]())
    bestSet.reverse
  }

  def iterateBestSet(lettersLeft: Int, words: List[Word], currentSet: List[Char]): (List[Char], Int) = {
    val currentLetter = if (currentSet.nonEmpty) currentSet.head else firstLetter
    val validWords = words.filter(word => word.residue.size <= lettersLeft)
    if (validWords.isEmpty) {
      (currentSet, 0)
    } else if (lettersLeft == 1) {
      val (lastLetter, numPoints) = bestSetLastLetter(validWords)
      (lastLetter :: currentSet, numPoints)
    } else {
      var bestPoints = 0
      var bestSet = List[Char]()
      var currentWords = validWords
      for (letter <- currentLetter to lastLetter) {
        currentWords = currentWords.filter(_.residue.head >= letter)
        if (totalPoints(currentWords) > bestPoints) {
          val (nextWords, matchedWords) = currentWords
            .map(_.matchLetter(letter))
            .partition(_.residue.nonEmpty)
          val (newSet, newPoints) = iterateBestSet(lettersLeft - 1, nextWords, letter :: currentSet)
          if (newPoints + totalPoints(matchedWords) > bestPoints) {
            bestPoints = newPoints + totalPoints(matchedWords)
            bestSet = newSet
          }
        }
      }
      (bestSet, bestPoints)
    }
  }

  def bestSetLastLetter(words: List[Word]): (Char, Int) = {
    val lastLetterFrequency = words
      .groupBy(_.residue.head)
      .mapValues(totalPoints)
    lastLetterFrequency.maxBy(_._2)
  }

  def totalPoints(words: List[Word]): Int = words.map(_.points).sum
}
