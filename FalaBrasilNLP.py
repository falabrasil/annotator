#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import os
os.environ['JAVA_HOME'] = sys.prefix if 'conda' in sys.prefix.lower() else '/usr/lib/jvm/java-8-openjdk'
os.environ['CLASSPATH'] = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'fb_nlplib.jar')

from jnius import autoclass

class FalaBrasilNLP:
    def __init__(self, ascii=False):
        self.jClass = autoclass('ufpa.util.PyUse')(ascii)

    def fb_getg2p(self, palavra):
        return self.jClass.useG2P(palavra)

    def fb_getsyl(self, palavra):
        return self.jClass.useSyll(palavra)

    def fb_get_stressindex(self, palavra):
        return self.jClass.useSVow(palavra)

    def fb_get_g2psyl(self, palavra):
        return self.jClass.useSG2P(palavra)

    def fb_get_crossword(self, frase):
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

def fb_print_demo(nlp, word):
    print('DEMO: "%s"' % word)
    if " " in word:
        print('  cross:    ', fb_nlp.fb_get_crossword(word))
        return
    print('  g2p:      ', fb_nlp.fb_getg2p(word))
    print('  syll:     ', fb_nlp.fb_getsyl(word))
    print('  stress:   ', fb_nlp.fb_get_stressindex(word))
    print('  syl pohns:', fb_nlp.fb_get_g2psyl(word))

if __name__ == '__main__':
    if len(sys.argv) != 2:
        fb_print_asciilogo()
        print('Usage: {} <PALAVRA>'.format(sys.argv[0]))
        exit(1)

    fb_nlp = FalaBrasilNLP()

    fb_print_demo(fb_nlp, sys.argv[1])
    print('')

    fb_nlp = FalaBrasilNLP(ascii=True)

    fb_print_demo(fb_nlp, sys.argv[1])
