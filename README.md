## word-score

A simple command line application written in Scala to answer the following puzzle: What set of letters yields the greatest sum of points of words that can be spelled using only letters in that set? For example, what seven tiles yield the greatest sum of Scrabble points of words that can be spelled from those tiles?

## Example Usage

```$ ./word-score --scrabble --word-points --size 4 /usr/share/dict/american-english 
Best set: List(a, p, s, y)
Point count: 97
Word count: 18
Words:
====================
  1 | a
  2 | as
  5 | asp
  5 | ay
  3 | p
  4 | pa
  5 | pas
  8 | pay
  9 | pays
  1 | s
  5 | sap
  6 | say
  5 | spa
  9 | spay
  8 | spy
  4 | y
  8 | yap
  9 | yaps
```

