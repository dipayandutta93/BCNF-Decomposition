import java.util.*;
import java.io.File;
import java.util.Scanner;
import BCNF.*;

public class driver {
	private Relation a;

	public static void main(String[] args) throws Exception {
		Stack<Relation> s = new Stack<Relation>();
		FDList fdlist = new FDList();
		RelList db = new RelList();
		boolean first = true;
		Relation r = null;

		//Read the input file
		File myFile = new File("/Users/dipayandutta/IdeaProjects/CS_539/src/one.txt");
		Scanner sc = new Scanner(myFile);
		int counter = 0;
		while (sc.hasNextLine()) {
			String str = sc.nextLine();
			if (first)
				r = new Relation(str.replace(" ", ""));
			else {
				int pos = str.indexOf("->");
				if (pos > 0) {
					String lhs = str.substring(0, pos).replace(" ", "");
					String rhs = str.substring(pos + 2, str.length()).replace(" ", "");
					fdlist.insert(new Fd(new Relation(lhs), new Relation(rhs)));
				}
			}
			first = false;
		}
		sc.close();

		System.out.println("Relations:");

		System.out.println(r);
		System.out.println("-----------");
		System.out.println("FD's:");
		System.out.println(fdlist);
		System.out.println("-----------");


		// compute complete list of functional dependencies
		FDList completeF = new FDList();

		Relation new_rel = r.powerSetFirst();
		while (new_rel != null) {
			Relation clos = fdlist.closure(new_rel);
			if (!new_rel.equals(clos)) {
				completeF.insert(new Fd(new_rel, clos));
			}
			new_rel = r.powerSetNext();
		}
		while (new_rel != null) ;
		System.out.println(completeF);

		System.out.println("------------------------");

		// compute superkeys
		FDList super_keys = new FDList();
		Fd my_fd = completeF.getFirst();
		while (my_fd != null) {
			Relation lr = my_fd.getLHS().union(my_fd.getRHS());
			if (r.equals(lr)) {
				super_keys.insert(my_fd);
				System.out.println("superkey: " + my_fd);
			}
			my_fd = completeF.getNext();
		}

		System.out.println("------------------------");

		// compute keys until minimal set is obtained
		int size_old, size_new;
		FDList keys = new FDList();
		FDList toCheck = super_keys;
		FDList keys_final = new FDList();
		my_fd = toCheck.getFirst();    //which fd list
		size_old = super_keys.getFdsAsList().size();
		size_new = keys.getFdsAsList().size() + 1;
		while (size_new != size_old && size_new != 0) {
			System.out.println("--------------------------------------");
			my_fd = toCheck.getFirst();    //which fd list
			keys = new FDList();
			size_old = toCheck.getFdsAsList().size();
			while (my_fd != null) {
				boolean minimal = false;
				Relation lh = my_fd.getLHS();
				if (toCheck.inLhs(lh)) {        //which fd list
					System.out.println("driver.main" + lh);
					minimal = true;
				}
				if (minimal) {
					System.out.println("Key: " + my_fd);
					keys.insert(my_fd);
				}
				my_fd = toCheck.getNext();
			}
			size_new = keys.getFdsAsList().size();
			keys_final = toCheck;
			toCheck = keys;
		}

		keys = keys_final;

		boolean violation = false;
		int count = 0;
		for (Fd f : fdlist.getFdsAsList()) {
//			System.out.println("args = [" + count + "]");
			if (f.BCNFviolation(r, super_keys)) {
				System.out.println("BCNF violation: " + f);
			}
		}



		// BCNF DECOMPOSITION:
		Fd f;
		s.push(r);
		while (!s.isEmpty()){
			Relation a= new Relation(s.pop().toString());


			//recalculate complete set
			FDList newCompleteF = new FDList();

			Relation newRelation = a.powerSetFirst();
			while (newRelation != null) {
				FDList newFDList = projection(a,new FDList(fdlist.getFdsAsList()));
				Relation clos = newFDList.closure(newRelation);
				if (!newRelation.equals(clos)) {
					newCompleteF.insert(new Fd(newRelation, clos));
				}
				newRelation = a.powerSetNext();
			}
			while (newRelation != null) ;


			//re-compute superkeys for each a
			FDList newSK = new FDList();
			FDList newFDs = projection(a, new FDList(newCompleteF.getFdsAsList()));
			if (newFDs.isEmpty()){
				db.insert(a);
				break;
			}
			Fd new_my_fd = newFDs.getFirst();
			while (new_my_fd != null) {
				Relation lr = new_my_fd.getLHS().union(new_my_fd.getRHS());
				if (a.equals(lr)) {
					newSK.insert(new_my_fd);
					System.out.println("superkey: " + new_my_fd);
				}
				new_my_fd = newFDs.getNext();
			}



			violation=false;
			FDList new_list = projection(a,new FDList(fdlist.getFdsAsList()));
			if (new_list!=null) {
				f = new_list.getFirst();
				while (!violation && f != null) {
					if (f.BCNFviolation(a, newSK))
						violation = true;
					else
						f = new_list.getNext();
				}
				if (!violation) {
					System.out.println(a);
					System.out.println("--- inserted --- (" + a + ")");
					db.insert(a);
//					System.out.println("args =");
				} else if (f != null) {
					String x = (f.getLHS()).toString();
					Relation rhs = new Relation(((f.getRHS()).intersect(a)).toString());
					String y = rhs.toString();
					Relation xy = new Relation(x + y);

					s.push(a.minus(f.getRHS()));
					s.push(xy);
				}
			}
		}

		System.out.println("Tables in BCNF");
		System.out.println(db);
	}

	public static FDList projection(Relation r, FDList fds){
		FDList fdsCopy = new FDList(fds.getFdsAsList());
		Relation isPresent = new Relation();
		FDList returnList = new FDList();

		for(Fd fd : fdsCopy.getFdsAsList()){
			isPresent = new Relation(fd.getLHS().toString());
			isPresent.addRelation(fd.getRHS());
			if (r.subset(isPresent)){
				returnList.insert(fd);
			}
		}
		return returnList;
	}
	
}
