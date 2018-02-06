## Lecture 1 Hash function

### Hash function:

- Takes any string as input
- fixed-size output (`256bits` is what we use)
- efficiently computable
### Security Properties of Hash function
- **collision-free**
    - Idea: Nobody can find a pair $x,y$ s.t. $x \neq y$ yet $Hash(x) = Hash(y)$
    - Collisions do exist
      - Easy to illustrate, take our `256 bits` hash function as example: there are only $2^{256}$ outputs and undefined arbitrarily large amount of input strings. There has to be collisions.
    - So the key is to avoid regular people to find these collisions
    - Ways to find collisions
      - Try $2^{130}$ randomly chosen inputs, there are $99.8\%$ chance that two of them collide. But this takes too long to matter.
    - No hash functions up to now has been proven to be **collision-free**, it's just too hard to find a collision so we believe it as **collision-free**
    - Application of **collision-free**: Hash as message digest
      - If we know $H(x) = H(y)$, we can say $x=y$ as we believe $H$ is collision-free
      - So we can use it as a tool to compare large objects like files. Instead of comparing the whole file, we can just compare their hash which is significantly smaller. (In fact, `md5` is used to verify file integrity)

- **hiding**

    - Given $H(x)$, it is infeasible to find $x$.

    - No value of $x$ that is particularly likely. Set of value $x$ has to be very sparse. 

    - Concatenate $x$ with another value $r$ which is from a very sparse set (or $r$ is chosen from a probability distribution that has `high min-entrophy`) to sovle this problem. 
      `High min-entropy` means that the distribution is very `spread out`, so that no particular value is chosen with more than negligible probability.

      Then given $H(r \mid x)$, it is infeasible to find $x$.

    - Application: Commitment

      ```
      (com, key) := commit(msg)
      match := verify(com, key, msg)

      To seal msg in envelope:
      	(com, key) := commit(msg) #then publish com
      To open envelope:
      	publish key, msg
      	anyone can use verify to check its validity
      	
      #Implementation:
      commit(msg) := (H(key|msg), key) where key is a random 256-bit value
      verify(com, key, msg) := (H(key|msg) == com)

      #To have such security properties:
      	Hiding: given (H(key|msg), infeasible to find msg
      	Binding: Infeasible to find msg != msg' s.t. that (H(key|msg) == H(key|msg')
      ```

- **puzzle-friendly**

    - For every possible output value $y$, if $k$ is chosen from a distribution with high min-entropy, then it is infeasible to find $x$ s.t. $H(k\mid x) = y$

    - Application: Search puzzle

      ``` 
      Given a 'puzzle ID' id(from high min-entropy distrib) and a targe set Y, try to find a 'solution' x s.t. H(id | x) in Y.
      ```

### Example: SHA-256 hash function

![sha-256](sha-256.png)

Algo walkthrough:

1. Take the original message and divide it up in chunks of `512bits​` size. The last block is padded with how many bits are there in the block and 0s to fill it up to `512bits​`.
2. After the message is partitioned, we take the `IV` key which is a `256bits` string and concatenate with the first chunk of message `block_1 ` to be a `768bits` message chunk.
3. Run this message chunk thru the compression algorithm `c` to produce a new `256bits` key and pass into the next execution with message `block_2`
4. Repeat until no blocks left; return hash.

**Th.** If `c` is collision-free, then `SHA-256` is collsiion free.

## Lecture 2 Hash pointers

### Hash pointer

- Pointer to where some info is stored
- and cryptographic hash of the info
- just like normal pointers, but not only where it is stored but also the hash of its data

With hash pointer, we can

- ask to get the info back
- certify that it hasn't changed

### Key idea: Build data structure with hash pointers

#### Example: Linkedlist with Hash pointers (blockchain)![ll_hashpointers](ll_hashpointers.png)

- we can build temper-evident log (If someone mess with data earlier we can detect it)

#### Example: binary tree with hash pointers (Merkle tree)

![merkle tree](merkle tree.png)

- Advantages:
  - Holds many items but only need to remember the root hash
  - Can verify membership in $O(\log n)$ time
- Variant: sorted Merkle tree (binary search tree with hash pointers)
  - can verify non-membership in $O(\log n)$ time (Prove nothing in between)

