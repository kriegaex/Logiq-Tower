# Logiq Tower™

Automatically solve all levels of the [Logiq Tower™](http://logiqtower.com/)
puzzle produced by Great Circle Works. The puzzle is originally made of wood
and challenges the user to build a cylindrical tower without any gaps.

I am presenting here a lightning-fast solver utilising Donald Knuth's
[DLX (Dancing Links) algorithm](http://en.wikipedia.org/wiki/Dancing_Links).

| Level | Rows | Unique solutions (my software) | Duplicates eliminated (my software) | Unique solutions (game manual) | Time for finding & printing all solutions incl. duplicates (my software) |
|--:|--:|--:|--:|--:|--:|
| 1 | 2 | 22 | 14  | 23 | 0.07 s |
| 2 | 3 | 2,051 | 1,098  | 2,294  | 0.33 s |
| 3 | 4 | 12,570 | 6,511 | 13,588 | 1.90 s |
| 4 | 5 | 5,676 | 2,958 | 6,164 | 6.60 s |

Actually the sparse DLX matrix is set up in a way which avoids most
shift/rotate duplicates to begin with. My checker finds some anyway.
I have no idea why there is a ~4-11 % relative difference between my own
solution count and the official one written from the game's manual/website.
Anyway, I guess my solution is correct. I test-built a few of the solutions
found for each level and each of them could be easily verified with the
physical (wooden) puzzle.

Basically the DLX solver can be applied to other problems such as Sudoku,
n-Queens, Polyominos etc. Even though my own solutions is a self-built one,
only using Knuths original paper as a support, I know that there are several
other DLX solvers out there, for example
[Ben Fowler's](https://github.com/benfowler/dancing-links).

The current _master_ branch still contains my naive old exhaustive search solver
which does not use DLX and takes ages to terminate for higher levels (3-4).
I provide it just for reference.

Enjoy!
