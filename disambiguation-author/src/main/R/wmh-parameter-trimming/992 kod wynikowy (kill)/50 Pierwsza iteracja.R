###################################################
################# DECISION PARAMETERS #############
###################################################
# -1 no changes
# 0 Null=0
# 2 Null=-abs(max(data$col))
nullConversion=0

# -1 no change
# 0 Bool
# 1 Bay-no-scal
# 2 Bay-scal
conversionMethod=0

# 0 lin
# 1 pol
kernel = 0

degree=1:10
gamma=1/(1:100)
coeff=seq(0,10,by=0.1)

