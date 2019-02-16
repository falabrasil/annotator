import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import ufpa.falabrasil.GraphemeToPhoneme;
import ufpa.falabrasil.Syllabificator;
import ufpa.falabrasil.StressVowel;
import ufpa.falabrasil.Cross;
import ufpa.Flags;
import ufpa.Filehandler;
public class Runlib{
	private final char[] pflags = {'i','o','c','a','v','s','h','g','C','-'};
	private final String[] eflags = {
		"input","output","cross","ascii","vowel","syllab","help","g2p"};
	private GraphemeToPhoneme g = new GraphemeToPhoneme();
	private Syllabificator    s = new Syllabificator();
	private StressVowel       v = new StressVowel();
	private Cross             c = new Cross();
	private Flags             f = new Flags(pflags);
	private Filehandler       a = new Filehandler();
	public static void main(String[] args){
		Runlib run = new Runlib();
		//expande as flags
		for(int i = 0; i < run.eflags.length; i++){
			run.f.expandFlag(run.pflags[i], run.eflags[i]);
		}
		//configura as flags usadas pelo usuário
		args = run.f.setupFlags(args);
		run.validateFlagUsage(args.length);
		//se não estiver usando crossword
		if(!run.f.hasFlag('c')){
			//se entrada for arquivo
			if(run.f.hasFlag('i')){
				//se saída for arquivo
				if(run.f.hasFlag('o')) run.forFile(args[0],args[1]);
				//se saída for System.out
				else run.forFile(args[0],null);
			}
			//se entrada for palavra
			else{
				//se saída for arquivo
				if(run.f.hasFlag('o')) run.forWord(args[0],args[1]);
				//se saída for System.out
				else run.forWord(args[0],null);
			}
		}
		//se estiver usando crossword
		else{
			//saída para arquivo
			if(run.f.hasFlag('o')) run.forCrosswrd(args[0], args[1]);
			//saída para System.out
			else run.forCrosswrd(args[0], null);
		}
		System.exit(0);
	}
	//fim do main
	//entrada é palavra
	private void forWord(String palavra, String saveNome){
		String saida = "";
		if(this.f.hasFlag('g') && this.f.hasFlag('a'))
			saida += this.g.g2pLA(palavra)+"\t";
		else if(this.f.hasFlag('g'))
			saida += this.g.g2p(palavra)+"\t";
		if(this.f.hasFlag('s'))
			saida += this.s.syllabs(palavra)+"\t";
		if(this.f.hasFlag('v'))
			saida += this.v.findStress(palavra);
		//se saída for arquivo
		if(this.f.hasFlag('o')){
			ArrayList<String> outText = new ArrayList<String>();
			outText.add(palavra + "\t" + saida.trim());
			this.a.saveFile(saveNome, outText);
		}
		//se saída for System.out
		else{
			System.out.println(palavra + "\t" + saida.trim());
		}
	}
	//entrada é arquivo
	private void forFile(String loadNome, String saveNome){
		ArrayList<String> inText = this.a.loadFile(loadNome);
		ArrayList<String> outText = new ArrayList<String>();
		for(int i = 0; i < inText.size(); i++){
			String palavra = inText.get(i);
			String saida = "";
			if(this.f.hasFlag('g') && this.f.hasFlag('a'))
				saida += this.g.g2pLA(palavra)+"\t";
			else if(this.f.hasFlag('g'))
				saida += this.g.g2p(palavra)+"\t";
			if(this.f.hasFlag('s'))
				saida += this.s.syllabs(palavra)+"\t";
			if(this.f.hasFlag('v'))
				saida += this.v.findStress(palavra);
			outText.add(palavra + "\t" + saida.trim());
		}
		//se saída for arquivo
		if(this.f.hasFlag('o')){
			this.a.saveFile(saveNome,outText);
		}
		//se saída for System.out
		else{
			for(int i = 0; i < outText.size(); i++){
				System.out.println(outText.get(i));
			}
		}
	}
	//crossword
	private void forCrosswrd(String nomeArqEn, String nomeArqSa){
		ArrayList<String> outText = new ArrayList<String>();
		this.c.setASCII(this.f.hasFlag('a'));
		//caso entrada seja arquivo
		if(this.f.hasFlag('i')){
			ArrayList<String> inText = this.a.loadFile(nomeArqEn);
			this.c.setInputAsArray(inText.toArray(new String[inText.size()]));
		}
		//caso entrada seja string separa em array e manda pro cross
		else
			this.c.setInputAsArray(nomeArqEn.split(" "));
		//se o usuário quiser detalhes
		if(this.f.hasFlag('C')){
			int anterior = 0;
			for(int i = 0; i < this.c.getOutput().length; i++){
				//monta a lista de palavras que foram unidas
				int proximo = this.c.getCrossTextIndex()[i];
				String palavras = "[";
				for(int j = anterior; j <= proximo; j++)
					palavras += this.c.getInputText()[j]+",";
				palavras = palavras.substring(0,palavras.length()-1)+"]";
				//salva as linhas contendo informações do Crossword
				outText.add(palavras+" "
						+this.c.getOutput()[i]+" -LINES: "
						+(anterior+1)+" to "
						+(this.c.getCrossTextIndex()[i]+1)
				);
				anterior = this.c.getCrossTextIndex()[i]+1;
			}
		}
		//se o usuário não quiser detalhes
		else{
			outText = new ArrayList<String>(Arrays.asList(this.c.getOutput()));
		}
		//se saída for arquivo
		if(this.f.hasFlag('o'))
			this.a.saveFile(nomeArqSa, outText);
		//se saída for System.out
		else
			for(int i = 0; i < outText.size(); i++)
				System.out.println(outText.get(i));
	}
	private void validateFlagUsage(int argLen){
		//erro: nenhum dado de entrada
		if(argLen == 0){
			this.showUsageMessage(3);
		}
		//mensagem de ajuda para desenvolvedores
		if(this.f.hasFlag('h')){
			this.showUsageMessage(1);
		}
		//erro: sem nome de arquivo de saída
		if(this.f.hasFlag('o') && argLen < 2){
			this.showUsageMessage(2);
		}
		//erro: nenhuma flag essencial {c|v|s|g} foi usada
		if(!(this.f.hasFlag('c') || this.f.hasFlag('v') || 
			 this.f.hasFlag('s') || this.f.hasFlag('g')))
		{
			this.showUsageMessage(4);
		}
		//aviso: {v|s|g} não interferem em -c
		if(this.f.hasFlag('c') && (this.f.hasFlag('v') || 
		   this.f.hasFlag('s') || this.f.hasFlag('g')))
		{
			System.out.println("Aviso: {v|s|g} não interferem em 'c'");
		}
	}
	private void showUsageMessage(int CODE){
		ClassLoader cl = this.getClass().getClassLoader();
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(cl.getResourceAsStream("ufpa/quickhelp")));
		String message = "";
		try{
			for(String line = br.readLine(); line != null; line = br.readLine()){
				message += line + "\n";
			}
		}catch(IOException E){E.printStackTrace(System.err);}
		System.err.println(message);
		message = "";
		switch(CODE){
			case 1:
				//ajuda para desenvolvedores
				br = new BufferedReader(new InputStreamReader(cl.getResourceAsStream("ufpa/devhelp")));
				try{
					for(String line = br.readLine(); line != null; line = br.readLine()){
						message += line + "\n";
					}
				}catch(IOException E){E.printStackTrace(System.err);}
				System.out.println(message);
				break;
			case 2:
				//erro: sem nome de arquivo de saída
				System.err.println("Erro: Faltando nome de arquivo de saída.");
				break;
			case 3:
				//erro: sem palavra ou nome de arquivo de entrada
				System.err.println("Erro: Faltando dado de entrada.");
				break;
			case 4:
				//erro: nenhuma flag essencial usada
				System.err.println("Nenhuma flag essencial usada.");
				break;
			default:
				//mensagem padrão (curta)
				break;
		}
		System.exit(1);
	}
}


