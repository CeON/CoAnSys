pathToInputFile <- "/home/pdendek/icm_dane/pbn_bojan_1mln.csv"
pathToInputFile2 = "/home/pdendek/icm_dane/pbn_bojan_1mln_corrected.csv"
mungle_data <- function(pathToInputFile,pathToInputFile2){
  print("FUN:mungle_data()")
  pbn_bojan <- read.delim(pathToInputFile, header=F)
  #V2: EX_CLASSIFICATION_CODES#V5: EX_KEYWORDS_SPLIT#V8: EX_KEYWORDS#V11: EX_TITLE_SPLIT
  #V14: EX_YEAR#V17: EX_TITLE#V20: EX_DOC_AUTHS_SNAMES#V23: EX_DOC_AUTHS_FNAME_FST_LETTER
  #V26: EX_AUTH_FNAMES_FST_LETTER#V29: EX_AUTH_FNAME_FST_LETTER#V32: EX_PERSON_ID#V35: EX_EMAIL
  class(pbn_bojan)
  features.name.alllist <- unlist(pbn_bojan[1,seq(2,35,3)])
  
  features.name.1 <- as.vector( features.name.alllist[-(length(features.name.alllist)-1)] )
  features.name.2 <- as.vector( features.name.alllist[length(features.name.alllist)-1] )
  features.name <- c(features.name.1,features.name.2)
  head(features.name)
  features.value.max <- pbn_bojan[,seq(2,35,3)+1]
  features.value.contr <- pbn_bojan[,seq(2,35,3)+2]
  
  features.value.contr.sorted1 <- features.value.contr[,-(length(features.name.alllist)-1)]
  features.value.contr.sorted2 <- features.value.contr[,(length(features.name.alllist)-1)]
  features.value.contr.sorted <- cbind(features.value.contr.sorted1,features.value.contr.sorted2)
  names(features.value.contr.sorted) <- features.name
  head(features.value.contr.sorted)
  
  all = nrow(features.value.contr.sorted)
  same.ids = features.value.contr.sorted[,ncol(features.value.contr.sorted)]==1
  notsame.ids = !same.ids
  
  same.val = sum(same.ids)
  notsame.val = all - same.val
  same.perc = same.val/all
  notsame.perc = notsame.val/all
  
  sn = same.perc/notsame.perc
  ns = notsame.perc/same.perc
  
  sum(same.ids)
  
  if(sn>0){
    same.ids <- runif(all)<same.perc/sn & same.ids
  }else{
    notsame.ids <- runif(all)<notsame.perc/ns & notsame.ids
  }
  
  #features.value.contr.final <- features.value.contr.sorted[same.ids | notsame.ids,]
  features.value.contr.final <- features.value.contr.sorted
  
  write.csv(features.value.contr.final,pathToInputFile2, 
              row.names=F,quote=F)
}
#########################################################
#########################################################
#########################################################
if (! "reshape2" %in% row.names(installed.packages()))
  install.packages('reshape2')
require(reshape2)

if (! "kernlab" %in% row.names(installed.packages()))
   install.packages('kernlab', repos="http://cran.rstudio.com/")
require(kernlab)

getPolarW <- function(model,df){
  print("FUN:getPolarW()")
  w=0
  iter=0  
  for(i in alphaindex(model)[[1]]){
    iter=iter+1
    w = w +  alpha(model)[[1]][iter] * df[i,-ncol(df)] *  df[i,ncol(df)]
  } 
  return (w)
}

giveHyperplane <- function(model,df){
  print("FUN:giveHyperplane()")
  # w = sum_{s \in SV} a_0s \cdot y_s \cdot x_x
  b0 <- b(model)
  w <- getPolarW(model,df)
  v <- vector()  
  for(index in 1:length(w)){
    v <- append(v,paste(names(df)[index],w[index],sep="\t"))
  }
  v <- append(v,paste("THRESHOLD",b0,sep="\t"))
  results <- paste(v,collapse="\n")
  return (results)
}

getTP_FP_TN_FN <- function(x,pred){
  print("FUN:getTP_FP_TN_FN()")
  t <- cbind(x,pred)
  
  tn = nrow(t[(t[,1]==0) * (t[,2]==0),])
  tp = nrow(t[(t[,1]==1) * (t[,2]==1),])
  fp = nrow(t[(t[,1]==0) * (t[,2]==1),])
  fn = nrow(t[(t[,1]==1) * (t[,2]==0),])
  all = nrow(t)
  
  acc = (tp+tn)/all
  prec = tp/(tp+fp)
  f1 = 2*tp/(2*tp+fp+fn)
  
  return(paste('acc',acc,'prec',prec,'f1',f1,sep=" "))
}

pI <-  "/home/pdendek/icm_dane/pbn_bojan_corrected.csv"
main <- function(pI){
  print("FUN:main()")
  #read input <f1,f2,...,fN,same>
  tabela <- read.table(pI, header=T,sep=',',dec='.',stringsAsFactors=F)
  for(i in 1:length(tabela)){
    tabela[,i] <- as.double(tabela[,i])
  }
  #change last param name to "decision"
  names(tabela)[ncol(tabela)] <- "decision"
  #calculate linear SVM model
  split.train.test.thre <- 0.8
  split.train.test.vals <- runif(nrow(tabela))
  train <- tabela[split.train.test.vals<=split.train.test.thre,]
  test  <- tabela[split.train.test.vals>split.train.test.thre,]
  model <- ksvm(decision~., data=as.matrix(train), type="C-svc", scaled=F,kernel = "vanilladot");
  
  eval <- getTP_FP_TN_FN(
    predict(model,test[,-length(test)]),
    test[,length(test)]
    )
  
  #calculate separative hyperplane function and print it
  return(c(giveHyperplane(model,tabela)[1],eval))
}

run <- function(){
  print("FUN:run()")
  sta <- date()
  inData <- "/home/pdendek/icm_dane/pbn_bojan_1mln.csv"
  midData <- "/home/pdendek/icm_dane/pbn_bojan_1mln_corrected.csv"
  retVals <- main(midData)
  print(paste("START",sta))
  print(paste("STOP",date()))
  return(retVals)
}

ret <- run()

#cat(paste(ret,collapse='\n\n'))
date()
cat(paste(run(),collapse='\n\n'))
date()

