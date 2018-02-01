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
    assert(aardvark.residueValue('a') == 3)
    assert(aardvark.residueValue('r') == 2)
    assert(aardvark.residueValue('d') == 1)
    assert(aardvark.residueValue('v') == 1)
    assert(aardvark.residueValue('k') == 1)
    // Check a random chunk of letters that should be zero
    for (c <- 'e' to 'j') assert(aardvark.residueValue(c) == 0)
  }
}
