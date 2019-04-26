#!/bin/python3
#coding: utf-8
import os
import sys
if len(sys.argv) == 3:
	os.environ['CLASSPATH'] = sys.argv[1]
from jnius import autoclass as cl
class FalaPyNLP:
	def __init__(self):
		self.jClass = cl('ufpa.util.PyUse')()
	def g2p(self, palavra):
		return self.jClass.useG2P(palavra)
	def syllabs(self, palavra):
		return self.jClass.useSyll(palavra)
	def findStress(self, palavra):
		return self.jClass.useSVow(palavra)
	def separaG2P(self, palavra):
		return self.jClass.useSG2P(palavra)
if __name__ == '__main__':
	if len(sys.argv) != 3:
		print('Usage: {} /path/to/fb_nlplib.jar <PALAVRA>'.format(sys.argv[0]))
		exit(1)
	nlp = FalaPyNLP()
	print('DEMO: {}\t{}\t{}\t{}'.format(
		nlp.g2p(sys.argv[2]),
		nlp.syllabs(sys.argv[2]),
		nlp.findStress(sys.argv[2]),
		nlp.separaG2P(sys.argv[2])
	))
	exit(0)

