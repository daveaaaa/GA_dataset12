package multithreadai.GA;

import multithreadai.GA.Individual;
import multithreadai.GA.Fitness;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author David Armstrong
 */
public class Mutation extends Thread {

    private double mutationRate;
    private Random rand;
    private ArrayList<Integer> mutantsID;
    private MutationWorker[] mutationWorker;
    private RuleSet[] population;
    private int mutantCount;

    public Mutation(double mutationRate) {
        this.mutationRate = mutationRate;
        rand = new Random();
    }

    public synchronized RuleSet[] doMutation(RuleSet[] population) {
        mutantsID = new ArrayList<>();
        mutantCount = 0;

        this.population = population;

        getMutantsIDs();
        
        initMutationWorker();
        runMutationWorker();
        waitForMutationWorkersToFinish();

        return population;
    }

    private void getMutantsIDs() {
        for (int i = 0; i != population.length; i++) {
            if (mutationRate >= rand.nextDouble()) {
                mutantsID.add(i);
            }
        }
    }

    private void initMutationWorker() {
        mutationWorker = new MutationWorker[mutantsID.size()];
        for (int i = 0; i != mutantsID.size(); i++) {
            mutationWorker[i] = new MutationWorker(this);
            mutationWorker[i].setMutantID(mutantsID.get(i));
        }
    }

    private void runMutationWorker() {
        for (int i = 0; i != mutationWorker.length; i++) {
            mutationWorker[i].start();
        }
    }

    private void waitForMutationWorkersToFinish() {
        while (mutantCount != mutantsID.size()) {
            try {
                this.wait();
            } catch (InterruptedException iE) {
                iE.printStackTrace(System.err);
            }
        }
    }

    public synchronized void setMutant(RuleSet mutant, int mutantID) {
        population[mutantID] = mutant;
        population[mutantID].setFitnessChanged(true);
        mutantCount++;
        notify();

    }

    private synchronized int getRandomInt(int bound) {
        return rand.nextInt(bound);
    }

    private synchronized double getRandomDouble() {
        return rand.nextDouble();
    }

    private class MutationWorker extends Thread {

        private final static int CLASSIFICATION_BIT_SIZE = 1;
        private Thread thread;
        private int mutantID;
        private Mutation mutation;

        public MutationWorker(Mutation mutation) {
            this.mutation = mutation;
            thread = new Thread(this);

        }

        public void setMutantID(int mutantID) {
            this.mutantID = mutantID;
        }

        @Override
        public void run() {
            if (mutation.getRandomDouble() > 0.8) {
                reverseMutant();
            } else {
                bitMutant();
            }

        }

        private void reverseMutant() {
            RuleSet mutantRuleSet = population[mutantID].createClone();
            Individual[] mutantChromosome = new Individual[mutantRuleSet.getRules().length];

            for (int i = 0; i != mutantRuleSet.getRules().length; i++) {
                mutantChromosome[i] = mutantRuleSet.getRules()[(mutantRuleSet.getRules().length - 1) - i];
            }

            mutantRuleSet.setRules(mutantChromosome);

            mutation.setMutant(mutantRuleSet, mutantID);
        }

        private void bitMutant() {

            RuleSet mutantRuleSet = population[mutantID].createClone();

            int mutantIndiv = mutation.getRandomInt(mutantRuleSet.getRules().length);

            int[] gene = mutantRuleSet.getRules()[mutantIndiv].getChromosome();
            int classification = mutantRuleSet.getRules()[mutantIndiv].getClassification();

            int mutateGeneID = mutation.getRandomInt(gene.length + CLASSIFICATION_BIT_SIZE);

            if (mutateGeneID > gene.length - 1) {
                classification = changeClassification(classification);
            } else {
                gene = changeGene(gene, mutateGeneID);
            }

            Individual[] mutantRules = mutantRuleSet.getRules();
            mutantRules[mutantIndiv] = new Individual(gene, classification);

            mutantRuleSet.setRules(mutantRules);

            mutation.setMutant(mutantRuleSet, mutantID);

        }

        private int changeClassification(int classfication) {
            int classification = 1;

            if (classfication == 1) {
                classification = 0;
            }

            return classification;
        }

        private int[] changeGene(int[] gene, int mutantGene) {

            if (mutation.getRandomDouble() >= 0.9) {
                gene[mutantGene] = Individual.HASH_SIGN;
            } else {
                if (gene[mutantGene] == 1) {
                    gene[mutantGene] = 0;
                } else if (gene[mutantGene] == 0) {
                    gene[mutantGene] = 1;
                } else {
                    if (mutation.getRandomDouble() >= 0.5) {
                        gene[mutantGene] = 1;
                    } else {
                        gene[mutantGene] = 0;
                    }
                }
            }

            return gene;
        }
    }

}
