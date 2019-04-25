#!/bin/python3
#coding: utf-8
from py4j.java_gateway import JavaGateway
import sys
class FalaBrasilNLP:
	def __init__(self):
		self.gate = JavaGateway()
	def g2p(self, palavra):
		return self.gate.entry_point.useG2P(palavra)
	def syllabs(self, palavra):
		return self.gate.entry_point.useSyll(palavra)
	def strVow(self, palavra):
		return self.gate.entry_point.useSVow(palavra)
	def separaG2P(self, palavra):
		return self.gate.entry_point.useSG2P(palavra)
if __name__ == '__main__':
	if(len(sys.argv) <= 1):
		exit(1)
	print('Demo:')
	usejava = FalaBrasilNLP()
	print('{}\t{}\t{}\t{}'.format(
		usejava.g2p(sys.argv[1]), 
		usejava.syllabs(sys.argv[1]), 
		usejava.strVow(sys.argv[1]), 
		usejava.separaG2P(sys.argv[1])
	))
	exit(0)

