vector_len <- read.csv("~/vector_len.csv", header=F)

vector_len$V2 <- log10(vector_len$V2)

require('ggplot')
plot(vector_len)

A <- read.csv("~/Pulpit/number_of_docs_with_given_len/0_955.csv", header=F)
vals = c(955,96,965,97,975,98,985,99,995,996,998,999)
colrs = rainbow(length(vals))

plot(A,type="l",col=colrs[1],
     main="Num of docs represented with vector of given lenght",
     xlab="Length of vector representation (max=80)",
     ylab="Number of documents")
for(j in 2:length(vals)){
  B <- read.csv(paste("~/Pulpit/number_of_docs_with_given_len/0_",vals[j],".csv",sep=""), header=F)
  lines(B,type="l",col=colrs[j])
}

legend('topright', legend=vals ,lty=1, col=colrs, bty='n',cex=.5)

length(vals)
length(colrs)
