d<-result
d[d[,2]==-1,2] <- "NoChange"
d[d[,2]==0,2] <- "MinusBoolThreeVal"
d[d[,2]==1,2] <- "MinusBoolLinScale"
d[d[,2]==2,2] <- "MinusBoolSigmScale"
d[d[,2]==10,2] <- "MinusBayTwoVal"
d[d[,2]==11,2] <- "MinusBayLinScale"
d[d[,2]==12,2] <- "MinusBaySigmScale"
tmp <- d
tmp <- d[d[,10]==NA,]
# head(d[,])
# dim(d)

tmp <- d[ ,c(2,3,8,9,11)]
tmp[tmp$ker==0, ]

"=============== 11111111 ============================"
now = tmp[tmp$ker==0,]
print(now[order(now$avg.correct.rate.,decreasing=TRUE),])
"================222222222 ========================"
now = tmp[tmp$ker==0,]
print(now[order(now$max.correct.rate.,decreasing=TRUE),])
"================333333333 ========================="
now = tmp[tmp$ker==0,]
print(now[order(now$time.elapsed,decreasing=FALSE),])
