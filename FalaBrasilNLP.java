import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import ufpa.falabrasil.GraphemeToPhoneme;
import ufpa.falabrasil.Syllabificator;
import ufpa.falabrasil.StressVowel;
import ufpa.falabrasil.Cross;
import ufpa.util.*;

public class FalaBrasilNLP{
	private final char[] pflags = {'t','p','G','i','o','c','a','v','s','h','g',
									'C'};
	private final String[] eflags = {
		"threads","progress","safeg2p","input","output","cross","ascii","vowel",
									"syllab","help","g2p","vcross"};
	private String[] params;
	private ArrayList<String> output;
	private GraphemeToPhoneme g = new GraphemeToPhoneme();
	private Syllabificator    s = new Syllabificator();
	private StressVowel       v = new StressVowel();
	private Cross             c = new Cross();
	private Flags             f = new Flags(pflags);
	private Filehandler       a = new Filehandler();
	private int        pBarSize = 50;
	private Runlib(String[] args){
		//expande as flags
		for(int i = 0; i < this.eflags.length; i++)
			this.f.expandFlag(this.pflags[i], this.eflags[i]);
		//configura e valida as flags usadas pelo usuário
		this.params = this.f.setupFlags(args);
		this.validateFlagUsage(this.params.length);
		//configura os parâmetros do crossword
		this.c.setG2PFilter(this.f.hasFlag('G'));
		this.c.setASCII(this.f.hasFlag('a'));
		this.c.setPBar(this.f.hasFlag('p'),pBarSize);
	}


	public static void main(String[] args){
		Runlib run = new Runlib(args);
		//se estiver usando crossword
		if(run.f.hasFlag('c')){
			//se entrada for arquivo
			if(run.f.hasFlag('i')) run.output = run.forCrosswrdFile(run.params[0]);
			//se entrada for frase
			else run.output = run.forCrosswrdText(run.params[0]);
		}
		//se não estiver usando crossword
		else{
			//executa com threads
			if(run.f.hasFlag('t') && run.f.hasFlag('i')){
				run.output = run.forMult(run.params[0]);
			}
			//se entrada for arquivo
			else if(run.f.hasFlag('i')) run.output = run.forFile(run.params[0]);
			//se entrada for palavra
			else run.output = run.forWord(run.params[0]);
		}
		//se saída for para arquivo
		if(run.f.hasFlag('o')) run.a.saveFile(run.params[1], run.output);
		//se saída for para System.out
		else for(int i = 0; i < run.output.size(); i++)
				System.out.println(run.output.get(i));
		System.exit(0);
	}


	//configuração
	//caso entrada seja palavra
	private ArrayList<String> forWord(String palavra){
		ArrayList<String> inText = new ArrayList<String>();
		inText.add(palavra);
		return this.useClasses(inText);
	}
	//caso entrada seja arquivo
	private ArrayList<String> forFile(String loadNome){
		ArrayList<String> inText = this.a.loadFile(loadNome);
		return this.useClasses(inText);
	}
	//configura crossword para texto
	private ArrayList<String> forCrosswrdText(String frase){
		this.c.setInputAsArray(frase.split(" "));
		return this.forCrosswrd();
	}
	//configura crossword para arquivo
	private ArrayList<String> forCrosswrdFile(String loadNome){
		ArrayList<String> inText = this.a.loadFile(loadNome);
		this.c.setInputAsArray(inText.toArray(new String[inText.size()]));
		return this.forCrosswrd();
	}


