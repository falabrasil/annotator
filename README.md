# NLP
### Conversor Grafema-Fonema (G2P)
Compilação feita em versão `javac 1.8.0_162`.    
Instalação de dependências no Ubuntu/Debian:   
```
sudo apt-get install openjdk-8-jdk
sudo apt-get install openjdk-8-jre
```

O jar pode ser executado com:
```
java -jar falalib.jar
```
Caso nenhuma instrução seja dada, uma mensagem de ajuda é exibida.

```
Usage 1: java -jar falalib.jar <PALAVRA> -{v|s|g}
Usage 2: java -jar falalib.jar <PALAVRA> <SAIDA> -o{v|s|g}
Usage 3: java -jar falalib.jar <ENTRADA> -i{v|s|g}
Usage 4: java -jar falalib.jar <ENTRADA> <SAIDA> -io{v|s|g}
Usage 5: java -jar falalib.jar <FRASE> -c{C}
Usage 6: java -jar falalib.jar <FRASE> <SAIDA> -oc{C}
Usage 7: java -jar falalib.jar <ENTRADA> -ic{C}
Usage 8: java -jar falalib.jar <ENTRADA> <SAIDA> -ioc{C}

	<PALAVRA> é: palavra do Português Brasileiro em caixa baixa
	<SAIDA> é: nome para o arquivo destino contendo o resultado do processamento
	<ENTRADA> é: nome do arquivo de entrada, deve conter uma palavra por linha
	<FRASE> é: frase entre aspas, por exemplo: "a casa era amarela"

Lista de flags:
-i	--input (caso entrada seja arquivo)
-o	--output (caso saída seja arquivo)
-c	--cross (caso crossword deva ser usado)
-C	(caso a saída do crossword deva ser detalhada)
-a	--ascii (caso a saída deva conter apenas caracteres ascii)
-v	--vowel (para usar identificador de vogal tônica)
-s	--syllab (para usar separador silábico)
-g	--g2p (para usar o conversor grafema fonema)
-h	--help (para exibir ajuda para desenvolvedores)
```

A compilação do arquivo `Runlib.java` não é obrigatória para utilizar as
funcionalidades biblioteca de utilitários do FalaBrasil

Compilação do arquivo `Runlib.java`, presente no diretório `fala-libs/`:   
```
javac -cp ".:falalib.jar" Runlib.java
```

Execução do arquivo `Runlib`, presente no diretório `fala-libs/`:   
```
java  -cp ".:falalib.jar" Runlib
```

Caso a localização da lib seja alterada, para compilação do `Runlib.java`, 
altere o _classpath_ de `".:falalib.jar"` para `
".:/nova/localização/do/falalib.jar"`.


