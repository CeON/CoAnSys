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
colnames(d)
levels(factor(d[,1]))

tmp <- d[ ,c(2,3,4,5,6,8,9,11)]
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



d<-result
tmp <- d
tmp <- d[d[,10]==NA,]
head(d[,])
# dim(d)
tmp <- d[ ,c(2,3,4,5,6,8,9,11)]
head(tmp)
tmp <- tmp[tmp$ker==1,]

length(mod_le <- levels(factor( tmp$mod )))
length(ker_le <- levels(factor( tmp$ker )))
length(deg_le <- levels(factor( tmp$deg )))
length(gam_le <- levels(factor( tmp$gam )))
length(coef_le <- levels(factor( tmp$coef )))


### DEG  -> {AvgCorr,MaxCorr,TimeElapsed} 
### PLOT
for(finval in c(6,7,8)){  
  for(mod in mod_le){
#   now<-paste(date(),proc.time(),sep="_")
#   fil=paste("/home/pdendek/WMH/00 badania/98 wyniki/wmh_with_null_",now,".pdf",sep="")
#   pdf(file=fil, height=3.5, width=5)
#   # Trim off excess margin space (bottom, left, top, right)
#   par(mar=c(4.2, 3.8, 0.2, 0.2))
  ### produce chart of given FinalVal
  g_range <- range(min(tmp[,finval]),max(tmp[,finval]))    
  print(fil)
  print(g_range)
  #first of list
  
  palette(rainbow(length(deg_le)))
  
  i = deg_le[2]
  i_deg = tmp[tmp$deg==i,]  
  i_deg = tmp[tmp$mod==mod,]
  
  plot(sort(i_deg[,finval],decreasing=FALSE), 
       lty=which(deg_le==i), 
       col=palette()[1], 
       ylim=g_range, 
       axes=TRUE, ann=FALSE, type="l")
  
  for(i in deg_le[c(1,3:length(deg_le))]){
    i_deg = tmp[tmp$deg==deg_le[4],]  
    i_deg = tmp[tmp$mod==mod,]
    ### produce line of given MOD
    # Graph trucks with red dashed line and square points
    lines(sort(i_deg[,finval],decreasing=FALSE), lty=which(deg_le==i), 
          col=palette()[which(deg_le==i)]
          )    
  
    }
  title(xlab="Number of model version")
  title(ylab=names(tmp)[finval])
  
  legend(1, g_range[2], deg_le, cex=0.8, 
   palette(), lty=1:length(mod_le));
  title(main=paste("Model",mod), col.main="red", font.main=4)
  # Turn off device driver (to flush output to PDF)
#   dev.off()

  # Restore default margins
#   par(mar=c(5, 4, 4, 2) + 0.1)
}}

### MODIFICATIONS -> {AvgCorr,MaxCorr,TimeElapsed} 
### SUMMATION
tmp <- d
tmp <- d[d[,10]==NA,]
head(d[,])
# dim(d)
tmp <- d[ ,c(2,3,4,5,6,8,9,11)]

for(finval in c(6,7,8)){
  print(paste("===================",names(tmp)[finval],"==================="))
#   finval=7
  lst <-list()
  iter=0
  for(i in deg_le){
#     i=mod_le[1]
    iter=iter+1;
    ### produce line of given MOD
    i_deg = tmp[tmp$deg==i,]
    
    # Graph trucks with red dashed line and square points
    lst[[iter]]= data.frame(name=i,avg=sum(i_deg[,finval])/nrow(i_deg)
                            )
    #print(paste(i,": ",sum(i_mod[,finval])/nrow(i_mod),sep=""))
  }
  final = do.call("rbind", lst)
  final <- final[order(final$avg,decreasing=TRUE),]
  print(final)
  print(paste("Difference:",final[1,2]-final[length(mod_le),2]))
}




### DEG >> MOD >> GAM  ->  {AvgCorr,MaxCorr,TimeElapsed} 
### PLOT
palette(rainbow(length(deg_le)))

counter=0
tmplst <- list()
for(finval in c(8)){  
  
  finval = 8
  
  g_range <- range(min(tmp[,finval]),max(tmp[,finval]))
  for(mod in mod_le[2]){
    
    mod = mod_le[2]
    
    for(gam in gam_le){    
      
      gam=gam_le[1]
      
      now<-paste(date(),proc.time(),sep="_")
      fil=paste("/home/pdendek/WMH/00 badania/98 wyniki/__wmh_with_null_",now,".pdf",sep="")
      pdf(file=fil, height=3.5, width=5)
      par(mar=c(4.2, 3.8, 0.2, 0.2))

      tmp <- d[ ,c(2,3,4,5,6,8,9,11)]
      
      z=1
      
    
       for(mod in mod_le){
      for(gam in c(gam_le[4],gam_le[1],gam_le[2],gam_le[3]) ){
    for(coef in coef_le){   
        i_deg <- tmp
        i_deg <- i_deg[!is.na(x=i_deg$coef )  ,  ]
#         head(i_deg)
#         i <- deg_le[z]
#         i_deg <- tmp[tmp$deg==i,]  
        i_deg <- i_deg[i_deg$coef ==coef,]
        i_deg <- i_deg[i_deg$mod==mod,]
        i_deg <- i_deg[i_deg$gam==gam,]
        i_deg <- i_deg[,c(3,8)] #3=deg, 5=coef
        i_deg
      print(mean(i_deg$time.elapsed))
        plot(i_deg)
        title(main=paste("Ceof =",coef,"Model:",mod,
                         "Gamma =",gam,"Mean =",mean(i_deg$time.elapsed)), 
              col.main="red", font.main=2)
      }
    }
    }
      
      
      z=1
      for(z in 1:9){
        i <- deg_le[z]
        i_deg <- tmp[tmp$deg==i,]  
        i_deg <- i_deg[i_deg$mod==mod,]
        i_deg <- i_deg[i_deg$gam==gam,]
        i_deg <- i_deg[,c(5,8)]
        i_deg
      
        plot(i_deg)
        title(main=paste("Degree",z,"Gamma",gam), col.main="red", font.main=4)
      }
      
    
      sort(i_deg[,finval]
      
      plot(sort(i_deg[,finval],decreasing=FALSE), 
        lty=which(deg_le==i), 
        col=palette()[1], 
        ylim=g_range, 
        axes=TRUE, ann=FALSE, type="l")
  
      counter=counter+1;
      tmplst[[counter]]=data.frame(finval=finval,mod=mod,deg=i)
      
      for(i in deg_le[2:length(deg_le)]){
#         counter=counter+1;
#         tmplst[[counter]]=data.frame(finval=finval,mod=mod,deg=i)
        lines(sort(i_deg[,finval],decreasing=FALSE), lty=which(deg_le==i), 
        col=palette()[which(deg_le==i)])
      }
      title(xlab=paste("Number of degree model version"))
      title(ylab=names(tmp)[finval])
  
      legend(1, g_range[2], deg_le, cex=0.8, 
        palette(), lty=1:length(mod_le));
      title(main=paste("Gamma",gam), col.main="red", font.main=4)

      dev.off()  
      par(mar=c(5, 4, 4, 2) + 0.1)    
    }
  }
}
do.call("rbind",tmplst)


### COEF -> {AvgCorr,MaxCorr,TimeElapsed}
### GAM  -> {AvgCorr,MaxCorr,TimeElapsed}
### [[@DONE already]]] DATA_MODIFICATION  -> {AvgCorr,MaxCorr,TimeElapsed}













