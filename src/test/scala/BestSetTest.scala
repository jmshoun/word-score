import org.scalatest.FlatSpec

class BestSetTest extends FlatSpec {
  val wordList = WordScoreApp.loadWordList("src/test/resources/sample_dict.txt")

  "BestSets" should "find the correct results in the 2-letter case" in {
    val (bestSet, bestCount) = BestSet.findBestSet(wordList, 2)
    assert(bestSet == List('a', 't'))
    assert(bestCount == 2)
  }

  "BestSets" should "find the correct results in the 3-letter case" in {
    val (bestSet, bestCount) = BestSet.findBestSet(wordList, 3)
    assert(bestSet == List('a', 'e', 't'))
    assert(bestCount == 6)
  }
}
