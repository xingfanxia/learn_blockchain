import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;

public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    private UTXOPool uPool;

    public TxHandler(UTXOPool utxoPool) {
        this.uPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {

        ArrayList<Transaction.Output> outputs = tx.getOutputs();
        ArrayList<Transaction.Input> inputs = tx.getInputs();
        ArrayList<UTXO> allUTXO = this.uPool.getAllUTXO();
        ArrayList<Transaction.Output> poolOutputs = new ArrayList<Transaction.Output>();

        ArrayList<UTXO> checkDup = new ArrayList<UTXO>();
        for (UTXO utxo:allUTXO) {
            poolOutputs.add(this.uPool.getTxOutput(utxo));
        }

        for (int i = 0; i < outputs.size(); i++) {
            //(1) all outputs claimed by {@code tx} are in the current UTXO pool
            if (!outputs.get(i).equals(poolOutputs.get(i)))
                return false;

            //(2) the signatures on each input of {@code tx} are valid
            if (!Crypto.verifySignature(outputs.get(i).address, tx.getRawTx(), inputs.get(i).signature))
                return false;

            if (outputs.get(i).value < 0)
                return false;
            checkDup.add(new UTXO(inputs.get(i).prevTxHash, inputs.get(i).outputIndex));
        }
        Set<UTXO> checkDupset = new HashSet<UTXO>(checkDup);
        if (checkDupset.size() < checkDup.size())
            return false;

        for  (int j = 0; j < outputs.size(); j++) {
            if (Crypto.verifySignature(input.signature))
        }

    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
    }

}
