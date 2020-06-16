package Server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Problem implements Serializable{
	public String problem;
	public String answer;
	
	public Problem(String problem,String answer) {
		this.problem = problem;
		this.answer = answer;
	}

	public static void main(String[] args) {
		Problem p1 = new Problem("Question1.'?'에 무엇이 들어갈까?\n1 = 5\n2 = 7\n3 = 9\n4 = 13\n5 = ?","1");
		Problem p2 = new Problem("Question2.컴퓨터 암호의 힌트이다. 암호는 무엇일까?\n1 2 3 6 9 8 7 4 5 6\n9 6 3 2 1 4 7\n3 2 1 4 7 8 9 6 5 4\n"
									+ "1 2 3 6 5 4 7 8 9\n9 8 7 4 5 6 3 2 1","guess");
		Problem p3 = new Problem("Question3.a와 b가 의미하는 것은?\n    명태           중국\n    ----    	        ----\n입 | a | 물    단 | b | 머니\n"
				+ "    ----        	    ----\n     왕            깨","국어");
		Problem p4 = new Problem("Question4.수식을 계산해 답을 구하라\n 5 0 0 0 8 5 5\n-  7 1 1 1\n+  1\n----------------\n\n _ _ _ _ _ _ _","success");
		
		try(FileOutputStream fos = new FileOutputStream("Problem.dat");
				ObjectOutputStream os = new ObjectOutputStream(fos)){
			os.writeObject(p1);
			os.writeObject(p2);
			os.writeObject(p3);
			os.writeObject(p4);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Problem.dat"));
			Problem p;
			while((p = (Problem)ois.readObject())!=null) {
				System.out.println(p);
				System.out.println();
			}
		}
		catch (Exception e) {
		}
	}
	
	@Override
	public String toString() {
		return this.problem+"\n"+this.answer;
	}

}
