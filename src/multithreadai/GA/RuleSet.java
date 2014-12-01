/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadai.GA;

import multithreadai.GA.Individual;
import multithreadai.utils.IndividualFactory;

/**
 *
 * @author David Armstrong
 */
public class RuleSet {

    private Individual[] ruleSet;
    private int fitness;
    private boolean fitnessChanged;

    public RuleSet() {

    }

    public RuleSet(Individual[] rules) {
        this.ruleSet = rules;
        fitness = 0;
        fitnessChanged = true;
    }

    public RuleSet(int[] rules, int geneLength, int ruleCount) {
        this.ruleSet = new Individual[ruleCount];
        int i = 0;
        int ruleSetCount = 0;
        
        while (i != rules.length) {
            int[] newGene = new int[geneLength];

            for (int j = 0; j != geneLength; j++) {
                newGene[j] = rules[i];
                i++;
            }
            ruleSet[ruleSetCount] = new Individual(newGene, rules[i]);
            ruleSetCount++;
            i++;
        }
    }

    public RuleSet(int ruleSetSize, int geneSize) {
        ruleSet = new Individual[ruleSetSize];

        for (int i = 0; i != ruleSetSize; i++) {
            ruleSet[i] = IndividualFactory.GenerateNewIndividual(geneSize);
        }
        fitness = 0;
    }

    public void setRules(Individual[] rules) {
        this.ruleSet = rules;
        fitnessChanged = true;
    }

    public Individual[] getRules() {
        return ruleSet;
    }

    public int[] getRulesAsInt() {

        int[] rules = new int[ruleSet.length + (ruleSet.length * ruleSet[0].getGene().length)];

        int i = 0;
        for (Individual rule : ruleSet) {
            for (int j = 0; j != rule.getGene().length; j++) {
                rules[i] = rule.getGene()[j];
                i++;
            }
            rules[i] = rule.getClassification();
            i++;
        }

        return rules;
    }

    public void setRulesAsInt(int[] rules) {
        int i = 0;
        for (Individual rule : ruleSet) {
            int[] newGene = new int[rule.getGene().length];
            for (int j = 0; j != rule.getGene().length; j++) {
                newGene[j] = rules[i];
                i++;
            }
            rule.setGene(newGene);
            rule.setClassification(rules[i]);
            i++;
        }

        fitnessChanged = true;

    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public boolean getFitnessChanged() {
        return fitnessChanged;
    }

    public void setFitnessChanged(boolean fitnessChanged) {
        this.fitnessChanged = fitnessChanged;
    }

    public int getFitness() {
        return this.fitness;
    }

    public RuleSet createClone() {
        RuleSet clonedRuleSet = new RuleSet();
        Individual[] clonedRules = new Individual[ruleSet.length];

        for (int i = 0; i != ruleSet.length; i++) {
            clonedRules[i] = ruleSet[i].createClone();
        }

        clonedRuleSet.setRules(clonedRules);
        clonedRuleSet.setFitness(fitness);
        clonedRuleSet.setFitnessChanged(false);
        return clonedRuleSet;
    }

    public boolean isMatch(RuleSet otherRuleSet) {
        boolean isMatch = false;
        int sameCount = 0;

        for (Individual thisRule : ruleSet) {
            for (Individual otherRule : otherRuleSet.getRules()) {
                if (thisRule.isMatch(otherRule)) {
                    sameCount++;
                }
            }
        }

        if (sameCount == ruleSet.length) {
            isMatch = true;
        }

        return isMatch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(ruleSet.length * ruleSet[0].getGene().length);

        for (Individual rule : ruleSet) {
            sb.append(rule.toString());
            sb.append(" ");
        }

        return sb.toString();
    }

}
