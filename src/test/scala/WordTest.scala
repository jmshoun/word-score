import org.scalatest.FlatSpec

class WordTest extends FlatSpec {
  "Words" should "have a default point value" in {
    val test = new Word("test")
    assert(test.points == 1)
  }
}

class WordResidueTest extends FlatSpec {
  "Word residues" should "have initial residue sizes equal to their lengths" in {
    val test = new WordResidue(new Word("test"))
    val aardvark = new WordResidue(new Word("aardvark"))
    assert(test.residueSize == 4)
    assert(aardvark.residueSize == 8)
  }

  "Word residues" should "have correct residue vectors" in {
    val aardvark = new WordResidue(new Word("aardvark"))
    assert(aardvark.residueLetters(0) == 3)
    assert(aardvark.residueLetters(17) == 2)
    assert(aardvark.residueLetters(21) == 1)
    assert(aardvark.residueLetters(10) == 1)
    assert(aardvark.residueLetters(3) == 1)
    // Check a random chunk of letters that should be zero
    for (n <- 4 to 9) assert(aardvark.residueLetters(n) == 0)
  }
}
