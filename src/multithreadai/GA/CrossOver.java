/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadai.GA;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author David Armstrong
 */
public class CrossOver extends Thread {

    private RuleSet[] population;
    private RuleSet[] newGeneration;
    private int crossOverPoint;
    private CrossOverWorker[] crossoverWorkers;
    private int childCount;
    private boolean randomCrossover;
    private double crossoverPercentage;
    private int crossoverNumber;

    public CrossOver(int crossOverPoint, boolean randomCrossover, double crossoverPercentage) {
        this.crossOverPoint = crossOverPoint;
        this.randomCrossover = randomCrossover;
        this.crossoverPercentage = crossoverPercentage;
    }

    public synchronized RuleSet[] doCrossOver(RuleSet[] population) {
        Random rand = new Random();
        childCount = 0;
        this.population = population;

        crossoverNumber = (int) ((double) population.length * crossoverPercentage);

        newGeneration = new RuleSet[population.length];

        initCrossoverWorkers(crossoverNumber, population[0].getRules().length);
        getChildren();

        while (childCount != crossoverNumber) {
            try {
                wait();
            } catch (InterruptedException iE) {
                iE.printStackTrace(System.err);
            }
        }

        
        for (int i = crossoverNumber; i != population.length; i++) {
            newGeneration[i] = population[rand.nextInt(population.length)].createClone();
        }
        
        return newGeneration;
    }

    private void initCrossoverWorkers(int size, int geneSize) {
        Random rand = new Random();
        crossoverWorkers = new CrossOverWorker[size];

        for (int i = 0; i != size; i++) {
            if (randomCrossover) {
                int randomCrossoverPoint = rand.nextInt(geneSize);
                crossoverWorkers[i] = new CrossOverWorker(this, randomCrossoverPoint);
            } else {
                crossoverWorkers[i] = new CrossOverWorker(this, crossOverPoint);
            }

        }
    }

    private void getChildren() {
        for (int i = 0; i != crossoverWorkers.length; i++) {
            crossoverWorkers[i].start();
        }
    }

    public synchronized void addChildren(ArrayList<RuleSet> children) {
        int i = 0;
        while ((i != children.size()) & (childCount < crossoverNumber)){
            newGeneration[childCount] = children.get(i);
            newGeneration[childCount].setFitnessChanged(true);
            childCount++;
            i++;
        }
        notify();
    }

    private class CrossOverWorker extends Thread {

        private Random rand = new Random();
        private Thread thread;
        private CrossOver crossOver;
        private ArrayList<RuleSet> children;
        private int ownCrossoverPoint;

        public CrossOverWorker(CrossOver crossOver, int crossoverPoint) {
            children = new ArrayList<>();
            ownCrossoverPoint = crossoverPoint;
            this.crossOver = crossOver;
            this.thread = new Thread(this);
        }

        @Override
        public void run() {
            createChildren();
            crossOver.addChildren(children);
        }

        private void createChildren() {
            RuleSet parent1 = population[rand.nextInt(population.length)];
            RuleSet parent2 = population[rand.nextInt(population.length)];

            getChildren(parent1, parent2);
        }

        private void getChildren(RuleSet parent1, RuleSet parent2) {
            Individual[] child1Gene = new Individual[parent1.getRules().length];
            Individual[] child2Gene = new Individual[parent1.getRules().length];

            for (int i = 0; i != ownCrossoverPoint; i++) {
                child1Gene[i] = parent1.getRules()[i];
                child2Gene[i] = parent2.getRules()[i];
            }

            for (int i = ownCrossoverPoint; i != parent1.getRules().length; i++) {
                child1Gene[i] = parent2.getRules()[i];
                child2Gene[i] = parent1.getRules()[i];
            }

            RuleSet child1 = new RuleSet(child1Gene);
            RuleSet child2 = new RuleSet(child2Gene);

            Fitness.setFitness(child1);
            Fitness.setFitness(child2);

            children.add(child1);
            children.add(child2);

        }
    }

}
