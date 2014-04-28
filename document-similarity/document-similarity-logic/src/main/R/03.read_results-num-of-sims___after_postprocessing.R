vector_len <- read.csv("~/vector_len.csv", header=F)

vector_len$V2 <- log10(vector_len$V2)

#require('ggplot')
plot(vector_len)



data_type = "sim"
sourcePath <- paste("/home/pdendek/Pulpit/docsim-check/postprocessing_correction","/",sep="")

# vals = c(965,97,975,98,985,99,995,996,998)
vals = c('pre','post')
colrs = rainbow(length(vals))
###################
###################
###################
mpch=18
j=1
typ='l'

inplot=rbind(c(0.9,1.7),c(1.0,4.6))
#inplot=B

plot(inplot,type="n",
     main="Num of doc pairs with given similarity level",
     xlab="Similarity level (sim=[0;1])",
     ylab="Number of pairs of documents (log10)")
#legend('bottomleft', legend=vals ,lty=1, col=colrs, bty='n',cex=.675)

for(j in 1:2){

  B <- read.csv(paste(sourcePath,vals[j],".csv",sep=""), header=F)
  B <- B[order(B$V1),]
  B <- B[B$V1>=0.9,]
  B$V2 <- log10(B$V2)
  points(B,type=typ,col=colrs[j],pch=mpch)
}
legend("topleft", legend=c('WITHOUT correction','WITH correction') ,lty=1, col=colrs, bty='n',cex=1.0)
#legend(0.7,8.5, legend=vals ,lty=1, col=colrs, bty='n',cex=1.0)
###################
###################
###################
length(vals)
length(colrs)
