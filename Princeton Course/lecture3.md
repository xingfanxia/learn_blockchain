# Mechanics of Bitcoin

## Part 1 Bitcoin Transactions

### Recap: Bitcoin consensus

Bitcoin consensus gives us:

- Append-only ledger
- Decentralized consensus
- Miners to validate transactions

**Assuming a currency exists to motivate miners**

### An account-based ledger (not Bitcoin)

![account-based ledger](account-based ledger.png)

- We have to look over all transactions back there till the beginning of time to figure out if `Alice` has enough coins.
- Even if we put some data structure to keep track of `Alice's` balance, there is a lot more housing keeping despite the blockchain itself.
- So bitcoin does not use this model.

### An transaction-based ledger (Bitcoin)

![transaction-based ledger](transaction-based ledger.png)

- Always completely consume the input by giving an equal output.
- Can trace back much easier by looking at the input pointer to check if the transaction is valid
- Implemented with hash pointers

#### Joint Payments

​	![joint_payments](joint_payments.png)

- Consume two inputs to joint pay `David`
- So needs two signatures as came from two different people

### Real deal: a Bitcoin transaction(very very close version)

![bitcoin transaction](bitcoin transaction.png)

#### Metadata

![transaction_metadata](transaction_metadata.png)

#### Input

![transaction_inputs](transaction_inputs.png)

#### Output

![transaction_outputs](transaction_outputs.png)

## Part 2 Bitcoin Scripts

