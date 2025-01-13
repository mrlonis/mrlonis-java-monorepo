value1:		.word 5
value2:		.word 25
value3:		.word 78
value4:		.word 77
value5:		.word 31
value6:		.word
	
lw $1, value1($0)		; load reg 1
lw $2, value2($0)		; load reg 2
lw $3, value3($0)		; load reg 3
lw $4, value4($0)		; load reg 4
lw $5, value5($0)		; load reg 5
or $6, $1, $2			; = 29
add $6, $6, $3			; = 107
slt $7, $3, $2			; $7 = ($3 < $2) ? 1 : 0 = 0
and $6, $6, $4			; = 73
sub $6, $6, $5			; = 42 ;-)
sw $6, value6($0)		; store $6
	
