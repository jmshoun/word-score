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

  "Letter matches" should "have the correct effect on words" in {
    val test = new Word("test")
    assert(test.matchLetter('a').residue == test.residue)
    assert(test.matchLetter('t').residue == test.residue)
    assert(test.matchLetter('e').residue == test.residue.tail)
  }

  "Letter set matches" should "yield the correct results" in {
    val test = new Word("test")
    assert(test.matchesLetterSet(List('e', 's', 't', 't')))
    // Irrelevant letters shouldn't matter
    assert(test.matchesLetterSet(List('b', 'e', 's', 't', 't')))
    assert(test.matchesLetterSet(List('b', 'e', 's', 't', 't', 't')))
    assert(test.matchesLetterSet(List('b', 'e', 'p', 's', 't', 't', 'v')))
    // Any missing letters should matter
    assert(!test.matchesLetterSet(List('s', 't', 't')))
    assert(!test.matchesLetterSet(List('e', 's', 't')))
    // Extra letters shouldn't make up for missing letters
    assert(!test.matchesLetterSet(List('b', 'e', 'q', 's', 't', 'v')))
  }
}
