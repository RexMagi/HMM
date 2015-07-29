package glados;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import marcovModel.BaumWelch;
import marcovModel.HMM;
import distributions.CategoricalDistribution;
import distributions.Distribution;
import distributions.Observation;

public class Test1 {

	public static void main(String[] args) throws FileNotFoundException {
		File data = new File("C:\\Users\\Lab User\\Desktop\\test.txt");
		Scanner eyes = new Scanner(data);
		ArrayList<Observation> book = new ArrayList<Observation>();
		int c = 0;
		boolean test =true;
		while(test/*eyes.hasNext()*/){
			String temp = eyes.nextLine();
			temp= temp.toLowerCase();
			for(char x:temp.toCharArray()){
				c++;
				if(c>=20)
					test =false;
				book.add(new Observation(x));
			}	
	
		}
		BigDecimal [][] a = {
				{new BigDecimal(0.47468),new BigDecimal(0.52532)},
				{new BigDecimal(0.51656),new BigDecimal(0.48344)}};

		BigDecimal [] pi = {new BigDecimal(0.51316),new BigDecimal(0.48684)};
		HashMap<Observation,BigDecimal> Table = new HashMap<Observation,BigDecimal>();
		ArrayList<Distribution> distributionSet = new ArrayList<Distribution>();
		Table.put(new Observation('a'),BigDecimal.valueOf(0.03735));
		Table.put(new Observation('b'),BigDecimal.valueOf(0.03408));
		Table.put(new Observation('c'),BigDecimal.valueOf(0.03455));
		Table.put(new Observation('d'),BigDecimal.valueOf(0.03828));
		Table.put(new Observation('e'),BigDecimal.valueOf(0.03782));
		Table.put(new Observation('f'),BigDecimal.valueOf(0.03922));
		Table.put(new Observation('g'),BigDecimal.valueOf(0.03688));
		Table.put(new Observation('h'),BigDecimal.valueOf(0.03408));
		Table.put(new Observation('i'),BigDecimal.valueOf(0.03875));
		Table.put(new Observation('j'),BigDecimal.valueOf(0.04062));
		Table.put(new Observation('k'),BigDecimal.valueOf(0.03735));
		Table.put(new Observation('l'),BigDecimal.valueOf(0.03968));
		Table.put(new Observation('m'),BigDecimal.valueOf(0.03548));
		Table.put(new Observation('n'),BigDecimal.valueOf(0.03735));
		Table.put(new Observation('o'),BigDecimal.valueOf(0.04062));
		Table.put(new Observation('p'),BigDecimal.valueOf(0.03595));
		Table.put(new Observation('q'),BigDecimal.valueOf(0.03641));
		Table.put(new Observation('r'),BigDecimal.valueOf(0.03408));
		Table.put(new Observation('s'),BigDecimal.valueOf(0.04062));
		Table.put(new Observation('t'),BigDecimal.valueOf(0.03548));
		Table.put(new Observation('u'),BigDecimal.valueOf(0.03922));
		Table.put(new Observation('v'),BigDecimal.valueOf(0.04062));
		Table.put(new Observation('w'),BigDecimal.valueOf(0.03455));
		Table.put(new Observation('x'),BigDecimal.valueOf(0.03595));
		Table.put(new Observation('y'),BigDecimal.valueOf(0.03408));
		Table.put(new Observation('z'),BigDecimal.valueOf(0.03408));
		Table.put(new Observation(' '),BigDecimal.valueOf(0.03688));
		distributionSet.add(new CategoricalDistribution(Table));

		Table = new HashMap<Observation,BigDecimal>();	
		Table.put(new Observation('a'),BigDecimal.valueOf(0.03909));
		Table.put(new Observation('b'),BigDecimal.valueOf(0.03537));
		Table.put(new Observation('c'),BigDecimal.valueOf(0.03537));
		Table.put(new Observation('d'),BigDecimal.valueOf(0.03909));
		Table.put(new Observation('e'),BigDecimal.valueOf(0.03583));
		Table.put(new Observation('f'),BigDecimal.valueOf(0.03630));
		Table.put(new Observation('g'),BigDecimal.valueOf(0.04048));
		Table.put(new Observation('h'),BigDecimal.valueOf(0.03537));
		Table.put(new Observation('i'),BigDecimal.valueOf(0.03816));
		Table.put(new Observation('j'),BigDecimal.valueOf(0.03909));
		Table.put(new Observation('k'),BigDecimal.valueOf(0.03490));
		Table.put(new Observation('l'),BigDecimal.valueOf(0.03723));
		Table.put(new Observation('m'),BigDecimal.valueOf(0.03537));
		Table.put(new Observation('n'),BigDecimal.valueOf(0.03909));
		Table.put(new Observation('o'),BigDecimal.valueOf(0.03397));
		Table.put(new Observation('p'),BigDecimal.valueOf(0.03397));
		Table.put(new Observation('q'),BigDecimal.valueOf(0.03816));
		Table.put(new Observation('r'),BigDecimal.valueOf(0.03676));
		Table.put(new Observation('s'),BigDecimal.valueOf(0.04048));
		Table.put(new Observation('t'),BigDecimal.valueOf(0.03443));
		Table.put(new Observation('u'),BigDecimal.valueOf(0.03537));
		Table.put(new Observation('v'),BigDecimal.valueOf(0.03955));
		Table.put(new Observation('w'),BigDecimal.valueOf(0.03816));
		Table.put(new Observation('x'),BigDecimal.valueOf(0.03723));
		Table.put(new Observation('y'),BigDecimal.valueOf(0.03769));
		Table.put(new Observation('z'),BigDecimal.valueOf(0.03955));
		Table.put(new Observation(' '),BigDecimal.valueOf(0.03397));
		distributionSet.add(new CategoricalDistribution(Table));
		HMM model = new HMM(a,pi,distributionSet);
		BaumWelch teacher = new BaumWelch(book,model);

		teacher.learn(100);
		eyes.close();

	}

}
