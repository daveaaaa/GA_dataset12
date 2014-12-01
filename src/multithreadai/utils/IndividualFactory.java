/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadai.utils;

import multithreadai.GA.Individual;

/**
 *
 * @author david
 */
public class IndividualFactory {
    
    public static Individual GenerateNewIndividual(int geneLength){
        int[] newGene = new int[geneLength];
        int value = 0;
        for(int i = 0; i != geneLength; i++){
            if(Math.random() > 0.8){
                newGene[i] = Individual.HASH_SIGN;
            } else if (Math.random() > 0.5){
                newGene[i] = 1; 
            } else {
                newGene[i] = 0;
            }
        }
        
        if(Math.random() > 0.5){
            value = 1;
        }
        
        return new Individual(newGene,value);
    }
    
}
