# NLP: Natural Language Processing

## Conversor Grafema-Fonema (G2P)
Compilação feita em versão `javac 1.8.0_162`.    
Instalação de dependências no Ubuntu/Debian:   
```bash
sudo apt-get install openjdk-8-jdk
sudo apt-get install openjdk-8-jre
```

O jar pode ser executado pela linha de comando (terminal) com:      
```bash
java -jar falalib.jar
```
Caso nenhuma instrução seja dada, uma mensagem de ajuda é exibida:

```
Usage  1: java -jar falalib.jar <PALAVRA>                  -{v|s|g}
Usage  2: java -jar falalib.jar <PALAVRA> <SAIDA>          -o{v|s|g}
Usage  3: java -jar falalib.jar <ENTRADA>                  -i{v|s|g}
Usage  4: java -jar falalib.jar <ENTRADA> <SAIDA>          -io{v|s|g}
Usage  5: java -jar falalib.jar <FRASE>                    -c{C}
Usage  6: java -jar falalib.jar <FRASE>   <SAIDA>          -oc{C}
Usage  7: java -jar falalib.jar <ENTRADA>                  -ic{C}
Usage  8: java -jar falalib.jar <ENTRADA> <SAIDA>          -ioc{C}
Usage  9: java -jar falalib.jar <ENTRADA> <NUMERO>         -it{v|s|g}
Usage 10: java -jar falalib.jar <ENTRADA> <SAIDA> <NUMERO> -iot{v|s|g}

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
-G  --safeg2p  (para filtrar os caracteres não reconhecidos pelo g2p)
-h  --help     (para exibir ajuda para desenvolvedores)
-p  --progress (mostra uma barra de progresso)
-t  --threads  (função multithread, para uso com -i{g|s|v})
```

A compilação do arquivo `Runlib.java` não é obrigatória para utilizar as
funcionalidades biblioteca de utilitários do FalaBrasil, porém pode ser
utilizada como uma API. A compilação do arquivo `Runlib.java` é dada por:     
```bash
javac -cp ".:falalib.jar" Runlib.java
```

Já a execução do arquivo `Runlib`:     
```bash
java  -cp ".:falalib.jar" Runlib
```

Caso a localização da lib seja alterada, para compilação do `Runlib.java`, 
altere o _classpath_ de `".:falalib.jar"` para `
".:/nova/localização/do/falalib.jar"`.

__Grupo FalaBrasil (2019)__    
__Universidade Federal do Pará__    
Daniel Santana - daniel.santana.1661@gmail.com
