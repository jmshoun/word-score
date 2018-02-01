import org.scalatest.FlatSpec

class WordTest extends FlatSpec {
  "Words" should "have a default point value" in {
    val test = new Word("test")
    assert(test.points == 1)
  }

  "Words" should "have correct residues" in {
    val test = new Word("test")
    val aardvark = new Word("aardvark")
    assert(test.residue == List('e', 's', 't', 't'))
    assert(aardvark.residue == List('a', 'a', 'a', 'd', 'k', 'r', 'r', 'v'))
  }
}
