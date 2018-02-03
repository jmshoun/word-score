import org.scalatest.FlatSpec

class BestSetTest extends FlatSpec {
  val wordList = WordScoreApp.loadWordList("src/test/resources/sample_dict.txt")

  "BestSets" should "find the correct results in the 2-letter case" in {
    val bestSet = BestSet.findBestSet(wordList, 2)
    assert(bestSet == List('a', 't'))
  }

  "BestSets" should "find the correct results in the 3-letter case" in {
    val bestSet = BestSet.findBestSet(wordList, 3)
    assert(bestSet == List('a', 'e', 't'))
  }
}
