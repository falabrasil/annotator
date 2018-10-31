import java.io.File;
import ufpa.falabrasil.Cross;
import ufpa.falabrasil.GraphemeToPhoneme;
import ufpa.falabrasil.Syllabificator;
import ufpa.falabrasil.StressVowel;
public class TestG2P{
	public static void main(String[] args){
		if(args.length == 1){
			System.out.println(new StressVowel().findStress(args[0]));
			System.out.println(new Syllabificator().syllabs(args[0]));
			System.out.println(new GraphemeToPhoneme().g2p(args[0]));
		}
		else if(args.length == 2){
			try{
				Cross test = new Cross();
				if(args.length == 2){
					test.setASCII(Boolean.parseBoolean(args[1]));
					if(new File(args[0]).exists() && !new File(args[0]).isDirectory())
							test.setInputAsFile(new File(args[0]));
					else
						test.setInputAsArray(args[0].split(" "));
				}
				else{
					System.out.println("Por favor, utilize apenas dois (2) argumentos.");
				}
				int anterior = 0;
				for(int i = 0; i < test.getCrossTextIndex().length; i++){
					System.out.println(""
					+	   test.getInputText()[anterior]+" "
					+	   test.getInputText()[test.getCrossTextIndex()[i]]+"\t"
					+	   test.getOutput()[i]+" -LINE:"
					+	   test.getCrossTextIndex()[i]
					);
					anterior = test.getCrossTextIndex()[i]+1;
				}
			}
			catch(Exception e){helpMessage();}
		}
		else
			helpMessage();
	}
	public static void helpMessage(){
		System.out.println("-1 argumento aplica StressVowel, Syllabificator e "
			+"\nGraphemeToPhoneme no argumento."
			+"\n-2 argumentos, o primeiro deve ser nome de arquivo, o segundo "
			+"\ndeve ser booleano, aplica tratamento de crossword em arquivo "
			+"\nde texto pleno contendo uma palavra do portugues brasileiro por"
			+"\nlinha.");
	}
}
