class Word(val spelling: String) {
  val points = 1
}


class WordResidue(val word: Word) {
  private val charOffset = 'a'.toInt
  private val residueLetters = initializeResidueLetters(word)
  val residueSize: Int = residueLetters.sum

  def residueValue(letter: Char): Int = {
    residueLetters(letter.toInt - charOffset)
  }

  private def initializeResidueLetters(word: Word): Vector[Int] = {
    var residue = Vector.fill(26)(0)
    for (char <- word.spelling) {
      val charIndex = char.toInt - charOffset
      residue = residue.updated(charIndex, residue(charIndex) + 1)
    }
    residue
  }
}
