/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadai;

import java.text.DecimalFormat;
import multithreadai.GA.GA;
import multithreadai.GA.Individual;
import multithreadai.utils.HeaderResults;
import multithreadai.utils.FileWriter;
import multithreadai.utils.FileReader;
import multithreadai.utils.Result;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author david
 */
public class MultiThreadAI {

    public static Individual[] trainingSet;
    public static Individual[] unseenSet;

    private static String dataFile;
    private static double trainingSetPercent;
    private static int populationSize;
    private static double mutationRate;
    private static int crossOverPoint;
    private static int competitorSize;
    private static int maxGenerations;
    private static int ruleSetSize;
    private static double crossoverPercentage;
    private static int maxRuns;
    private static boolean randomCrossover = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        trainingSetPercent = 0.8;
        dataFile = "data2";
        FileReader.generateDataSets(trainingSetPercent, dataFile);

        initVariables();
        increaseRuleSet();

//        initVariables();
//        increaseCompetitorSize();
//        initVariables();
//        increasePop();
//        initVariables();
//        increaseMutationRate();
//        initVariables();
//        randomCrossover();
//        initVariables();
//        increaseCrossover();
//        trainingSetPercent = 0.8;
//        dataFile = "data2";
//        FileReader.generateDataSets(trainingSetPercent, dataFile);
//
//        initVariables();
//        increaseCompetitorSize();
//        initVariables();
//        increasePop();
//        initVariables();
//        increaseMutationRate();
//        initVariables();
//        randomCrossover();
//        initVariables();
//        increaseCrossover();
    }

    private static void initVariables() {
        trainingSetPercent = 0.8;
        populationSize = 100;
        mutationRate = 0.1;
        crossOverPoint = trainingSet[0].getGene().length / 2;
        randomCrossover = false;
        competitorSize = 2;
        maxGenerations = 3000;
        ruleSetSize = 20;
        crossoverPercentage = 0.7;
        maxRuns = 20;
    }

    private static void increasePop() {
        populationSize = 10;
        for (int i = 0; i != 4; i++) {
            long starttime = System.currentTimeMillis();
            doTest("IncreasePop", "Test" + i);
            long endtime = System.currentTimeMillis();
            populationSize += 5;
            System.out.println("IncreasePop\t" + "Test" + i + "\t" + (((endtime - starttime) / 1000) / 60));
        }
    }

    private static void increaseRuleSet() {
        ruleSetSize = 2;
        crossOverPoint = ruleSetSize / 2;
        for (int i = 0; i != 19; i++) {
            long starttime = System.currentTimeMillis();
            doTest("IncreaseRuleSetSize", "Test" + i);
            long endtime = System.currentTimeMillis();
            ruleSetSize += 1;
            crossOverPoint = ruleSetSize / 2;
            System.out.println("IncreaseRuleSetSize\t" + "Test" + i + "\t" + (((endtime - starttime) / 1000) / 60));
        }
    }

    private static void randomCrossover() {
        randomCrossover = true;
        for (int i = 0; i != 4; i++) {
            long starttime = System.currentTimeMillis();
            doTest("randomCrossover", "Test" + i);
            long endtime = System.currentTimeMillis();
            System.out.println("randomCrossover\t" + "Test" + i + "\t" + (((endtime - starttime) / 1000) / 60));
        }
    }

    private static void increaseCrossover() {
        crossoverPercentage = 0.0;
        for (int i = 0; i != 4; i++) {
            long starttime = System.currentTimeMillis();
            doTest("increaseCrossover", "Test" + i);
            long endtime = System.currentTimeMillis();
            crossoverPercentage += 0.1;
            System.out.println("Increase Crossover\t" + "Test" + i + "\t" + (((endtime - starttime) / 1000) / 60));
        }
    }

    private static void increaseMutationRate() {
        System.out.println("Increase Mutation Rate");
        for (int i = 0; i != 4; i++) {
            doTest("IncreaseMutationRate", "Test" + i);
            mutationRate += 0.1;
        }
    }

    private static void increaseCompetitorSize() {
        System.out.println("increase Competitor Size");
        for (int i = 0; i != 4; i++) {
            doTest("IncreaseCompetitorSize", "Test" + i);
            competitorSize += 3;
        }
    }

    private static void doTest(String directory, String fileName) {
        HeaderResults hr;
        String path;
        DecimalFormat df = new DecimalFormat("0.##");

        if (System.getProperty("os.name").contains("inux")) {

            path = "/home/david/Dropbox/uni/year_4/AI/assignment/MultiThreadAI/src/results/" + dataFile + "/" + directory + "/";
        } else {
            path = "C:\\Users\\david\\Dropbox\\uni\\year_4\\AI\\assignment\\MultiThreadAI\\src\\results\\" + dataFile + "\\" + directory + "\\";
        }

        hr = new HeaderResults(dataFile, mutationRate, trainingSetPercent, maxGenerations, ruleSetSize, populationSize, competitorSize);

        System.out.println("Run: \t Time: \tUnseen Fitness:");
        for (int run = 0; run != maxRuns; run++) {
            GA alg = new GA(competitorSize, mutationRate, crossOverPoint, populationSize, ruleSetSize, trainingSet[0].getGene().length, crossoverPercentage, randomCrossover);

            long starttime = System.currentTimeMillis();
            int intiUnseenFitness = alg.getFitness(unseenSet);

            double initPercent = (double) intiUnseenFitness / ((double) unseenSet.length / 100.0);

            Result[] results = alg.run(maxGenerations);

            int unseenFitness = alg.getFitness(unseenSet);

            double endPercent = (double) unseenFitness / ((double) unseenSet.length / 100.0);
            hr.addRunInformation(run, initPercent, endPercent);

            FileWriter.WriteFile(path, fileName, results, run, unseenFitness);

            long endtime = System.currentTimeMillis();
            System.out.println(run + "\t" + ((endtime - starttime) / 1000) + "\t" + df.format((double) unseenFitness / (unseenSet.length / 100.0)));

            //Force clean up (hopefully... )
            results = null;
            alg = null;
            System.gc();
        }

        if (System.getProperty("os.name").contains("inux")) {
            FileWriter.WriteFile(path, fileName + "Header", hr);
        } else {
            FileWriter.WriteFile(path, fileName + "Header", hr);
        }
    }
}
