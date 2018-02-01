class Word(val spelling: String, val residue: List[Char]) {
  def this(spelling: String) = this(spelling, spelling.toList.sorted)

  val points = 1
}
