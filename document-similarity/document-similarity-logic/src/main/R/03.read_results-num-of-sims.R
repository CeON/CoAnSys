vector_len <- read.csv("~/vector_len.csv", header=F)

vector_len$V2 <- log10(vector_len$V2)

#require('ggplot')
plot(vector_len)

data_type = "sim"
sourcePath <- paste("/home/pdendek/icm_kod/Projects/CoAnSys/document-similarity/document-similarity-logic/src/main/resources/rd/number_of_docs_with_given_",data_type,"/",sep="")

# vals = c(965,97,975,98,985,99,995,996,998)
vals = c(97,985,995,996,998)
colrs = rainbow(length(vals))
###################
###################
###################
mpch=18
j=length(vals)
typ='l'
B <- read.csv(paste(sourcePath,"/0_",vals[j],".csv",sep=""), header=F)
B <- B[order(B$V1),]
B$V2 <- log10(B$V2)
plot(B,type=typ,col=colrs[j],pch=mpch,
     main="Num of docs represented with vector of given lenght",
     xlab="Length of vector representation (max=80)",
     ylab="Number of documents")
for(j in 1:(length(vals)-1)){
  #j=5
  B <- read.csv(paste(sourcePath,"/0_",vals[j],".csv",sep=""), header=F)
  B <- B[order(B$V1),]
  B$V2 <- log10(B$V2)
  points(B,type=typ,col=colrs[j],pch=mpch)
}

legend(0.83,9, legend=vals ,lty=1, col=colrs, bty='n',cex=.825)
###################
###################
###################
length(vals)
length(colrs)