	//execução
	//executa com multithread
	private ArrayList<String> forMult(String loadNome){
		ConcurrentGSS THD = new ConcurrentGSS(
		this.a.loadFile(loadNome),
		this.f,
		new Lastint(this.params).getInt(),
		this.pBarSize
		);
		Thread concg = new Thread(THD);
		concg.start();
		while(!THD.isFinished()){
			try{
				Thread.sleep(300);
			} catch (InterruptedException E){
				E.printStackTrace(System.out);
			}
		}
		return THD.getOutput();
	}
	//uso das classes G2P, Syllabificator e StressVowel
	private ArrayList<String> useClasses(ArrayList<String> inText){
		ArrayList<String> outText = new ArrayList<String>();
		for(int i = 0; i < inText.size(); i++){
			String palavra = inText.get(i);
			String saida = "";
			//se saída do g2p deve conter apenas caracteres ASCII
			if(this.f.hasFlag('g') && this.f.hasFlag('a')){
				if(this.f.hasFlag('G'))
					//g2p com filtro e saída ascii
					saida += this.g.g2pSafeLA(palavra)+"\t";
				else
					//g2p com saída ascii
					saida += this.g.g2pLA(palavra)+"\t";
			}
			else if(this.f.hasFlag('g')){
				if(this.f.hasFlag('G'))
					//g2p com filtro
					saida += this.g.g2pSafe(palavra)+"\t";
				else
					//somente g2p
					saida += this.g.g2p(palavra)+"\t";
			}
			if(this.f.hasFlag('s'))
				//separador silábico
				saida += this.s.syllabs(palavra)+"\t";
			if(this.f.hasFlag('v'))
				//vogal tônica
				saida += this.v.findStress(palavra);
			outText.add(palavra + "\t" + saida.trim());
			if(this.f.hasFlag('p')){
				System.out.print(
						new Progress().getBar(this.pBarSize,i,inText.size()));
			}
		}
		if(this.f.hasFlag('p')) System.out.println();
		return outText;
	}
	//uso do crossword
	private ArrayList<String> forCrosswrd(){
		ArrayList<String> outText = new ArrayList<String>();
		//se o usuário quiser detalhes
		if(this.f.hasFlag('C')){
			outText = this.detailedCross(outText);
		}
		//se o usuário quiser só fonemas
		else{
			outText = new ArrayList<String>(Arrays.asList(this.c.getOutput()));
		}
		return outText;
	}
	//detalha output do cross
	private ArrayList<String> detailedCross(ArrayList<String> outText){
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
		return outText;
	}

	//erro e ajuda
	//valida o uso das flags
	private void validateFlagUsage(int argLen){
		//mensagem de ajuda para desenvolvedores
		if(this.f.hasFlag('h')){
			this.showUsageMessage(1);
		}
		//erro: nenhum dado de entrada
		if(argLen == 0){
			this.showUsageMessage(3);
		}
		//mensagem de ajuda para desenvolvedores
		if(this.f.hasFlag('h')){
			this.showUsageMessage(1);
		}
		//erro: args insuficientes para multithread e saída
		if(this.f.hasFlag('t') && this.f.hasFlag('o') && argLen < 3){
			this.showUsageMessage(5);
		}
		//erro: args insuficientes para multithread
		else if(this.f.hasFlag('t') && argLen < 2){
			this.showUsageMessage(5);
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
	//mensagem de ajuda de acordo com erro
	private void showUsageMessage(int CODE){
		ClassLoader cl = this.getClass().getClassLoader();
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(
				"ufpa/util/quickhelp")));
		String message = "";
		try{
			for(String line = br.readLine(); 
					line != null; 
					line = br.readLine())
			{
				message += line + "\n";
			}
		}catch(IOException E){E.printStackTrace(System.err);}
		System.err.println(message);
		message = "";
		switch(CODE){
			case 1:
				//ajuda para desenvolvedores
				br = new BufferedReader(new InputStreamReader(
						cl.getResourceAsStream("ufpa/util/devhelp")));
				try{
					for(String line = br.readLine(); 
							line != null; 
							line = br.readLine())
					{
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
				System.err.println("Erro: Nenhuma flag essencial usada.");
				break;
			case 5:
				//erro: args insuficientes no uso de multithread
				System.err.println(
				"Erro: Faltando entrada, saída, ou quantidade de threads."
				);
				break;
			case 6:
				//erro: ultimo arg não foi quantidade de threads
				System.err.println(
				"Quando usando threads, último argumento deve ser número "+
				"inteiro maior que 0."
				);
			default:
				//mensagem padrão (curta)
				break;
		}
		System.exit(1);
	}
}


