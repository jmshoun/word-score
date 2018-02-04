## word-score

A simple command line application written in Scala to answer the following puzzle: What set of letters yields the greatest sum of points of words that can be spelled using only letters in that set? For example, what seven tiles yield the greatest sum of Scrabble points of words that can be spelled from those tiles?

## Installation

This program must be compiled with Scala 2.12. There are no external dependencies. The included shell script wrapper expects a standalone JAR in `/out/artifacts/words-score.jar`.

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

## Dictionaries

The program requires the use of an external dictionary. A few options are:
  - [OPSD](http://www.puzzlers.org/pub/wordlists/ospd.txt) - The official Scrabble players dictionary.
  - [ENABLE1](https://github.com/dolph/dictionary/blob/master/enable1.txt) - A common corpus used by Words with Friends.
  - The build-in dictionary in a *nix operating system. For example, Ubuntu has a dictionary at `/usr/share/dict/american-english`.

## Table of Results

All of the results below were compiled using the OSPD.

Letter Set Size | Scoring Method | Letter set | # Points | # Words
--- | --- | --- | --- | ---
4 | Default | a e s t | 24 | 24
5 | Default | a e l s t | 66 | 66
6 | Default | a e p r s t | 150 | 150
7 | Default | a e l p r s t | 281 | 281
4 | Boggle | a e s t | 18 | 18
5 | Boggle | a e l s t | 18 | 18
6 | Boggle | a e l p s t | 191 | 136
7 | Boggle | a e l p r s t | 437 | 267
4 | Scrabble | a p s y | 116 | 18
5 | Scrabble | a e p r s | 329 | 63
6 | Scrabble | a e p r s t | 847 | 150
7 | Scrabble | a e l p r s t | 2015 | 281

