/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadai.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import multithreadai.GA.Individual;
import static multithreadai.MultiThreadAI.trainingSet;
import static multithreadai.MultiThreadAI.unseenSet;

/**
 *
 * @author david
 */
public class FileReader {

    private static Random rand = new Random();
    private static File file;
    private static final int dataPart = 0;
    private static final int classificationPart = 1;
    private static Individual[] rawData;

    public static Individual[] readFile(String dataFile) {
        String url;

        if (System.getProperty("os.name").contains("inux")) {
            url = "/home/david/Dropbox/uni/year_4/AI/assignment/MultiThreadAI/src/resource/" + dataFile + ".txt";
        } else {
            url = "C:\\Users\\david\\Dropbox\\uni\\year_4\\AI\\assignment\\MultiThreadAI\\src\\resource\\" + dataFile + ".txt";
        }

        file = new File(url);
        rawData = null;
        ArrayList<Individual> tempInput = new ArrayList<>();

        try {
            Scanner scan = new Scanner(file);

            scan.nextLine();

            while (scan.hasNextLine()) {
                String str = scan.nextLine();

                String[] data = str.split(" ");

                int[] gene = getGene(data[dataPart]);
                int classification = Integer.parseInt(data[classificationPart]);

                tempInput.add(new Individual(gene, classification));
            }

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

        return getIndividualArray(tempInput);

    }

    private static int[] getGene(String str) {
        ArrayList<Integer> value = new ArrayList<>();
        int[] gene;

        for (char c : str.toCharArray()) {
            value.add(Character.getNumericValue(c));
        }

        gene = new int[value.size()];

        for (int i = 0; i != value.size(); i++) {
            gene[i] = (int) value.get(i);
        }

        return gene;
    }

    private static Individual[] getIndividualArray(ArrayList<Individual> tempInput) {
        rawData = new Individual[tempInput.size()];

        for (int i = 0; i != tempInput.size(); i++) {
            rawData[i] = tempInput.get(i);
        }

        return rawData;
    }

    public static void generateDataSets(double percentSeen, String dataFile) {
        rawData = FileReader.readFile(dataFile);

        int numberSeen = (int) (rawData.length * percentSeen);

        ArrayList<Individual> classification1 = new ArrayList<>();
        ArrayList<Individual> classification0 = new ArrayList<>();

        for (Individual indiv : rawData) {
            if (indiv.getClassification() == 1) {
                classification1.add(indiv);
            } else {
                classification0.add(indiv);
            }
        }

        ArrayList<Individual> classification1_seenSet = new ArrayList<>();
        ArrayList<Individual> classification0_seenSet = new ArrayList<>();

        int classification1Seen = (int) (classification1.size() * percentSeen);
        for (int i = 0; i != classification1Seen; i++) {
            int indiv = rand.nextInt(classification1.size());

            classification1_seenSet.add(classification1.get(indiv));
            classification1.remove(indiv);
        }

        int classification0Seen = (int) (classification0.size() * percentSeen);
        for (int i = 0; i != classification0Seen; i++) {
            int indiv = rand.nextInt(classification0.size());
            classification0_seenSet.add(classification0.get(indiv));
            classification0.remove(indiv);
        }

        System.out.println("Classification 0 Seen Set Size:" + classification0_seenSet.size() + "\t " + ((double) classification0_seenSet.size() / ((double) ((classification0_seenSet.size() + classification1_seenSet.size())) / 100.0)) + "%");
        System.out.println("Classification 1 Seen Set Size:" + classification1_seenSet.size() + "\t " + ((double) classification1_seenSet.size() / ((double) ((classification0_seenSet.size() + classification1_seenSet.size())) / 100.0)) + "%");

        trainingSet = new Individual[classification0_seenSet.size() + classification1_seenSet.size()];

        for (int i = 0; i != trainingSet.length; i++) {
            if ((classification1_seenSet.size() > 0) & (classification0_seenSet.size() > 0)) {
                if (rand.nextDouble() > 0.5) {
                    int indiv = rand.nextInt(classification0_seenSet.size());
                    trainingSet[i] = classification0_seenSet.get(indiv);
                    classification0_seenSet.remove(indiv);
                } else {
                    int indiv = rand.nextInt(classification1_seenSet.size());
                    trainingSet[i] = classification1_seenSet.get(indiv);
                    classification1_seenSet.remove(indiv);
                }
            } else if (classification1_seenSet.size() > 0) {
                int indiv = rand.nextInt(classification1_seenSet.size());
                trainingSet[i] = classification1_seenSet.get(indiv);
                classification1_seenSet.remove(indiv);
            } else if (classification0_seenSet.size() > 0) {
                int indiv = rand.nextInt(classification0_seenSet.size());
                trainingSet[i] = classification0_seenSet.get(indiv);
                classification0_seenSet.remove(indiv);
            }
        }

        System.out.println("Classification 0 Unseen Set Size:" + classification0.size() + "\t " + (double) (classification0.size() / ((double) (classification0.size() + classification1.size()) / 100.0)) + "%");
        System.out.println("Classification 1 Unseen Set Size:" + classification1.size() + "\t " + (double) (classification1.size() / ((double) (classification0.size() + classification1.size()) / 100)) + "%");

        unseenSet = new Individual[rawData.length - numberSeen];
        for (int i = 0; i != unseenSet.length; i++) {
            if ((classification1.size() > 0) & (classification0.size() > 0)) {
                if (rand.nextDouble() > 0.5) {
                    unseenSet[i] = classification1.get(0);
                    classification1.remove(0);
                } else {
                    unseenSet[i] = classification0.get(0);
                    classification0.remove(0);
                }
            } else if (classification1.size() > 0) {
                unseenSet[i] = classification1.get(0);
                classification1.remove(0);
            } else if(classification0.size() > 0) {
                unseenSet[i] = classification0.get(0);
                classification0.remove(0);
            }
        }

    }
}
