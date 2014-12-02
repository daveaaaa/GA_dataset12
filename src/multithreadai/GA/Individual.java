package multithreadai.GA;

import static multithreadai.MultiThreadAI.trainingSet;

/**
 *
 * @author David Armstrong
 */
public class Individual {

    public final static int HASH_SIGN = -1;

    private int[] chromosome;
    private int classification;

    public Individual() {

    }

    public Individual(int[] chromosome, int classification) {
        this.chromosome = chromosome;
        this.classification = classification;

    }

    public boolean isMatch(Individual data) {
        boolean isMatch = true;
        
        for (int i = 0; i != chromosome.length; i++) {
            if (this.chromosome[i] == HASH_SIGN) {
                // Do nothing
            } else if (data.getChromosome()[i] != this.chromosome[i]) {
                isMatch = false;
                break;
            }
        }
        return isMatch;
    }

    public int getClassification() {
        return classification;
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public Individual createClone() {
        int[] cloneGene = new int[chromosome.length];
        int cloneClassification = classification;
        for (int i = 0; i != chromosome.length; i++) {
            cloneGene[i] = chromosome[i];
        }

        return new Individual(cloneGene, cloneClassification);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(chromosome.length + 2);
        for (int i = 0; i != chromosome.length; i++) {
            if (chromosome[i] == HASH_SIGN) {
                sb.append("#");
            } else {
                sb.append(chromosome[i]);
            }
        }

        sb.append(" ");
        sb.append(classification);

        return sb.toString();
    }

}
