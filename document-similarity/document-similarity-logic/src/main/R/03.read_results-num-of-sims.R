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

C  <- B
Cl <- c(sum(C$V2)/10^6)
Cl2 <- seq(0.0,0.9,0.1)
for(i in seq(0.1,0.9,0.1)){
  Cl <- c(Cl, sum(B[B$V1>i,]$V2)/10^6)
}


cbind(Cl2,Cl)

Cl

Cl/(sum(C$V2)/10^6)*100



B <- B[B$V1>0.95,]
B$V2 <- log10(B$V2)
plot(B,type=typ,col=colrs[j],pch=mpch,
     main="Num of doc pairs with given similarity level",
     xlab="Similarity level (sim=[0;1])",
     ylab="Number of pairs of documents (log10)")
for(j in 1:(length(vals)-1)){
  #j=5
  B <- read.csv(paste(sourcePath,"/0_",vals[j],".csv",sep=""), header=F)
  B <- B[order(B$V1),]
  B <- B[B$V1>0.95,]
  B$V2 <- log10(B$V2)
  points(B,type=typ,col=colrs[j],pch=mpch)
}
legend(0.97,5.0, legend=vals ,lty=1, col=colrs, bty='n',cex=1.0)
#legend(0.7,8.5, legend=vals ,lty=1, col=colrs, bty='n',cex=1.0)
###################
###################
###################
length(vals)
length(colrs)
