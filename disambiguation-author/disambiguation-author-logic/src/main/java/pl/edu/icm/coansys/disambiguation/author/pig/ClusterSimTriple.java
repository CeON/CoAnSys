/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.disambiguation.author.pig;

/**
 *
 * @author kura
 */
public class ClusterSimTriple {
    
    public int a;
    public int b;
    public float sim;

    public ClusterSimTriple(int a, int b, float sim) {
        this.a = a;
        this.b = b;
        this.sim = sim;
    }
    
}
