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

        for (int i = 0; i != population.length; i++) {
            if (mutationRate >= rand.nextDouble()) {
                mutantsID.add(i);
            }
        }

        initMutationWorker();
        runMutationWorker();
        while (mutantCount != mutantsID.size()) {
            try {
                this.wait();
            } catch (InterruptedException iE) {
                iE.printStackTrace(System.err);
            }
        }

        return population;
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

    public synchronized void setMutant(RuleSet mutant, int mutantID) {
        population[mutantID] = mutant;
        mutantCount++;
        notify();
    }

    private class MutationWorker extends Thread {

        private Thread thread;
        private int mutantID;
        private Mutation mutation;
        private Random rand;

        public MutationWorker(Mutation mutation) {
            this.mutation = mutation;
            rand = new Random();
            thread = new Thread(this);
           
        }

        public void setMutantID(int mutantID) {
            this.mutantID = mutantID;
        }

        @Override
        public void run() {
            createMutant();

        }

        private void createMutant() {
            RuleSet mutantRuleSet = population[mutantID].createClone();

            int mutantIndiv = rand.nextInt(mutantRuleSet.getRules().length);

            int[] gene = mutantRuleSet.getRules()[mutantIndiv].getGene();
            int classfication = mutantRuleSet.getRules()[mutantIndiv].getClassification();

            if (rand.nextDouble() > 0.8) {
                classfication = changeClassification(classfication);
            } else {
                gene = changeGene(gene);
            }

            Individual[] mutantRules = mutantRuleSet.getRules();
            mutantRules[mutantIndiv] = new Individual(gene, classfication);

            mutantRuleSet.setRules(mutantRules);
            
            Fitness.setFitness(mutantRuleSet);

            if (mutantRuleSet.getFitness() > population[mutantID].getFitness()) {
                mutation.setMutant(mutantRuleSet, mutantID);
            } else {
                mutation.setMutant(population[mutantID], mutantID);
            }

        }

        private int changeClassification(int classfication) {
            int classification = 1;

            if (classfication == 1) {
                classification = 0;
            }

            return classification;
        }

        private int[] changeGene(int[] gene) {

            int mutantGene = rand.nextInt(gene.length);

            if (rand.nextDouble() >= 0.8) {
                gene[mutantGene] = Individual.HASH_SIGN;
            } else {
                if (gene[mutantGene] == 1) {
                    gene[mutantGene] = 0;
                } else {
                    gene[mutantGene] = 1;
                }
            }

            return gene;
        }
    }

}