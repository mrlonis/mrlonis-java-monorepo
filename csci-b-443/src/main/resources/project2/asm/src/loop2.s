# Nested loops (could be used to test branch prediction)
# s=0
# for i=5..1
#   for j=8..1
#     if (j and 3) < 2
#       for k=4..1
#         s = s+1
# store s (=80)

i:		.word 5
j:		.word 8
k:		.word 4
s:		.word
c3:		.word 3
c2:		.word 2
c1:		.word 1

# load constants
lw $4, c3($0)			# $4 = c3
lw $5, c2($0)			# $5 = c2
lw $6, c1($0)			# $6 = c1

add $8, $0, $0			# $8 = 0   (Reg8)
lw $1, i($0)			# $1 = i

fori:	lw $2, j($0)		# $2 = j
forj:	and $7, $2, $4		# $7 = j & c3
slt $7, $7, $5			# $7 = (j & 3) < 2 ? 1 : 0
beq $7, $0, endif

lw $3, k($0)			# $3 = k
fork:	add $8, $8, $6		# s = s+1
sub $3, $3, $6			# k = k-1
beq $3, $0, endif		# k == 0 => goto endif
j fork				# next iteration

endif:	sub $2, $2, $6		# j = j-1
beq $2, $0, endj		# j == 0 => goto endj
j forj				# next iteration

endj:	sub $1, $1, $6		# i = i-1
beq $1, $0, endi		# j == 0 => goto endi
j fori				# next iteration

endi:	sw $8, s($0)		# store s --- *s = $8
