package solver;

/**
 * Created by Hallvard on 18.11.2014.
 */
public class AI {

    public AI(){
        Expectimax em = new Expectimax();
        Direction next;
        while((next = em.nextMove(null)) == null) {
            //TODO
        }
    }
}
