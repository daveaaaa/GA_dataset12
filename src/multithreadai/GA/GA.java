package multithreadai.GA;

import multithreadai.utils.Result;
import static multithreadai.MultiThreadAI.trainingSet;
import static multithreadai.MultiThreadAI.unseenSet;

/**
 *
 * @author David Armstrong
 */
public class GA {

    private Selection selection;
    private Mutation mutation;
    private CrossOver crossOver;
    private RuleSet[] population;

    public GA(int tournamentSize, double mutationRate, int crossoverPoint, int populationSize, int ruleSetSize, int geneLength,double crossoverPercentage,boolean randomCrossover) {
        selection = new Selection(tournamentSize);
        mutation = new Mutation(mutationRate);
        crossOver = new CrossOver(crossoverPoint, randomCrossover, crossoverPercentage);

        population = new RuleSet[populationSize];

        for (int i = 0; i != populationSize; i++) {
            population[i] = new RuleSet(ruleSetSize, geneLength);
        }
    }

    public int getFitness(Individual[] data) {
        RuleSet bestRule = getWinner(population);
        return  Fitness.getFitness(bestRule, unseenSet);
    }

    public Result[] run(int maxGenerations) {
        Result[] results = new Result[(maxGenerations / 10) + 1];

        for (int generation = 0; generation != maxGenerations; generation++) {
            if (generation % 10 == 0) {
                results[generation / 10] = new Result(generation, population);
            }

            population = selection.doSelection(population);
            population = crossOver.doCrossOver(population);
            population = mutation.doMutation(population);
            updateFitness();
        }
        results[(maxGenerations / 10)] = new Result(maxGenerations, population);
        return results;

    }

    private void updateFitness() {
       Fitness fitness = new Fitness();
       fitness.getFitness_trainingData(population);
    }
    
    private RuleSet getWinner(RuleSet[] competitors) {
        RuleSet bestRuleSet = null;
        int bestFitness = 0;

        for (RuleSet rule : competitors) {
            int fitness = rule.getFitness();
            if ((fitness >= bestFitness) || (bestRuleSet == null)) {
                bestFitness = fitness;
                bestRuleSet = rule;
            }
        }

        return bestRuleSet;
    }
}
