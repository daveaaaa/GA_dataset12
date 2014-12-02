package multithreadai.utils;

import java.util.ArrayList;
import multithreadai.GA.Fitness;
import multithreadai.GA.Individual;
import multithreadai.GA.RuleSet;

/**
 *
 * @author david
 */
public class Result {

    private int populationFitness = 0;
    private String bestRule;
    private int generation;
    private int populationSize;
    private int bestFitness;
    private int uniqueRulesCount = 0;
    private String uniqueRules;
    private int bestRuleCount = 0;
    private int uniqueBestRule = 0;

    public int getUniqueRulesCount() {
        return uniqueRulesCount;
    }

    public Result(int generation, RuleSet[] ruleSet, Individual[] data) {
        this.generation = generation;

        Fitness fitness = new Fitness();
        fitness.getFitness_unseenData(ruleSet, data);

        for (RuleSet rules : ruleSet) {
            populationFitness += rules.getFitness();
        }

        populationSize = ruleSet.length;

        bestRule = getBestRule(ruleSet);
        countUniqueRules(ruleSet);
    }

    public Result(int generation, RuleSet[] ruleSet) {
        this.generation = generation;

        Fitness fitness = new Fitness();
        fitness.getFitness_trainingData(ruleSet);

        for (RuleSet rules : ruleSet) {
            populationFitness += rules.getFitness();
        }

        populationSize = ruleSet.length;

        bestRule = getBestRule(ruleSet);
        countUniqueRules(ruleSet);
    }

    private String getBestRule(RuleSet[] ruleSet) {
        ArrayList<RuleSet> bestRules = new ArrayList<>();
        bestFitness = 0;
        StringBuilder sb = new StringBuilder(300);

        for (RuleSet rule : ruleSet) {
            int ruleFitness = rule.getFitness();

            if (ruleFitness == bestFitness) {
                bestRuleCount++;
                bestRules.add(rule);
            } else if ((ruleFitness > bestFitness)) {
                bestRules.clear();
                bestRules.add(rule);
                bestRuleCount = 1;
                uniqueBestRule = 1;
                bestFitness = ruleFitness;
            }
        }

        for (RuleSet rule : bestRules) {
            sb.append(rule.toString());
        }
        return sb.toString();
    }

    private void countUniqueRules(RuleSet[] ruleSet) {

        StringBuilder sb = new StringBuilder(300);

        for (int i = 0; i != ruleSet.length; i++) {
            if (!(sb.toString().contains(ruleSet[i].toString()))) {
                sb.append(ruleSet[i]).toString();
                uniqueRulesCount++;
            }
        }
        uniqueRules = sb.toString();

    }

    public static String fullInfoHeader() {
        StringBuilder sb = new StringBuilder(120);

        sb.append("Generation");
        sb.append("\t");
        sb.append("RuleSet Population Fitness");
        sb.append("\t");
        sb.append("Unique Rules");
        sb.append("\t");
        sb.append("Unique RuleSet Population");
        sb.append("\t");
        sb.append("Best Rules");

        return sb.toString();
    }

    public String fullInfo() {

        StringBuilder sb = new StringBuilder(800);

        sb.append(generation);
        sb.append("\t");
        sb.append(populationFitness);
        sb.append("\t");
        sb.append(uniqueRulesCount);
        sb.append("\t");
        sb.append(uniqueRules);
        sb.append("\t");
        sb.append(bestRule);
        return sb.toString();
    }

    public static String shortInfoHeader() {
        StringBuilder sb = new StringBuilder(120);
        sb.append("Generation");
        sb.append("\t");
        sb.append("RuleSet Population Fitness");
        sb.append("\t");
        sb.append("RuleSet Population Size");
        sb.append("\t");
        sb.append("Unique RuleSet Count");
        sb.append("\t");
        sb.append("Best Rule Count");
        sb.append("\t");
        sb.append("Unique Best Rule Count");
        sb.append("\t");
        sb.append("Best Fitness");

        return sb.toString();
    }

    public String shortInfo() {
        StringBuilder sb = new StringBuilder(40);

        sb.append(generation);
        sb.append("\t");
        sb.append(populationFitness);
        sb.append("\t");
        sb.append(populationSize);
        sb.append("\t");
        sb.append(uniqueRulesCount);
        sb.append("\t");
        sb.append(bestRuleCount);
        sb.append("\t");
        sb.append(uniqueBestRule);
        sb.append("\t");
        sb.append(bestFitness);

        return sb.toString();
    }

    public static String uniqueRuleHeader() {
        StringBuilder sb = new StringBuilder(40);

        sb.append("Generation");
        sb.append("\t");
        sb.append("Population Fitness");
        sb.append("\t");
        sb.append("Population Size");
        sb.append("\t");
        sb.append("Unique Rule Count");
        sb.append("\t");
        sb.append("Unique Rules");

        return sb.toString();
    }

    public String uniqueRules() {
        StringBuilder sb = new StringBuilder(40);

        sb.append(generation);
        sb.append("\t");
        sb.append(populationFitness);
        sb.append("\t");
        sb.append(populationSize);
        sb.append("\t");
        sb.append(uniqueRulesCount);
        sb.append("\t");
        sb.append(uniqueRules);

        return sb.toString();
    }

    public static String bestRulesHeader() {
        StringBuilder sb = new StringBuilder(40);

        sb.append("Generation");
        sb.append("\t");
        sb.append("Population Fitness");
        sb.append("\t");
        sb.append("Population Size");
        sb.append("\t");
        sb.append("Best Rule Count");
        sb.append("\t");
        sb.append("Unique Best Rule Count");
        sb.append("\t");
        sb.append("Best Fitness");
        sb.append("\t");
        sb.append("Best Rule(s)");

        return sb.toString();
    }

    public String bestRule() {
        StringBuilder sb = new StringBuilder(40);

        sb.append(generation);
        sb.append("\t");
        sb.append(populationFitness);
        sb.append("\t");
        sb.append(populationSize);
        sb.append("\t");
        sb.append(bestRuleCount);
        sb.append("\t");
        sb.append(uniqueBestRule);
        sb.append("\t");
        sb.append(bestFitness);
        sb.append("\t");
        sb.append(bestRule);

        return sb.toString();

    }
}
