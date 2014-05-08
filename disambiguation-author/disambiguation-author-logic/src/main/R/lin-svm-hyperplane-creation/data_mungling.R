pathToInputFile <- "/home/pdendek/icm_dane/pbn_bojan_1k.csv"
pbn_bojan <- read.delim(pathToInputFile, header=F)
#V2: EX_CLASSIFICATION_CODES 1.0     0.7071067811865475      
#V5: EX_KEYWORDS_SPLIT     0.0     0.0     
#V8: EX_KEYWORDS     0.0     0.0     
#V11: EX_TITLE_SPLIT  0.0     0.0     
#V14: EX_YEAR       0.0     0.0     
#V17: EX_TITLE        0.0     0.0     
#V20: EX_DOC_AUTHS_SNAMES     1.0     0.5773502691896258    
#V23: EX_DOC_AUTHS_FNAME_FST_LETTER   2.0     0.8944271909999159      
#V26: EX_AUTH_FNAMES_FST_LETTER    1.0      1.0     
#V29: EX_AUTH_FNAME_FST_LETTER        1.0     1.0     
#V32: EX_PERSON_ID    1.0     1.0     
#V35: EX_EMAIL      0.0     0.0
class(pbn_bojan)
features.name.alllist <- unlist(pbn_bojan[1,seq(2,35,3)])
features.name.1 <- as.vector( features.name.alllist[-(length(features)-1)] )
features.name.2 <- as.vector( features.name.alllist[length(features)-1] )
features.name <- c(features.name.1,features.name.2)
features.value.max <- pbn_bojan[,seq(2,35,3)+1]
features.value.contr <- pbn_bojan[,seq(2,35,3)+2]

features.value.contr.sorted1 <- features.value.contr[,-(length(features)-1)]
features.value.contr.sorted2 <- features.value.contr[,(length(features)-1)]
features.value.contr.sorted <- cbind(features.value.contr.sorted1,features.value.contr.sorted2)
names(features.value.contr.sorted) <- features.name
head(features.value.contr.sorted)

pathToInputFile2 = "/home/pdendek/icm_dane/pbn_bojan_1k_corrected.csv"
write.csv(features.value.contr.sorted,pathToInputFile2, 
            row.names=F,col.names=F,dec='.',quote=F,sep=',')


#########################################################
#########################################################
#########################################################

if (! "kernlab" %in% row.names(installed.packages()))
  install.packages('kernlab')
require(kernlab)

getPolarW <- function(model,df){
  w=0
  iter=0  
  for(i in alphaindex(model)[[1]]){
    iter=iter+1
    w = w +  alpha(model)[[1]][iter] * df[i,-ncol(df)] *  df[i,ncol(df)]
  } 
  return (w)
}

giveHyperplane <- function(model,df){
  # w = sum_{s \in SV} a_0s \cdot y_s \cdot x_x
  b0 <- b(model)
  w <- getPolarW(model,df)
  v <- vector  
  for(index in 1:length(w)){
    v <- append(v,paste(names(df)[index],w[index],sep="\t"))
  }
  v <- append(v,paste("THRESHOLD",b0,sep="\t"))
  results <- paste(v,collapse="\n")
  return (results)
}

main <- function(pI){
  #read input <f1,f2,...,fN,same>
  tabela <- read.table(pI, header=T,sep=',',dec='.',stringsAsFactors=F)
  for(i in 1:length(tabela)){
    tabela[,i] <- as.double(tabela[,i])
  }
  #change last param name to "decision"
  names(tabela)[ncol(tabela)] <- "decision"
  #calculate linear SVM model
  model <- ksvm(decision~., data=as.matrix(tabela), type="C-svc", scaled=F,kernel = "vanilladot");
  #calculate separative hyperplane function and print it
  cat(giveHyperplane(model,tabela)[1])
}

pathToInputFile <- "/home/pdendek/icm_dane/pbn_bojan_1k_corrected.csv"
main(pathToInputFile)
