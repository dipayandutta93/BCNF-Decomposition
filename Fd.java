package BCNF;

public class Fd {
    Relation l, r;

    public boolean BCNFviolation(Relation s, FDList keys){
        boolean contains = false;
        for (Fd key: keys.getFdsAsList()){
            if (l.getSet().containsAll(key.getLHS().getSet())){
                contains = true;
                break;
            }
        }
        return !contains;
    }

    public Fd(Relation in_lhs, Relation in_rhs) {
        l = in_lhs; r= in_rhs;
    }

    public String toString(){
        return l.toString() + " -> " + r.toString();
    }


    public Relation getLHS(){
        return l;
    }

    public Relation getRHS(){
        return r;
    }
}
