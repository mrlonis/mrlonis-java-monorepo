"""Flip endianness of a file.

usage: flip.py [-h] input_file
"""

import argparse as ap

parser = ap.ArgumentParser(description="Flip endianness of a 32 bit binary file in place.")
parser.add_argument('input_file', type=ap.FileType('rb+'),
                    help='Input file')
args = parser.parse_args()

input = args.input_file.read()
output = []
word = []
for b in input:
    word.append(b)
    if len(word) == 4:
        output.append(word[3])
        output.append(word[2])
        output.append(word[1])
        output.append(word[0])
        word = []

args.input_file.seek(0)
args.input_file.write(bytes(bytearray(output)))
