import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import ufpa.falabrasil.GraphemeToPhoneme;
import ufpa.falabrasil.Syllabificator;
import ufpa.falabrasil.StressVowel;
import ufpa.falabrasil.Cross;
import ufpa.falabrasil.TextTool;
import ufpa.util.*;
public class FalaBrasilNLP{
	private final char[] pflags = {
		'G','t','p','i','o',
		'c','a','v','s','h','g',
		'C','e'};
	private final String[] eflags = {
		"g2p-s","threads","progress","input","output",
		"cross","ascii","vowel","syllab","help","g2p",
		"vcross","encoding"};
	private final boolean[] aflags = {
		false, true, false, true , true,
		false, false, false, false, false, false,
		false, true};
	private String[] params;
	private GraphemeToPhoneme g = new GraphemeToPhoneme();
	private Syllabificator    s = new Syllabificator();
	private StressVowel       v = new StressVowel();
	private TextTool          t = new TextTool();
	private Cross             c = new Cross();
	private Flags             f = new Flags();
	private Filehandler       a = new Filehandler();
	private int        pBarSize = 50;
	private int         threadC;
	private FalaBrasilNLP(String[] args){
		//cria as flags
		for(int i = 0; i < this.pflags.length; i++)
			this.f.addFlag(this.pflags[i], this.eflags[i], this.aflags[i]);
		//configura e valida as flags usadas pelo usuário
		try {
			this.f.parseArgs(args);
			this.params = this.f.getOnlyArgs();
		} catch(InvalidFlagException E) {
			System.err.printf("Uso incorreto de parâmetros de execução:\n\n");
			E.printStackTrace(System.err);
			System.exit(-1);
		}
		this.validateFlagUsage(this.params.length);
		//configura os parâmetros do crossword
		this.c.setASCII(this.f.hasFlag('a'));
		this.c.setVerbosis(this.f.hasFlag('p'));
		if(this.f.hasFlag('t')) {
			try {
				this.threadC = Math.abs(Integer.parseInt(this.f.getFlagArg('t')));
			} catch (NumberFormatException E) {
				System.err.println("Quantidade de Threads deve ser número");
			}
			this.c.setThreads(this.threadC);
		}
	}


	public static void main(String[] args){
		FalaBrasilNLP run = new FalaBrasilNLP(args);
		ArrayList<String> output;
		//se estiver usando crossword
		if(run.f.hasFlag('c')){
			//se entrada for arquivo
			if(run.f.hasFlag('i')) output = run.forCrosswrdFile(run.f.getFlagArg('i'));
			//se entrada for frase
			else output = run.forCrosswrdText(run.params[0]);
		}
		//se não estiver usando crossword
		else{
			//executa com threads
			if(run.f.hasFlag('t') && run.f.hasFlag('i'))
				output = run.forMult(run.f.getFlagArg('i'));
			//se entrada for arquivo
			else if(run.f.hasFlag('i')) output = run.forFile(run.f.getFlagArg('i'));
			//se entrada for palavra
			else output = run.forWord(run.params[0]);
		}
		//se saída for para arquivo
		if(run.f.hasFlag('o'))
			try {
				run.a.saveFile(run.f.getFlagArg('o'), output);
			} catch(IOException E) { E.printStackTrace(System.err); }
		//se saída for para System.out
		else for(int i = 0; i < output.size(); i++)
				System.out.println(output.get(i));
		System.exit(0);
	}


	//configuração
	//caso entrada seja palavra
	private ArrayList<String> forWord(String palavra){
		ArrayList<String> inText = new ArrayList<String>();
		inText.add(palavra);
		return this.useClasses(inText);
	}
	private ArrayList<String> getFileContent(String loadNome) {
		ArrayList<String> inText = new ArrayList<>();
		int opt = 0;
		if(this.f.hasFlag('e')) {
			opt = Integer.parseInt(this.f.getFlagArg('e'));
			if (opt < 0 || opt > 5) {
				opt = 0;
				System.out.println("Opção de encoder inválida, padrão selecionado: 0");
			}
		}
		try {
			inText = this.a.loadFile(loadNome, opt);
		} catch(IOException E) {
			System.err.println("Erro ao carregar arquivo.");
			E.printStackTrace(System.err);
			System.exit(1);
		}
		return inText;
	}
	//caso entrada seja arquivo
	private ArrayList<String> forFile(String loadNome){
		ArrayList<String> inText;
		inText = this.getFileContent(loadNome);
		return this.useClasses(inText);
	}
	//configura crossword para texto
	private ArrayList<String> forCrosswrdText(String frase){
		this.c.setInputAsArray(frase.split(" "));
		return this.forCrosswrd();
	}
	//configura crossword para arquivo
	private ArrayList<String> forCrosswrdFile(String loadNome){
		ArrayList<String> inText;
		inText = this.getFileContent(loadNome);
		this.c.setInputAsArray(inText.toArray(new String[inText.size()]));
		return this.forCrosswrd();
	}


	//execução
	//executa com multithread
	private ArrayList<String> forMult(String loadNome){
		ArrayList<String> inText;
		inText = this.getFileContent(loadNome);
		ConcurrentGSS THD = new ConcurrentGSS(
			inText,
			this.f,
			this.threadC,
			this.pBarSize
		);
		Thread concg = new Thread(THD);
		concg.start();
		while(!THD.isFinished()){
			try{
				Thread.sleep(300);
			} catch (InterruptedException E){
				E.printStackTrace(System.err);
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
				//g2p com saída ascii
				saida += this.g.g2pLA(palavra)+"\t";
			}
			else if(this.f.hasFlag('g')){
				//somente g2p
				saida += this.g.g2p(palavra)+"\t";
			}
			if(this.f.hasFlag('s'))
				//separador silábico
				saida += this.s.syllabs(palavra)+"\t";
			if(this.f.hasFlag('v'))
				//vogal tônica
				saida += this.v.findStress(palavra)+"\t";
			if(this.f.hasFlag('G'))
				//g2p separado
				saida += this.t.separaG2P(palavra);
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
		if(argLen == 0 && !this.f.hasFlag('i')){
			this.showUsageMessage(2);
		}
		//erro: nenhuma flag essencial {c|v|s|g} foi usada
		if(!(this.f.hasFlag('c') || this.f.hasFlag('v') || 
			 this.f.hasFlag('s') || this.f.hasFlag('g') ||
			 this.f.hasFlag('G')
		))
		{
			this.showUsageMessage(3);
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
				//erro: sem palavra ou nome de arquivo de entrada
				System.err.println("Erro: Faltando dado de entrada.");
				break;
			case 3:
				//erro: nenhuma flag essencial usada
				System.err.println("Erro: Nenhuma flag essencial usada.");
				break;
			default:
				//mensagem padrão (curta)
				break;
		}
		System.exit(1);
	}
}


