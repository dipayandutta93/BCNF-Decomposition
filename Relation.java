package BCNF;

import java.util.*;

public class Relation {
    Set<Character> relation;
    int pos, size;
    Set<Set<Character>> pSet;
    List<Set<Character>> list;

    public Relation() {
        relation = new HashSet<>();
        pSet = new LinkedHashSet<>();
    }

    public Relation(Set<Character> r) {
        relation = new HashSet<>();
        relation.addAll(r);
        pSet = new LinkedHashSet<>();
    }

    public Relation(String r) {
        relation = new HashSet<>();

        char[] charArray = r.toCharArray();

        for (char i : charArray) {
            relation.add(i);
        }

        pos = 0;
        size = 0;
        pSet = new LinkedHashSet<>();
    }

    public Relation union(Relation setB) {
        Set<Character> tmp = new HashSet<Character>(relation);
        tmp.addAll(setB.getSet());
        return new Relation(tmp);
    }

    public Relation intersect(Relation setB) {
        Set<Character> tmp = new HashSet<Character>();
        for (Character x : relation)
            if (setB.contains(x))
                tmp.add(x);
        return new Relation(tmp);
    }

    public Set<Character> getSet() {
        return relation;
    }

    public void addRelation(Relation relationToAdd) {
        relation.addAll(relationToAdd.getSet());

    }


    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Character item : relation)
        {
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }

    public String toString(Set<Character> s) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Character item : s)
        {
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }

    public boolean equals(Relation rel2) {
        return rel2.getSet().equals(relation);
    }

    public boolean contains(Character c) {
        if (relation.contains(c))
            return true;
        return false;
    }

    private Set<Set<Character>> powerSet(Set<Character> originalSet) {
        Set<Set<Character>> sets = new LinkedHashSet<>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<Character>());
            return sets;
        }
        List<Character> list = new ArrayList<Character>(originalSet);
        Character head = list.get(0);
        Set<Character> rest = new LinkedHashSet<Character>(list.subList(1, list.size()));
        for (Set<Character> set : powerSet(rest)) {
            Set<Character> newSet = new LinkedHashSet<Character>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    public Relation powerSetFirst() {
        pSet = powerSet(relation);
        pSet.remove(new LinkedHashSet<>());
        list = new ArrayList<>();

        for (Set<Character> set : pSet)
            list.add(set);

        size = list.size();

        if(pos < size)
            return new Relation(list.get(0));
        return null;
    }

    public Relation powerSetNext() {

        pos++;
        if(pos < size)
            return new Relation(list.get(pos));
        return null;
    }

    public boolean subset(Relation r2){
        return relation.containsAll(r2.getSet());
    }

    public Relation minus(Relation relationToSubtract){
        relation.removeAll(relationToSubtract.getSet());
        return new Relation(relation);
    }

}