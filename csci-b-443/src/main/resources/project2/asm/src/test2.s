	# Forwarding/stalling test
	# type 1 data hazards

	addi $1, $0, 1          # $1 = 1
	addi $2, $0, 0          # $2 = 0
	addi $3, $0, 3          # $3 = 3
	add  $2, $2, $1	        # $2 = 0+1 = 1
	add  $2, $2, $1	        # $2 = 1+1 = 2
	add  $2, $2, $1	        # $2 = 2+1 = 3
	add  $3, $3, $1         # $3 = 3+1 = 4