#### Generally

- Can use hash pointers in any pointer-based data structure that has not cycles

## Lecture 3 Digital Signatures

### Features of Signatures

- Only you can sign, but anyone can verify
- Signature is tied to particular document; can't be copy-and-pasted to another document

### API for digital signatures

```
(sk, pk) := generateKeys(keysize)
sig := sign(sk, message)
isValid := verify(pk, message, sig)
```

#### Requirements for signatures

- Valid signatures verify

  `verify(pk, message, sign(sk,message)) == true`

- Can't forge signatures

  - Adversary who
    - knows pk
    - gets to see signatures on messages of his choice
  - can't produce a verifiable signature on another message

#### Practical stuff

- Algorithms are randomized
  - good source of randomness required
- Limit on message isze
  - fix: use Hash(message) rather than message
- Fun trick: sign a hash pointer
  - signatures covers the whole structure (sign the entire content)
- Bitcoins uses `ECDSA(Elliptic Curve Digital Signature Algorithm)` standard
  - relies on extremely complicated maths
  - good randomness is extremely essential for ECDSA

## Lecture 4 Public Keys as Identities

### Useful trick: `pulic key == an identity`

- if you see `sig` s.t. `verify(pk, msg, sig) == true` ,

  In order to speak as `pk`, you need to have the matching private key `sk`.

### How to make a new identity

- create a new random key-pair `(sk, pk)`
- `pk` is the pubic "name" you can use, normally `H(pk)` as `pk` is large
- `sk` let's you speak as `pk`
- you control the identity because only you know `sk`
- if `pk` looks random, nobody needs to know who you are

### Decentralized identity management

- No need to use username, etc
- anyone can make a new identity anytime and make as many as wanted
- no central point of coordination
- these identities are called `address` or `wallet_address` in `Bitcoin`

### Privacy (how private it is)

- Addresses not directly connected to real-word identity
- But observer can link together an address's activity over time, make inferences
- Will talk this later

## Lecture 5 A simple Cryptocurrency

### Goofy Coin: Simplest Cryptocurrency

#### Rules:

- Goofy can create new coins that belongs to him

  ![goofy_create_coin](goofy_create_coin.png)

  `CreateCoin[uuid]` singed by `pk_goof`

- A coin's owner can spend it

  ![goofy_spend_coin](goofy_spend_coin.png)

  - make a statement `pay to pk_alice` with hash pointer signed by `pk_goofy`

- The recipient can pass on the coin again

  ![pass_along](pass_along.png)

#### Problems

double-spending attack (one of the major design challenges)

![double-spending attack](double-spending attack.png)

- both `Bob` and `Chuck` has a valid claim on the coin

### Scrooge Coin

#### Changes

- on top of goofy coin

- Scrooge publishes a history of all transactions(a blockchain signed by Scrooge)

  ![scrooge_blockchain](scrooge_blockchain.png)

- Optimization: put multiple transactions in the same block

- Now everyone can detect double-spending

- New `CreateCoins` transaction

  ![createCoins transaction](createCoins transaction.png)

- New `PayCoin` transaction: 

  ![PayCoins](PayCoins.png)

  - consumes some coins and creates new coins of the same total value
  - Valid if
    - consumed coins valid
    - not already consumed
    - total value out = total value in
    - signed by owners of all consumed coins

- New **<u>Immutable</u>** `Coin`

  - Coins can't be transferred, subdivided, or combined
  - But you can achieve the same effect by using transactions to subdivide
    - Create new trans that consume your coin and pay out two new coins to yourself with a same total value

#### Problem: Centralization

- Scrooge can be misbehaving

- Scrooge have to operate his functions all the time

- how to operate **without** any central, trusted party

- Problems to solve:

  If we can solve all of these problems, then we can build a currency that is very much like BitCoin. Which is like ScroogeCoin but without a centralized party.

  - How everyone can agree upon a single published block chain that is the agreed upon history which transactions have happened?
  - How people can agree which transactions are valid and which transactions have actually occurred?
  - How we can assign IDs to things in a decentralized way? 

#### Assignment: Scrooge Coin's public ledger

