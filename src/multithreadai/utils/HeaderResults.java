package multithreadai.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author David Armstrong
 */
public class HeaderResults {

    private String datafile;
    private double mutationRate;
    private double trainingSetPercent;
    private int maxGenerations;
    private int ruleSetSize;
    private int populationSize;
    private int competitorSize;
    private ArrayList<String> runInformation;
    
    public HeaderResults(String datafile, double mutationRate, double trainingSetPercent, int maxGenerations, int ruleSetSize, int populationSize, int competitorSize) {
    this.datafile = datafile;
    this.mutationRate = mutationRate;
    this.trainingSetPercent = trainingSetPercent;
    this.maxGenerations = maxGenerations;
    this.ruleSetSize = ruleSetSize;
    this.competitorSize = competitorSize;
    this.populationSize = populationSize;
    runInformation = new ArrayList<>();
    }

    public void addRunInformation(int run, double intiPercentFitness, double finalPercentFitness){
        runInformation.add(run + "\t" +  new DecimalFormat("#.00").format(intiPercentFitness)+ "\t" +  new DecimalFormat("#.00").format(finalPercentFitness)); 
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DataSet:\t" + datafile + "\n");
        sb.append("Mutation Rate:\t" + mutationRate + "\n");
        sb.append("Training Set Percent \t" + (100 * trainingSetPercent) + "%" + "\n");
        sb.append("Generation: \t" + maxGenerations + "\n");
        sb.append("RuleSet Size:\t" + ruleSetSize + "\n");
        sb.append("Population Size:\t" + populationSize + "\n");
        sb.append("Competitor Size:\t" + competitorSize + "\n");
        sb.append( "\n");

        sb.append("Run:\t Initial Fitness:\t Final Fitness: \t").append("\n");
        for(String runs : runInformation){
            sb.append(runs + "\n");
        }
        
        return sb.toString();
    }

}
