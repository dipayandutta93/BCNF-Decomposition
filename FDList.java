package BCNF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FDList {
    List<Fd> fds;
    int pos;

    public FDList(){
        fds = new ArrayList<Fd>();
        pos = 0;
    }

    public Fd getFirst(){
        pos = 0;
        return fds.get(pos);
    }

    public Fd getNext(){
        pos++;
        if(pos<fds.size()){

        }
        else{
            return null;
        }
        return fds.get(pos);
    }

    public FDList(List<Fd> fds1){
        fds = new ArrayList<>();
        for (Fd fd: fds1){
            fds.add(fd);
        }
        pos=0;
    }

    public String toString(){
        String op = null;

        for(Fd f:fds){
            op+=f+"\n";
        }
        return op;
    }

    public void insert(Fd f){
        fds.add(f);
    }

    public Relation closure(Relation r){
        Relation result = new Relation();
        result.addRelation(r);

        boolean found = true;
        while(found){
            found = false;
            for(Fd fd : fds){
                if(result.getSet().containsAll(fd.getLHS().getSet()) && !result.getSet().containsAll(fd.getRHS().getSet())){
                    result.addRelation(fd.getRHS());
                    found = true;
                }
            }
        }


        return result;
    }

    public boolean inLhs(Relation r){
        int count=0;
        for(Fd fd : fds) {
            if(fd.getLHS().subset(r)) {
                count++;
            }
        }
        return (count>1);
    }

    public List<Fd> getFdsAsList(){
        return fds;
    }

    public boolean isEmpty(){
        return (fds.size()==0);
    }

}
