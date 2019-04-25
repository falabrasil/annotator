# NLP: Gerador de Ferramentas para Processamento de Linguagem Natural

A ferramenta NLP disponibilizada pelo Grupo FalaBrasil conta atualmente com os
módulos de conversão grafema-fonema (G2P em ambas modalidades *internal-word* e
*cross-word*), separação silábica e identificador de vogal tônica.

Compilação feita em versão `javac 1.8.0_162`.    
Instalação de dependências no Ubuntu/Debian:   
```bash
sudo apt-get install openjdk-8-jdk
sudo apt-get install openjdk-8-jre

sudo -H pip3 install py4j --upgrade
```

O jar pode ser executado pela linha de comando (terminal) com:      
```bash
java -jar fb_nlplib.jar
```
Caso nenhuma instrução seja dada, uma mensagem de ajuda é exibida:

```
Usage  1: java -jar fb_nlplib.jar <PALAVRA>                  -{v|s|g|G}
Usage  2: java -jar fb_nlplib.jar <PALAVRA> <SAIDA>          -o{v|s|g|G}
Usage  3: java -jar fb_nlplib.jar <ENTRADA>                  -i{v|s|g|G}
Usage  4: java -jar fb_nlplib.jar <ENTRADA> <SAIDA>          -io{v|s|g|G}
Usage  5: java -jar fb_nlplib.jar <FRASE>                    -c{C}
Usage  6: java -jar fb_nlplib.jar <FRASE>   <SAIDA>          -oc{C}
Usage  7: java -jar fb_nlplib.jar <ENTRADA>                  -ic{C}
Usage  8: java -jar fb_nlplib.jar <ENTRADA> <SAIDA>          -ioc{C}
Usage  9: java -jar fb_nlplib.jar <ENTRADA> <NUMERO>         -it{v|s|g|G}{c}{C}
Usage 10: java -jar fb_nlplib.jar <ENTRADA> <SAIDA> <NUMERO> -iot{v|s|g|G}{c}{C}
Usage 11: java -jar fb_nlplib.jar -P

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
-P  --python   (inicia o GatewayServer do py4j)
-p  --progress (mostra uma barra de progresso)
-t  --threads  (função multithread, para uso com -i{g|s|v})
-e  --encoding (para usuário selecionar codificação do arquivo de entrada)
-h  --help     (para exibir ajuda para desenvolvedores)
```

A compilação do arquivo `FalaBrasilNLP.java` não é obrigatória para utilizar as
funcionalidades biblioteca de utilitários do FalaBrasil, porém pode ser
utilizada como uma API. A compilação do arquivo `FalaBrasilNLP.java` é dada por:     
```bash
javac -cp ".:fb_nlplib.jar" FalaBrasilNLP.java
```

Já a execução do arquivo `FalaBrasilNLP`:     
```bash
java  -cp ".:fb_nlplib.jar" FalaBrasilNLP
```

Caso a localização da lib seja alterada, para compilação do `FalaBrasilNLP.java`, 
altere o _classpath_ de `".:fb_nlplib.jar"` para `
".:/nova/localização/do/fb_nlplib.jar"`.

__Grupo FalaBrasil (2019)__    
__Universidade Federal do Pará__    
Daniel Santana - daniel.santana.1661@gmail.com
