object Points {
  val scrabblePoints = Map[Char, Int](
    'a' -> 1, 'b' -> 3, 'c' -> 3, 'd' -> 2, 'e' -> 1, 'f' -> 4, 'g' -> 2, 'h' -> 4, 'i' -> 1,
    'j' -> 8, 'k' -> 5, 'l' -> 1, 'm' -> 3, 'n' -> 1, 'o' -> 1, 'p' -> 3, 'q' -> 10, 'r' -> 1,
    's' -> 1, 't' -> 1, 'u' -> 1, 'v' -> 4, 'w' -> 4, 'x' -> 8, 'y' -> 4, 'z' -> 10
  )

  def compute(word: String, method: Symbol): Int = {
    method match {
      case 'boggle => computeBogglePoints(word)
      case 'scrabble => computeScrabblePoints(word)
      case _ => 1
    }
  }

  def computeBogglePoints(word: String): Int = {
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

  def computeScrabblePoints(word: String): Int = {
    val letterPoints = word.toList.map(scrabblePoints(_))
    letterPoints.sum
  }
}


class Word(val spelling: String, val points: Int, val residue: List[Char]) {
  def this(spelling: String) = {
    this(spelling, 1, spelling.toList.sorted)
  }

  def this(spelling: String, pointsMethod: Symbol) = {
    this(spelling, Points.compute(spelling, pointsMethod), spelling.toList.sorted)
  }

  def matchLetter(letter: Char): Word = {
    if (letter == residue.head) {
      new Word(spelling, points, residue.tail)
    } else {
      this
    }
  }

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
