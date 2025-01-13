# Test looping
# s=0
# for i=3..1
#   for j=6..1
#     if (j and 3) < 2
#       for k=2..1
#         s = s+1
# store s

i:		.word 54
j:		.word 74
k:		.word 31
s:		.word
c3:		.word 3
c2:		.word 2
c1:		.word 1

# constanten laden
lw $4, c3($0)			# $4 = c3
lw $5, c2($0)			# $4 = c2
lw $6, c1($0)			# $4 = c1

add $8, $0, $0			# s = 0   (reg 8)
lw $1, i($0)			# load i  (reg 1)

fori:	lw $2, j($0)		# load j  (reg 2)
forj:	and $7, $2, $4		# $7 = j & c3
slt $7, $7, $5			# $7 = (j & 3) < 2 ? 1 : 0
beq $7, $0, endif

lw $3, k($0)			# load k (reg 3)
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

endi:	sw $8, s($0)		# store s
