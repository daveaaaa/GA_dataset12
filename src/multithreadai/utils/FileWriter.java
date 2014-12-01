/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadai.utils;

import multithreadai.utils.Result;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author david
 */
public class FileWriter {

    public static void WriteFile(String path, String fileName, HeaderResults header) {
        try {
            File directory = new File(path);
            directory.mkdirs();
            fileName = (fileName + ".txt");
            File result = new File(path, fileName);
            java.io.FileWriter writer;
            writer = new java.io.FileWriter(result);
            writer.append(header.toString());
            writer.flush();
            writer.close();
        } catch (IOException ioE) {
            System.exit(1);
        }
    }

    public static void WriteFile(String path, String fileName, Result[] results, int run, int unseenFitness) {
        createShortResults(path, fileName, results, run, unseenFitness);
    }

    private static void createShortResults(String path, String fileName, Result[] results, int run, int unseenFitness) {
        try {
            File directory = new File(path);
            directory.mkdirs();
            fileName = (fileName + ".txt");
            File resultFile = new File(path, fileName);
            java.io.FileWriter writer;
            if (run == 0) {
                writer = new java.io.FileWriter(resultFile, false);
            } else {
                writer = new java.io.FileWriter(resultFile, true);
            }
            writer.append("Run:" + run + "\t" + "Unseen Fitness: " + unseenFitness + "\n");
            writer.append(Result.shortInfoHeader());
            writer.append("\n");
            for (Result result : results) {
                writer.append(result.shortInfo());
                writer.append("\t");
                writer.append(result.bestRule());
                writer.append("\n");
            }
            writer.append("\n");
            writer.flush();
            writer.close();

        } catch (IOException ioE) {
            ioE.printStackTrace(System.out);
            System.exit(1);
        }
    }

    private static void createUniqueResults(String path, Result[] results, int run, int unseenFitness) {
        try {

            File shortResults = new File(path + "uniqueResults" + run + ".txt");
            java.io.FileWriter writer = new java.io.FileWriter(shortResults, false);
            writer.append("Run:" + run + "Unseen Fitness: " + unseenFitness + "\n");
            writer.append(Result.uniqueRuleHeader());
            writer.append("\n");
            for (Result result : results) {
                writer.append(result.uniqueRules());
                writer.append("\n");
            }

            writer.flush();
            writer.close();

        } catch (IOException ioE) {
            ioE.printStackTrace(System.out);
            System.exit(1);
        }
    }

    private static void createBestResults(String path, Result[] results, int run, int unseenFitness) {
        try {

            File shortResults = new File(path + "bestResults" + run + ".txt");
            java.io.FileWriter writer = new java.io.FileWriter(shortResults, false);
            writer.append("Run:" + run + "Unseen Fitness: " + unseenFitness + "\n");
            writer.append(Result.bestRulesHeader());
            writer.append("\n");
            for (Result result : results) {
                writer.append(result.bestRule());
                writer.append("\n");
            }

            writer.flush();
            writer.close();

        } catch (IOException ioE) {
            ioE.printStackTrace(System.out);
            System.exit(1);
        }
    }

    private static void createFullResults(String path, Result[] results, int run, int unseenFitness) {
        try {

            File shortResults = new File(path + "fullResults" + run + ".txt");
            java.io.FileWriter writer = new java.io.FileWriter(shortResults, false);
            writer.append("Run:" + run + "Unseen Fitness: " + unseenFitness + "\n");
            writer.append(Result.fullInfoHeader());
            writer.append("\n");
            for (Result result : results) {
                writer.append(result.fullInfo());
                writer.append("\n");
            }

            writer.flush();
            writer.close();

        } catch (IOException ioE) {
            ioE.printStackTrace(System.out);
            System.exit(1);
        }
    }

}
