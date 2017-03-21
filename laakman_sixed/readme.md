## chapter 3. Stack and queue (3/6)
### 3.2 Stack with min in O(1)
* my ideas: augment stack - push pairs `(v, min)`, where min is current `min`;
 this `min` is also the `min` **below** `v`;
* solution: this is the valid idea; it's also possible to store min in a 
second stack; also we may subclass existing stack instead of rewriting it;
```
// min == 2 and we push 3
push Pair(3, 2); 
// min == 2 and we push 1
push Pair(1, 2);
```

### 3.4 Queue via stack
* my ideas: (a) we have 2 stacks - say `left` and `right`; we push elements
into `left` stack until first `dequeue` request; (b) then we move elements into 
`right` stack and do `deque`; **key observation** - in this case we `dequeue` 
based on FIFO; (c) next observation - we may leave all elements in `right` stack 
and dequeue from there (until its empty), while pushing elements into the `right`
stack;
* solution: this is basically identical with what we have in solution; Laakman talks about
key difference between stack and queue; so we can reverse order using the second
stack; she also calls our approach to leave elements in the second stack as "lazy";

### 3.5 Sort stack using additional stack
* solution: use analog of insertion sort; suppose we have 2 stacks: `left` and `right`;
`right` is sorted; (a) to move element `x` from `left` to `right` we pop from `right`
until they are less than `x` (and store them in `left`); (b) then push `x` to right; 
(c) we repeat this - so elements moved on step `a` will be above `x` in `right`;





















