/** Finds the set of letters that maximizes the sum of points of words
  * that can be spelled from that set.
  */
object BestSet {
  private val firstLetter = 'a'
  private val lastLetter = 'z'

  /** Find the optimal set of letters to maximize achievable points from a list of words.
    *
    * @param words List of valid words.
    * @param letters The number of letters in the final set.
    * @return The optimal set of letters.
    */
  def findBestSet(words: List[Word], letters: Int): List[Char] = {
    val (bestSet, bestCount) = iterateBestSet(letters, words, List[Char]())
    bestSet.reverse
  }

  /** Find the next letter in the optimal letter set. */
  private def iterateBestSet(lettersLeft: Int, words: List[Word], currentSet: List[Char]): (List[Char], Int) = {
    // The letter set is built in alphabetical order, so the next letter must be >= the most recent one.
    val currentLetter = if (currentSet.nonEmpty) currentSet.head else firstLetter
    // If a word's residue is longer than the number of remaning letters, it's unattainable.
    val validWords = words.filter(word => word.residue.size <= lettersLeft)
    if (validWords.isEmpty) {
      (currentSet, 0)
    } else if (lettersLeft == 1) {
      val (lastLetter, numPoints) = bestSetLastLetter(validWords)
      (lastLetter :: currentSet, numPoints)
    } else {
      // Recursively try sets starting with all subsequent letters.
      var bestPoints = 0
      var bestSet = List[Char]()
      var currentWords = validWords
      for (letter <- currentLetter to lastLetter) {
        // Residues are alphabetical, so if the next set letter is after the next residue letter, the word
        // will never be spelled.
        currentWords = currentWords.filter(_.residue.head >= letter)
        if (totalPoints(currentWords) > bestPoints) {
          // Match the candidate letter to the words and separate words that have been spelled.
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

  /** Find the optimum last letter in a set given all the other letters. */
  private def bestSetLastLetter(words: List[Word]): (Char, Int) = {
    val lastLetterFrequency = words
      .groupBy(_.residue.head)
      .mapValues(totalPoints)
    lastLetterFrequency.maxBy(_._2)
  }

  /** Compute the total points in a list of words. */
  private def totalPoints(words: List[Word]): Int = words.map(_.points).sum
}
