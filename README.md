# NLP
### Conversor Grafema-Fonema (G2P)
Compilação feita em versão `javac 1.8.0_162`.    
Instalação de dependências no Ubuntu/Debian:   
```
sudo apt-get install openjdk-8-jdk
sudo apt-get install openjdk-8-jre
```

O Jar pode ser executado com:
```
java -jar falalib.jar
```
Caso nenhuma instrução seja dada, uma mensagem de ajuda é exibida.

Compilação do arquivo de teste, presente no diretório `fala-libs/`:   
```
javac -cp ".:falalib.jar" TestG2P.java
```

Execução do arquivo de teste, presente no diretório `fala-libs/`:   
```
java  -cp ".:falalib.jar" TestG2P [<PALAVRA> | <ENTRADA.list> <BOOLEAN>]
```

`<PALAVRA>` é qualquer palavra da língua Português brasileiro com apenas
caracteres caixa baixa.     
`<ENTRADA.list>` é um arquivo com um palavra por linha e     
`<BOOLEAN>` é um valor booleano "verdadeiro" ou "falso".

Caso a localização da lib seja alterada, basta apenas alterar o _classpath_ de
`".:falalib.jar"` por `".:/absolute/path/to/lib.jar"`.
