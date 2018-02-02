import org.scalatest.FlatSpec

class WordListTest extends FlatSpec {
  "Word lists" should "scrub bad words" in {
    val wordList = WordScoreApp.loadWordList("src/test/resources/sample_dict.txt")
    assert(wordList.size == 6)
    assert(wordList(0).spelling == "ate")
    assert(wordList(1).spelling == "eat")
    assert(wordList(5).spelling == "ta")
  }
}
