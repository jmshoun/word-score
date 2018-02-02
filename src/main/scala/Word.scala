class Word(val spelling: String, val residue: List[Char]) {
  def this(spelling: String) = this(spelling, spelling.toList.sorted)

  val points = 1

  def matchLetter(letter: Char): Word = {
    if (letter == residue.head) {
      new Word(spelling, residue.tail)
    } else {
      this
    }
  }
}
