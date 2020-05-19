#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys

class FalaBrasilNLP:
    def __init__(self, ascii=False):
        import os
        os.environ['JAVA_HOME'] = sys.prefix if 'conda' in sys.prefix.lower() else '/usr/lib/jvm/java-8-openjdk'
        os.environ['CLASSPATH'] = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'fb_nlplib.jar')
        from jnius import autoclass
        self.jClass = autoclass('ufpa.util.PyUse')(ascii)

    def get_g2p(self, palavra):
        return self.jClass.useG2P(palavra)

    def get_syll(self, palavra):
        return self.jClass.useSyll(palavra)

    def get_stressindex(self, palavra):
        return self.jClass.useSVow(palavra)

    def get_g2psyll(self, palavra):
        return self.jClass.useSG2P(palavra)

    def get_crossword(self, frase):
        return self.jClass.useCross(frase)

def fb_print_asciilogo():
    print('\033[94m  ____                         \033[93m _____     _           \033[0m')
    print('\033[94m / ___| _ __ _   _ _ __   ___  \033[93m|  ___|_ _| | __ _     \033[0m')
    print('\033[94m| |  _ | \'__| | | | \'_ \ / _ \ \033[93m| |_ / _` | |/ _` |  \033[0m')
    print('\033[94m| |_| \| |  | |_| | |_) | (_) |\033[93m|  _| (_| | | (_| |    \033[0m')
    print('\033[94m \____||_|   \__,_| .__/ \___/ \033[93m|_|  \__,_|_|\__,_|    \033[0m')
    print('                  \033[94m|_|      \033[32m ____                _ _\033[91m  _   _ _____ ____    _            \033[0m')
    print('                           \033[32m| __ ) _ __ __ _ ___(_) |\033[91m| | | |  ___|  _ \  / \          \033[0m')
    print('                           \033[32m|  _ \| \'_ / _` / __| | |\033[91m| | | | |_  | |_) |/ ∆ \          \033[0m')
    print('                           \033[32m| |_) | | | (_| \__ \ | |\033[91m| |_| |  _| |  __// ___ \        \033[0m')
    print('                           \033[32m|____/|_|  \__,_|___/_|_|\033[91m \___/|_|   |_|  /_/   \_\       \033[0m')
    print('')

def print_demo(nlp, word):
    print('DEMO: "%s"' % word)
    if " " in word:
        print('  cross:    ', fb_nlp.get_crossword(word))
        return
    print('  g2p:      ', fb_nlp.get_g2p(word))
    print('  syll:     ', fb_nlp.get_syll(word))
    print('  stress:   ', fb_nlp.get_stressindex(word))
    print('  syl phons:', fb_nlp.get_g2psyll(word))
    print()

if __name__ == '__main__':
    if len(sys.argv) != 2:
        fb_print_asciilogo()
        print('Usage: {} <PALAVRA>'.format(sys.argv[0]))
        sys.exit(1)

    fb_nlp = FalaBrasilNLP()
    print_demo(fb_nlp, sys.argv[1])

    fb_nlp = FalaBrasilNLP(ascii=True)
    print_demo(fb_nlp, sys.argv[1])
