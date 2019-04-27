#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import sys

if len(sys.argv) == 3:
	os.environ['CLASSPATH'] = sys.argv[1]

from jnius import autoclass

class FalaBrasilNLP:
	def __init__(self):
		self.jClass = autoclass('ufpa.util.PyUse')()

	def g2p(self, palavra):
		return self.jClass.useG2P(palavra)

	def syllabs(self, palavra):
		return self.jClass.useSyll(palavra)

	def findStress(self, palavra):
		return self.jClass.useSVow(palavra)

	def separaG2P(self, palavra):
		return self.jClass.useSG2P(palavra)

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

if __name__ == '__main__':
	if len(sys.argv) != 3:
		fb_print_asciilogo()
		print('Usage: {} /path/to/fb_nlplib.jar <PALAVRA>'.format(sys.argv[0]))
		exit(1)

	fb_nlp = FalaBrasilNLP()

	print('DEMO: "%s"' % sys.argv[2])
	print('  g2p:      ', fb_nlp.g2p(sys.argv[2]))
	print('  syll:     ', fb_nlp.syllabs(sys.argv[2]))
	print('  stress:   ', fb_nlp.findStress(sys.argv[2]))
	print('  syl pohns:', fb_nlp.separaG2P(sys.argv[2]))
