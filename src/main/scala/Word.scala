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
