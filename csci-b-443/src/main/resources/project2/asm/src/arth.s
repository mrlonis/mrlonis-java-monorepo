value1:		.word 5
value2:		.word 25
value3:		.word 78
value4:		.word 77
value5:		.word 31
value6:		.word
	
lw $1, value1($0)		# load reg 1
lw $2, value2($0)		# load reg 2
lw $3, value3($0)		# load reg 3
lw $4, value4($0)		# load reg 4
lw $5, value5($0)		# load reg 5
or $6, $1, $2			# = 29
add $0, $0, $0			# nop
add $0, $0, $0			# nop
add $6, $6, $3			# = 107
add $0, $0, $0			# nop
add $0, $0, $0			# nop
and $6, $6, $4			# = 73
add $0, $0, $0			# nop
add $0, $0, $0			# nop
sub $6, $6, $5			# = 42 ;-)
add $0, $0, $0			# nop
add $0, $0, $0			# nop
sw $6, value6($0)		# store reg 6
	
