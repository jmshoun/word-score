/** Computes the number of points associated with a given word.
  *
  * Main entry point to the object is the compute function.
  */
object Points {
  // Maps of points per letter.
  private val scrabblePoints = Map[Char, Int](
    'a' -> 1, 'b' -> 3, 'c' -> 3, 'd' -> 2, 'e' -> 1, 'f' -> 4, 'g' -> 2, 'h' -> 4, 'i' -> 1,
    'j' -> 8, 'k' -> 5, 'l' -> 1, 'm' -> 3, 'n' -> 1, 'o' -> 1, 'p' -> 3, 'q' -> 10, 'r' -> 1,
    's' -> 1, 't' -> 1, 'u' -> 1, 'v' -> 4, 'w' -> 4, 'x' -> 8, 'y' -> 4, 'z' -> 10
  )
  private val wordsWithFriendsPoints = Map[Char, Int](
    'a' -> 1, 'b' -> 4, 'c' -> 4, 'd' -> 2, 'e' -> 1, 'f' -> 4, 'g' -> 3, 'h' -> 3, 'i' -> 1,
    'j' -> 10, 'k' -> 5, 'l' -> 2, 'm' -> 4, 'n' -> 2, 'o' -> 1, 'p' -> 4, 'q' -> 10, 'r' -> 1,
    's' -> 1, 't' -> 1, 'u' -> 2, 'v' -> 5, 'w' -> 4, 'x' -> 8, 'y' -> 3, 'z' -> 10
  )

  /** Compute the point value of a given word.
    *
    * @param word Word to score.
    * @param method Scoring method to use; one of 'boggle, 'scrablle, or 'wordsWithFriends.
    * @return The point value of the word.
    */
  def compute(word: String, method: Symbol): Int = {
    method match {
      case 'boggle => computeBogglePoints(word)
      case 'scrabble => computeScrabblePoints(word)
      case 'wordsWithFriends => computeWordsWithFriendsPoints(word)
      case _ => 1
    }
  }

  private def computeBogglePoints(word: String): Int = {
    // Boggle points are based purely on word length
    word.length match {
      case x if 0 to 2 contains x => 0
      case 3 => 1
      case 4 => 1
      case 5 => 2
      case 6 => 3
      case 7 => 5
      case _ => 11
    }
  }

  private def computeScrabblePoints(word: String): Int = {
    val letterPoints = word.toList.map(scrabblePoints(_))
    // There are double letter squares 4 away from the starting square.
    val possibleDoubles = 0 :: letterPoints.drop(4) ++ letterPoints.dropRight(4)
    val bingoBonus = if (word.size == 7) 50 else 0
    letterPoints.sum + possibleDoubles.max + bingoBonus
  }

  private def computeWordsWithFriendsPoints(word: String): Int = {
    val letterPoints = word.toList.map(wordsWithFriendsPoints(_))
    // Words with Friends has double-word instead of the double-letter on the Scrabble board
    val doubleWord = if (word.size >= 5) 2 else 1
    val bingoBonus = if (word.size == 7) 35 else 0
    letterPoints.sum * doubleWord + bingoBonus
  }
}

/** Represents a word that is being matched to a set of letters.
  *
  * @param spelling The full spelling of the word.
  * @param points The point value of the word.
  * @param residue An alphabetically sorted list of letters that have not been matched
  *                to letters in a set of letters.
  */
class Word(val spelling: String, val points: Int, val residue: List[Char]) {
  /** Alternate contsructor with default point values. */
  def this(spelling: String) = {
    this(spelling, 1, spelling.toList.sorted)
  }
  /** Alternate constructor that will compute point values from the given method. */
  def this(spelling: String, pointsMethod: Symbol) = {
    this(spelling, Points.compute(spelling, pointsMethod), spelling.toList.sorted)
  }

  /** Try to match a single letter from a set to a word. If there is a match, update the residue. */
  def matchLetter(letter: Char): Word = {
    if (letter == residue.head) {
      new Word(spelling, points, residue.tail)
    } else {
      this
    }
  }

  /** Test if a word can be spelled from a set of letters. */
  def matchesLetterSet(letterSet: List[Char]): Boolean = {
    // Base cases
    if (residue.isEmpty) {
      true
    } else if (letterSet.isEmpty) {
      false
    // Head comparison cases
    } else if (residue.head < letterSet.head) {
      false
    } else if (residue.head == letterSet.head) {
      matchLetter(letterSet.head).matchesLetterSet(letterSet.tail)
    } else {
      matchesLetterSet(letterSet.tail)
    }
  }
}
