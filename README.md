# NLP: Gerador de Ferramentas para Processamento de Linguagem Natural
A ferramenta NLP disponibilizada pelo Grupo FalaBrasil conta atualmente com os
módulos de conversão grafema-fonema (G2P em ambas modalidades *internal-word* e
*cross-word*), separação silábica e identificador de vogal tônica.

# Java :coffee:
Compilação feita em versão `javac 1.8.0_162`.    
Instalação de dependências no Ubuntu/Debian:   
```bash
$ sudo apt-get install openjdk-8-jdk
$ sudo apt-get install openjdk-8-jre
```

O jar pode ser executado pela linha de comando (terminal) com:      
```bash
$ java -jar fb_nlplib.jar
```
Caso nenhuma instrução seja dada, uma mensagem de ajuda é exibida:

```text
Usage  1: java -jar falalib.jar -{v|s|g}          <PALAVRA>
Usage  2: java -jar falalib.jar -{v|s|g}          <PALAVRA> -o <SAIDA>
Usage  3: java -jar falalib.jar -{v|s|g}       -i <ENTRADA>
Usage  4: java -jar falalib.jar -{v|s|g}       -i <ENTRADA> -o <SAIDA>
Usage  5: java -jar falalib.jar -c{C}             <FRASE>
Usage  6: java -jar falalib.jar -c{C}             <FRASE>   -o <SAIDA>
Usage  7: java -jar falalib.jar -c{C}          -i <ENTRADA>
Usage  8: java -jar falalib.jar -c{C}          -i <ENTRADA> -o <SAIDA>
Usage  9: java -jar falalib.jar -{v|s|g}{c}{C} -i <ENTRADA> -t <NUMERO>
Usage 10: java -jar falalib.jar -{v|s|g}{c}{C} -i <ENTRADA> -o <SAIDA> -t <NUMERO>

  <PALAVRA>: palavra do Português Brasileiro em caixa baixa
  <SAIDA>:   nome para o arquivo destino contendo o resultado do processamento
  <ENTRADA>: nome do arquivo de entrada, deve conter uma palavra por linha
  <FRASE>:   frase entre aspas, por exemplo: "a casa era amarela"
  <NUMERO>:  número inteiro maior que 0

Lista de flags:
-i  --input    (caso entrada seja arquivo)
-o  --output   (caso saída seja arquivo)
-c  --cross    (caso crossword deva ser usado)
-C  --vcross   (caso a saída do crossword deva ser detalhada)
-a  --ascii    (caso a saída deva conter apenas caracteres ascii)
-v  --vowel    (para usar identificador de vogal tônica)
-s  --syllab   (para usar separador silábico)
-g  --g2p      (para usar o conversor grafema fonema)
-G  --g2p-s    (para retornar fonemas silabicamente separados)
-p  --progress (mostra uma barra de progresso)
-t  --threads  (função multithread, para uso com -i{g|s|v})
-e  --encoding (para usuário selecionar codificação do arquivo de entrada)
-h  --help     (para exibir ajuda para desenvolvedores)
```

A compilação do arquivo `FalaBrasilNLP.java` não é obrigatória para utilizar as
funcionalidades biblioteca de utilitários do FalaBrasil, porém pode ser
utilizada como uma API. A compilação do arquivo `FalaBrasilNLP.java` é dada por:     
```bash
$ javac -cp ".:fb_nlplib.jar" FalaBrasilNLP.java
```

Já a execução do arquivo `FalaBrasilNLP`:     
```bash
$ java  -cp ".:fb_nlplib.jar" FalaBrasilNLP
```

Caso a localização da lib seja alterada, para compilação do
`FalaBrasilNLP.java`, altere o _classpath_ de `".:fb_nlplib.jar"` para
`".:/nova/localização/do/fb_nlplib.jar"`.

# Python :dragon:
A ferramenta NLP, originalmente escrita em Java, foi recentemente utilizada em
Python graças ao módulo [PyJNIus](https://github.com/kivy/pyjnius), o qual
permite carregar métodos Java em Python.

## Instalação

### Conda

```bash
$ conda create --name fb-nlp-gen --file req_conda.txt
```

ou

```bash
$ conda create --name fb-nlp-gen python=3.7
$ conda activate fb-nlp-gen 
$ conda install cython
$ conda install -c conda-forge pyjnius
$ conda install -c anaconda openjdk
```

### Ubuntu / Debian (apt-get + pip)

Dependências:

```bash
$ sudo apt-get install python3 python3-pip
$ sudo -H pip3 install cython jnius pyjnius --upgrade
```

Execução feita em versão `python 3.5.3`.    

```bash
$ python3 FalaBrasilNLP.py /path/to/fb_nlplib.jar <PALAVRA>
```

Caso ocorram erros de execução, certifique-se de que sua variável de ambiente 
`JAVA_HOME` aponta para o Java 8 (ou exportada no `.bashrc` ou setada 
diretamente via `os.environ` no script Python), e que a sua versão do PyJNIus é 
a mais recente:

```bash
$ pip3 list | egrep -i 'jni|cython'
Cython               0.29.13  
jnius                1.1.0    
pyjnius              1.2.0    
```

[![FalaBrasil](doc/logo_fb_github_footer.png)](https://ufpafalabrasil.gitlab.io/ "Visite o site do Grupo FalaBrasil") [![UFPA](doc/logo_ufpa_github_footer.png)](https://portal.ufpa.br/ "Visite o site da UFPA")

__Grupo FalaBrasil (2019)__ - https://ufpafalabrasil.gitlab.io/      
__Universidade Federal do Pará (UFPA)__ - https://portal.ufpa.br/     
Daniel Santana - daniel.santana.1661@gmail.com    
Cassio Batista - https://cassota.gitlab.io/
