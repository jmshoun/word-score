import org.scalatest.FlatSpec

class WordListTest extends FlatSpec {
  "Word lists" should "scrub bad words" in {
    val wordList = WordScoreApp.loadWordList("src/test/resources/sample_dict.txt")
    assert(wordList.size == 3)
    assert(wordList(0).spelling == "ate")
    assert(wordList(1).spelling == "eat")
    assert(wordList(2).spelling == "tea")
  }
}
