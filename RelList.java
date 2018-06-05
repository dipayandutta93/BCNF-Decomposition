package BCNF;

import java.util.ArrayList;
import java.util.List;

public class RelList {
    List<Relation> RL;
    int pos;
    public RelList(){
        RL = new ArrayList<Relation>();
        pos=0;
    }

    public void insert(Relation r){
        RL.add(r);
    }

    private Relation getFirst(){
        pos = 0;
        return RL.get(pos);
    }

    private Relation getNext(){
        pos++;
        if(pos<RL.size())
            System.out.println(RL.get(pos).toString());
        else{
            pos=0;
        }
        return RL.get(pos);
    }

    public String toString(){
        String str = null;

        for(Relation r:RL){
            str+=r+"\n";
        }
        return str;
    }

}
