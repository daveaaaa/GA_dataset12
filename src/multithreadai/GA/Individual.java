package multithreadai.GA;

import static multithreadai.MultiThreadAI.trainingSet;

/**
 *
 * @author David Armstrong
 */
public class Individual {

    public final static int HASH_SIGN = -1;

    private int[] gene;
    private int classification;

    public Individual() {

    }

    public Individual(int[] gene, int classification) {
        this.gene = gene;
        this.classification = classification;

    }

    public boolean isMatch(Individual data) {
        boolean isMatch = true;
        
        for (int i = 0; i != gene.length; i++) {
            if (this.gene[i] == HASH_SIGN) {
                // Do nothing
            } else if (data.getGene()[i] != this.gene[i]) {
                isMatch = false;
                break;
            }
        }
        return isMatch;
    }

    public int getClassification() {
        return classification;
    }

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        this.gene = gene;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public Individual createClone() {
        int[] cloneGene = new int[gene.length];
        int cloneClassification = classification;
        for (int i = 0; i != gene.length; i++) {
            cloneGene[i] = gene[i];
        }

        return new Individual(cloneGene, cloneClassification);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gene.length + 2);
        for (int i = 0; i != gene.length; i++) {
            if (gene[i] == HASH_SIGN) {
                sb.append("#");
            } else {
                sb.append(gene[i]);
            }
        }

        sb.append(" ");
        sb.append(classification);

        return sb.toString();
    }

}
