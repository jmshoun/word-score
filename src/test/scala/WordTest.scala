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

  "Word point calculations for Boggle" should "be correct" in {
    assert(Points.compute("test", 'boggle) == 1)
    assert(Points.compute("art", 'boggle) == 1)
    assert(Points.compute("to", 'boggle) == 0)
    assert(Points.compute("testing", 'boggle) == 5)
    assert(Points.compute("aardvark", 'boggle) == 11)
    assert(Points.compute("lksjalijalkves", 'boggle) == 11)
  }

  "Word point calculations for defaults" should "be correct" in {
    assert(Points.compute("test", 'default) == 1)
    assert(Points.compute("art", 'default) == 1)
    assert(Points.compute("to", 'default) == 1)
    assert(Points.compute("testing", 'default) == 1)
    assert(Points.compute("aardvark", 'default) == 1)
    assert(Points.compute("lksjalijalkves", 'default) == 1)
  }

  "Word point calculations for Scrabble" should "be correct" in {
    assert(Points.compute("test", 'scrabble) == 4)
    assert(Points.compute("art", 'scrabble) == 3)
    assert(Points.compute("party", 'scrabble) == 14)
    assert(Points.compute("testing", 'scrabble) == 60)
  }

  "Word point calculations for Words With Friends" should "be correct" in {
    assert(Points.compute("test", 'wordsWithFriends) == 4)
    assert(Points.compute("art", 'wordsWithFriends) == 3)
    assert(Points.compute("party", 'wordsWithFriends) == 20)
    assert(Points.compute("testing", 'wordsWithFriends) == 55)
  }
}
