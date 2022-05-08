# Annotator: Gerador de *tags* em texto

Módulos de conversão grafema-fonema (G2P em ambas modalidades *internal-word* e
*cross-word*), separação silábica e identificador de vogal tônica.


## Java :coffee:

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


## Python :snake:

:warning: Essa _wrapper_ é muito instável e as dependências não foram muito 
bem estabelecidas. Use sob sua própria conta e risco :smile:

A ferramenta NLP, originalmente escrita em Java, foi recentemente utilizada em
Python graças ao módulo [PyJNIus](https://github.com/kivy/pyjnius), o qual
permite carregar métodos Java em Python.

### Instalação

#### Conda

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

#### Ubuntu / Debian (apt-get + pip)

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


## Módulos em Docker :whale:

https://hub.docker.com/u/falabrasil

As imagens encontram-se disponíveis para download no docker
hub e podem ser baixadas com os seguintes comandos:

```bash
$ docker pull falabrasil/g2p
$ docker pull falabrasil/syl
$ docker pull falabrasil/stress
```

Em contrapartida, é também perfeitamente possível gerar imagens baseadas no
Alpine Linux a partir desse mesmo repositório utilizando os seguintes comandos:

```bash
$ docker build -t falabrasil/g2p:$(date +%Y%m%d) -f docker/g2p/Dockerfile .
$ docker build -t falabrasil/syl:$(date +%Y%m%d) -f docker/syl/Dockerfile .
$ docker build -t falabrasil/stress:$(date +%Y%m%d) -f docker/stress/Dockerfile .
```

A seguir, o uso é demonstrado em um arquivo recém escrito chamado `wlist.txt`,
o qual deve ser passado como argumento ao `stdin` do container:

```bash
$ cat << EOF > /tmp/wlist.txt
a
galinha
come
bolinha
EOF
```

Conversão grafema-fonema:

```text
$ cat /tmp/wlist.txt | docker run --rm -i falabrasil/g2p
a       a
galinha g a l i~ J  a
come    k o~ m i
bolinha b O l i~ J a
```

Separação em sílabas:

```text
$ cat /tmp/wlist.txt | docker run --rm -i falabrasil/syl
a       a
galinha ga-li-nha
come    co-me
bolinha bo-li-nha
```

Identificação de vogal tônica:

```text
$ cat /tmp/wlist.txt | docker run --rm -i falabrasil/stress
a       1
galinha 4
come    2
bolinha 4
```

:warning: Não esqueça que, para os comandos com `docker run` funcionarem, as 
imagens construídas com `docker build` devem ser *tagged* com a tag `latest`:

```bash
$ docker tag falabrasil/<MOD>:20211023 falabrasil/<MOD>:latest
```


## Citação

### Conversor G2P e identificador de vogal tônica

```bibtex
@article{Siravenha08,
    author   = {Ana Siravenha and Nelson Neto and Valquíria Macedo and Aldebaro Klautau},
    title    = {Uso de Regras Fonol\'{o}gicas com Determina\c{c}\~{a}o de Vogal T\^{o}nica para Convers\~{a}o Grafema-Fone em {P}ortugu\^{e}s {B}rasileiro},
    journal  = {7th International Information and Telecommunication Technologies Symposium},
    year     = {2008}
}
```

### Separador silábico

```bibtex
@article{Neto2015,
    author   = {Neto, Nelson and Rocha, Willian and Sousa, Gleidson},
    title    = {An open-source rule-based syllabification tool for Brazilian Portuguese},
    journal  = {Journal of the Brazilian Computer Society},
    year     = {2015},
    month    = {Jan},
    day      = {28},
    volume   = {21},
    number   = {1},
    pages    = {1},
    issn     = {1678-4804},
    doi      = {10.1186/s13173-014-0021-9},
    url      = {https://doi.org/10.1186/s13173-014-0021-9}
}
```

### Recursos livres para Português Brasileiro

```bibtex
@article{Neto10,
    author   = {Neto, Nelson and Patrick, Carlos and Klautau, Aldebaro and Trancoso, Isabel},
    title    = {Free tools and resources for Brazilian Portuguese speech recognition},
    journal  = {Journal of the Brazilian Computer Society},
    year     = {2011},
    month    = {Mar},
    day      = {01},
    volume   = {17},
    number   = {1},
    pages    = {53--68},
    issn     = {1678-4804},
    doi      = {10.1007/s13173-010-0023-1},
    url      = {https://doi.org/10.1007/s13173-010-0023-1}
}
```


[![FalaBrasil](https://gitlab.com/falabrasil/avatars/-/raw/main/logo_fb_git_footer.png)](https://ufpafalabrasil.gitlab.io/ "Visite o site do Grupo FalaBrasil") [![UFPA](https://gitlab.com/falabrasil/avatars/-/raw/main/logo_ufpa_git_footer.png)](https://portal.ufpa.br/ "Visite o site da UFPA")

__Grupo FalaBrasil (2021)__ - https://ufpafalabrasil.gitlab.io/      
__Universidade Federal do Pará (UFPA)__ - https://portal.ufpa.br/     
Daniel Santana - daniel.santana.1661@gmail.com    
Cassio Batista - https://cassota.gitlab.io/
